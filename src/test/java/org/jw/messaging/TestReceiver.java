package org.jw.messaging;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

public class TestReceiver {
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
    public void test_receive() throws InterruptedException {
        assertThat(receiver.queue.size()).isEqualTo(0);

        sender.send(10);
        sender.send(20);

        assertThat(receiver.queue.size()).isEqualTo(2);

        assertThat(receiver.receive()).isEqualTo(10);
        assertThat(receiver.receive()).isEqualTo(20);

        assertThat(receiver.queue.size()).isEqualTo(0);
    }


    @Test
    public void test_forEach() throws InterruptedException {
        assertThat(receiver.queue.size()).isEqualTo(0);

        sender.send(1);
        sender.send(10);
        sender.close();

        assertThat(receiver.queue.size()).isEqualTo(3);

        AtomicInteger sum = new AtomicInteger();

        receiver.forEach(sum::addAndGet);

        assertThat(receiver.queue.size()).isEqualTo(0);

        assertThat(sum.get()).isEqualTo(11);
    }

    @Test
    public void test_clone() {
        Receiver<Integer> receiver2 = receiver.clone();

        assertThat(receiver2).isNotSameAs(receiver);
        assertThat(receiver2.queue).isNotSameAs(receiver.queue);
        assertThat(receiver2.blockingMessageQueue).isSameAs(receiver.blockingMessageQueue);
    }
}
