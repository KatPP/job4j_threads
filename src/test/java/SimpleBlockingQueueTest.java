import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.job4j.concurrent.SimpleBlockingQueue;

import static org.junit.jupiter.api.Assertions.*;

class SimpleBlockingQueueTest {

    @Test
    @DisplayName("Производитель добавляет элемент, потребитель его извлекает")
    void singleProducerSingleConsumer() throws InterruptedException {
        SimpleBlockingQueue<Integer> queue = new SimpleBlockingQueue<>(1);
        final int[] producedValue = {0};
        final int[] consumedValue = {0};

        Thread producer = new Thread(() -> {
            producedValue[0] = 42;
            queue.offer(producedValue[0]);
        });

        Thread consumer = new Thread(() -> {
            try {
                consumedValue[0] = queue.poll();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        producer.start();
        producer.join();

        consumer.start();
        consumer.join();

        assertEquals(42, producedValue[0], "Произведено неверное значение");
        assertEquals(producedValue[0], consumedValue[0], "Потреблено неверное значение");
        assertTrue(queue.isEmpty(), "Очередь должна быть пустой");
    }

    @Test
    @DisplayName("Потребитель ждет, когда производитель добавит элемент")
    void consumerWaitsForProducer() throws InterruptedException {
        SimpleBlockingQueue<Integer> queue = new SimpleBlockingQueue<>(1);
        final boolean[] consumerFinished = {false};

        Thread consumer = new Thread(() -> {
            try {
                queue.poll();
                consumerFinished[0] = true;
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        Thread producer = new Thread(() -> {
            try {
                Thread.sleep(500); // Имитируем задержку
                queue.offer(1);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        consumer.start();
        Thread.sleep(100);

        producer.start();
        producer.join();
        consumer.join();

        assertTrue(consumerFinished[0], "Потребитель должен был завершиться");
        assertTrue(queue.isEmpty(), "Очередь должна быть пустой");
    }

    @Test
    @DisplayName("Производитель ждет, когда потребитель освободит место")
    void producerWaitsForConsumer() throws InterruptedException {
        SimpleBlockingQueue<Integer> queue = new SimpleBlockingQueue<>(1);
        final boolean[] producerFinished = {false};

        queue.offer(1);

        Thread producer = new Thread(() -> {
            queue.offer(2);
            producerFinished[0] = true;
        });

        Thread consumer = new Thread(() -> {
            try {
                Thread.sleep(500);
                queue.poll();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        producer.start();
        Thread.sleep(100);

        consumer.start();
        consumer.join();
        producer.join();

        assertTrue(producerFinished[0], "Производитель должен был завершиться");
        assertEquals(2, queue.poll(), "В очереди должен быть второй элемент");
    }
}