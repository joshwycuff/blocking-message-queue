package io.github.joshwycuff;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.BlockingQueue;

/**
 * A Sender class used to send messages to multiple blocking queues.
 *
 * The Sender class allows the sending of messages of type T to multiple blocking queues.
 * It provides methods to send messages and close the queues.
 *
 * Usage Examples:
 *
 * // Create a new BlockingMessageQueue for Integer messages
 * BlockingMessageQueue<Integer> messageQueue = new BlockingMessageQueue<>();
 *
 * // Create a Sender
 * Sender<Integer> sender = messageQueue.sender();
 *
 * // Sending a message to all queues
 * sender.send(123);
 *
 * // Closing all queues
 * sender.close();
 *
 * @param <T> the type of messages to be sent
 */
public class Sender<T> {

    /**
     * The list of blocking queues to send messages to.
     */
    protected final List<BlockingQueue<Optional<T>>> queues;

    /**
     * Constructs a new Sender object with the specified list of blocking queues.
     *
     * @param queues the list of blocking queues to send messages to
     */
    public Sender(List<BlockingQueue<Optional<T>>> queues) {
        this.queues = queues;
    }

    /**
     * Sends a message to all the blocking queues.
     *
     * @param t the message to be sent
     * @throws InterruptedException if the sending operation is interrupted
     */
    public void send(T t) throws InterruptedException {
        for (BlockingQueue<Optional<T>> queue : queues) {
            queue.put(Optional.of(t));
        }
    }

    /**
     * Closes all the blocking queues.
     *
     * @throws InterruptedException if the closing operation is interrupted
     */
    public void close() throws InterruptedException {
        for (BlockingQueue<Optional<T>> queue : queues) {
            queue.put(Optional.empty());
        }
    }
}
