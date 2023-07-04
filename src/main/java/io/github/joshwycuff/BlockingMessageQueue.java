package io.github.joshwycuff;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * A thread-safe implementation of a blocking message queue.
 *
 * This class allows multiple threads to send and receive messages through a shared queue.
 * Sending and receiving operations are blocked when the queue is empty or full, respectively,
 * ensuring proper synchronization between the sender and receiver threads.
 *
 * // Create a new BlockingMessageQueue for Integer messages
 * BlockingMessageQueue<Integer> messageQueue = new BlockingMessageQueue<>();
 *
 * // Create a Sender and a Receiver for the message queue
 * Sender<Integer> sender = messageQueue.sender();
 * Receiver<Integer> receiver = messageQueue.receiver();
 *
 * // Send messages through the sender
 * sender.send(10);
 * sender.send(20);
 * sender.send(30);
 *
 * // Receive messages through the receiver
 * System.out.println(receiver.receive()); // Output: 10
 * System.out.println(receiver.receive()); // Output: 20
 * System.out.println(receiver.receive()); // Output: 30
 *
 * @param <T> the type of messages to be sent and received
 */
public class BlockingMessageQueue<T> {

    /**
     * The list of blocking queues used to store the messages.
     * Each blocking queue represents a channel for sending and receiving messages.
     */
    protected final List<BlockingQueue<Optional<T>>> queues;

    /**
     * The sender associated with this blocking message queue.
     */
    protected final Sender<T> sender;

    /**
     * Constructs a new BlockingMessageQueue object.
     * Initializes the list of blocking queues and the sender.
     */
    public BlockingMessageQueue() {
        queues = new ArrayList<>();
        sender = new Sender<>(queues);
    }

    /**
     * Retrieves the sender associated with this blocking message queue.
     *
     * @return the sender object
     */
    public Sender<T> sender() {
        return sender;
    }

    /**
     * Creates a new receiver that can be used to receive messages from this blocking message queue.
     *
     * @return a new Receiver object
     */
    public Receiver<T> receiver() {
        BlockingQueue<Optional<T>> queue = new LinkedBlockingQueue<>();
        queues.add(queue);
        return new Receiver<>(this, queue);
    }

}
