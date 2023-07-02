# Blocking Message Queue

The Blocking Message Queue is a thread-safe implementation of a blocking message queue in Java. It allows multiple threads to send and receive messages through a shared queue. The sending and receiving operations are blocked when the queue is empty or full, respectively, ensuring proper synchronization between the threads.

## Usage

Here's an example of how to use the Blocking Message Queue:

```java
// Create a new BlockingMessageQueue for Integer messages
BlockingMessageQueue<Integer> messageQueue = new BlockingMessageQueue<>();

// Create a Sender and a Receiver for the message queue
Sender<Integer> sender = messageQueue.sender();
Receiver<Integer> receiver = messageQueue.receiver();

// Send messages through the sender
sender.send(10);
sender.send(20);
sender.send(30);

// Receive messages through the receiver
System.out.println(receiver.receive()); // Output: 10
System.out.println(receiver.receive()); // Output: 20
System.out.println(receiver.receive()); // Output: 30
```

Here's an example showcasing the usage of `sender.close()` and `receiver.forEach()`:

```java
BlockingMessageQueue<String> messageQueue = new BlockingMessageQueue<>();
Sender<String> sender = messageQueue.sender();
Receiver<String> receiver = messageQueue.receiver();

// Send messages through the sender
sender.send("Hello");
sender.send("World");
sender.send("!");

// Close the sender
sender.close();

// Iterate over received messages using forEach
receiver.forEach(message -> {
    System.out.println("Received message: " + message);
});
```

Output:
```
Received message: Hello
Received message: World
Received message: !
```

## Key Features

- Supports sending and receiving messages of any type.
- Multiple threads can send messages concurrently.
- Blocking behavior ensures proper synchronization between sender and receiver threads.
- Supports iterating over received messages using a consumer function.
- Supports cloning the receiver for multiple concurrent receivers.
- Supports closing the queues from the sender.

## API Reference

### BlockingMessageQueue<T>

A thread-safe implementation of a blocking message queue.

#### Methods

- `sender()`: Retrieves the sender associated with this blocking message queue.
- `receiver()`: Creates a new receiver that can be used to receive messages from this blocking message queue.

### Sender<T>

A class used to send messages to multiple blocking queues.

#### Methods

- `send(T t)`: Sends a message to all the blocking queues.
- `close()`: Closes all the blocking queues.

### Receiver<T>

A class used to receive messages from a blocking message queue.

#### Methods

- `receive()`: Receives a message from the queue.
- `forEach(Consumer<T> consumer)`: Iterates over received messages using the specified consumer function.
- `clone()`: Creates a clone of the receiver.

## License

This project is licensed under the MIT License. See [LICENSE](LICENSE) for more information.
