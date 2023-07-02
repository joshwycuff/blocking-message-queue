package org.jw.messaging;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TestBlockingMessageQueue {
    BlockingMessageQueue<Integer> blockingMessageQueue;
    Sender<Integer> sender;
    Receiver<Integer> receiver;

    @BeforeEach
    public void beforeEach() {
        blockingMessageQueue = new BlockingMessageQueue<>();
        sender = blockingMessageQueue.sender();
        receiver = blockingMessageQueue.receiver();
    }

    @Test
    public void test_constructor() {
        BlockingMessageQueue<Integer> blockingMessageQueue = new BlockingMessageQueue<>();

        assertThat(blockingMessageQueue.queues.size()).isEqualTo(0);
        assertThat(blockingMessageQueue.sender).isInstanceOf(Sender.class);
    }

    @Test
    public void test_sender() {
        assertThat(sender).isSameAs(blockingMessageQueue.sender);
    }

    @Test
    public void test_receiver() {
        BlockingMessageQueue<Integer> blockingMessageQueue = new BlockingMessageQueue<>();

        assertThat(blockingMessageQueue.queues.size()).isEqualTo(0);

        Receiver<Integer> receiver = blockingMessageQueue.receiver();

        assertThat(blockingMessageQueue.queues.size()).isEqualTo(1);
    }

    @Test
    public void test_secondReceiver_delayed() throws InterruptedException {
        assertThat(blockingMessageQueue.queues.size()).isEqualTo(1);
        assertThat(blockingMessageQueue.queues.get(0).size()).isEqualTo(0);

        sender.send(1);

        assertThat(blockingMessageQueue.queues.size()).isEqualTo(1);
        assertThat(blockingMessageQueue.queues.get(0).size()).isEqualTo(1);

        Receiver<Integer> receiver2 = blockingMessageQueue.receiver();

        assertThat(blockingMessageQueue.queues.size()).isEqualTo(2);
        assertThat(blockingMessageQueue.queues.get(0).size()).isEqualTo(1);
        assertThat(blockingMessageQueue.queues.get(1).size()).isEqualTo(0);

        sender.send(2);

        assertThat(blockingMessageQueue.queues.size()).isEqualTo(2);
        assertThat(blockingMessageQueue.queues.get(0).size()).isEqualTo(2);
        assertThat(blockingMessageQueue.queues.get(1).size()).isEqualTo(1);

        assertThat(receiver.receive()).isEqualTo(1);
        assertThat(receiver.receive()).isEqualTo(2);

        assertThat(receiver2.receive()).isEqualTo(2);

        assertThat(blockingMessageQueue.queues.size()).isEqualTo(2);
        assertThat(blockingMessageQueue.queues.get(0).size()).isEqualTo(0);
        assertThat(blockingMessageQueue.queues.get(1).size()).isEqualTo(0);
    }

    @Test
    public void test_clonedReceiver_delayed() throws InterruptedException {
        assertThat(blockingMessageQueue.queues.size()).isEqualTo(1);
        assertThat(blockingMessageQueue.queues.get(0).size()).isEqualTo(0);

        sender.send(1);

        assertThat(blockingMessageQueue.queues.size()).isEqualTo(1);
        assertThat(blockingMessageQueue.queues.get(0).size()).isEqualTo(1);

        Receiver<Integer> receiver2 = receiver.clone();

        assertThat(blockingMessageQueue.queues.size()).isEqualTo(2);
        assertThat(blockingMessageQueue.queues.get(0).size()).isEqualTo(1);
        assertThat(blockingMessageQueue.queues.get(1).size()).isEqualTo(0);

        sender.send(2);

        assertThat(blockingMessageQueue.queues.size()).isEqualTo(2);
        assertThat(blockingMessageQueue.queues.get(0).size()).isEqualTo(2);
        assertThat(blockingMessageQueue.queues.get(1).size()).isEqualTo(1);

        assertThat(receiver.receive()).isEqualTo(1);
        assertThat(receiver.receive()).isEqualTo(2);

        assertThat(receiver2.receive()).isEqualTo(2);

        assertThat(blockingMessageQueue.queues.size()).isEqualTo(2);
        assertThat(blockingMessageQueue.queues.get(0).size()).isEqualTo(0);
        assertThat(blockingMessageQueue.queues.get(1).size()).isEqualTo(0);
    }
}
