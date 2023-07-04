package io.github.joshwycuff;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.BlockingQueue;
import java.util.function.Consumer;

/**
 * The Receiver class is responsible for receiving messages from a blocking message queue.
 * It provides methods to receive messages and iterate over received messages using a consumer function.
 *
 * Usage Examples:
 * 1. Basic Message Reception:
 *    BlockingMessageQueue<Integer> messageQueue = new BlockingMessageQueue<>();
 *    Receiver<Integer> receiver = messageQueue.receiver();
 *    int message = receiver.receive();
 *
 * 2. Iterating over Received Messages:
 *    BlockingMessageQueue<String> messageQueue = new BlockingMessageQueue<>();
 *    Receiver<String> receiver = messageQueue.receiver();
 *
 *    receiver.forEach((message) -> {
 *        System.out.println("Received message: " + message);
 *    });
 *
 * 3. Cloning the Receiver:
 *    BlockingMessageQueue<Double> messageQueue1 = new BlockingMessageQueue<>();
 *    Receiver<Double> receiver1 = messageQueue1.receiver();
 *
 *    // Clone the receiver
 *    Receiver<Double> receiver2 = receiver1.clone();
 *
 * @param <T> the type of messages to be received
 */
public class Receiver<T> {

    /**
     * The blocking message queue that this receiver is tied to.
     */
    protected final BlockingMessageQueue<T> blockingMessageQueue;

    /**
     * The underlying blocking queue used to receive messages.
     */
    protected final BlockingQueue<Optional<T>> queue;

    /**
     * Constructs a new Receiver with the specified blocking message queue and queue.
     *
     * @param blockingMessageQueue the blocking message queue
     * @param queue the queue to receive messages from
     */
    public Receiver(BlockingMessageQueue<T> blockingMessageQueue, BlockingQueue<Optional<T>> queue) {
        this.blockingMessageQueue = blockingMessageQueue;
        this.queue = queue;
    }

    /**
     * Receives a message from the queue.
     *
     * @return the received message
     * @throws InterruptedException if the receiving operation is interrupted
     * @throws NoSuchElementException if there are no more messages in the queue
     */
    public T receive() throws InterruptedException, NoSuchElementException {
        Optional<T> opt = queue.take();
        if (opt.isPresent()) {
            return opt.get();
        } else {
            throw new NoSuchElementException();
        }
    }

    /**
     * Iterates over received messages using the specified consumer function.
     *
     * @param consumer the consumer function to apply to each received message
     * @throws InterruptedException if the iteration is interrupted
     */
    public void forEach(Consumer<T> consumer) throws InterruptedException {
        while (true) {
            try {
                T t = receive();
                consumer.accept(t);
            } catch (NoSuchElementException e) {
                break;
            }
        }
    }

    /**
     * Creates a clone of the receiver.
     *
     * @return a new Receiver object
     */
    @Override
    public Receiver<T> clone() {
        return blockingMessageQueue.receiver();
    }
}
