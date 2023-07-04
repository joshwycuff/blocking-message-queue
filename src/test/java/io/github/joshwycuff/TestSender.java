package io.github.joshwycuff;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class TestSender {
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
    public void test_send() throws InterruptedException {
        assertThat(blockingMessageQueue.queues.size()).isEqualTo(1);
        assertThat(blockingMessageQueue.queues.get(0).size()).isEqualTo(0);

        sender.send(99);

        assertThat(blockingMessageQueue.queues.size()).isEqualTo(1);
        assertThat(blockingMessageQueue.queues.get(0).size()).isEqualTo(1);
        assertThat(blockingMessageQueue.queues.get(0).take()).isEqualTo(Optional.of(99));

        assertThat(blockingMessageQueue.queues.get(0).size()).isEqualTo(0);
    }

    @Test
    public void test_sendMultiple() throws InterruptedException {
        assertThat(blockingMessageQueue.queues.size()).isEqualTo(1);
        assertThat(blockingMessageQueue.queues.get(0).size()).isEqualTo(0);

        sender.send(88);
        sender.send(99);

        assertThat(blockingMessageQueue.queues.size()).isEqualTo(1);
        assertThat(blockingMessageQueue.queues.get(0).size()).isEqualTo(2);
        assertThat(blockingMessageQueue.queues.get(0).take()).isEqualTo(Optional.of(88));
        assertThat(blockingMessageQueue.queues.get(0).take()).isEqualTo(Optional.of(99));

        assertThat(blockingMessageQueue.queues.get(0).size()).isEqualTo(0);
    }

    @Test
    public void test_close() throws InterruptedException {
        assertThat(blockingMessageQueue.queues.size()).isEqualTo(1);
        assertThat(blockingMessageQueue.queues.get(0).size()).isEqualTo(0);

        sender.close();

        assertThat(blockingMessageQueue.queues.size()).isEqualTo(1);
        assertThat(blockingMessageQueue.queues.get(0).size()).isEqualTo(1);
        assertThat(blockingMessageQueue.queues.get(0).take()).isEqualTo(Optional.empty());

        assertThat(blockingMessageQueue.queues.get(0).size()).isEqualTo(0);
    }
}
