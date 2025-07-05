import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.job4j.concurrent.SimpleBlockingQueue;

import static org.junit.jupiter.api.Assertions.*;

class SimpleBlockingQueueTest {

    @Test
    @DisplayName("Производитель добавляет 10 элементов, потребитель их извлекает")
    void producerAdds10ElementsConsumerConsumesThem() throws InterruptedException {
        SimpleBlockingQueue<Integer> queue = new SimpleBlockingQueue<>(5);

        Thread producer = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                queue.offer(i);
            }
        });

        Thread consumer = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                try {
                    queue.poll();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });

        producer.start();
        consumer.start();

        producer.join();
        consumer.join();

        assertTrue(queue.isEmpty(), "Очередь должна быть пустой после извлечения всех элементов");
    }

    @Test
    @DisplayName("При заполненной очереди производитель блокируется до освобождения места")
    void producerBlocksWhenQueueIsFull() throws InterruptedException {
        SimpleBlockingQueue<Integer> queue = new SimpleBlockingQueue<>(2);

        queue.offer(1);
        queue.offer(2);

        Thread producer = new Thread(() -> {
            queue.offer(3);
        });

        Thread consumer = new Thread(() -> {
            try {
                Thread.sleep(1000);
                queue.poll();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        producer.start();
        consumer.start();

        producer.join();
        consumer.join();

        assertEquals(2, queue.poll(), "Первый элемент должен быть 2");
        assertEquals(3, queue.poll(), "Второй элемент должен быть 3");
    }

    @Test
    @DisplayName("При пустой очереди потребитель блокируется до появления элементов")
    void consumerBlocksWhenQueueIsEmpty() throws InterruptedException {
        SimpleBlockingQueue<Integer> queue = new SimpleBlockingQueue<>(2);

        Thread consumer = new Thread(() -> {
            try {
                queue.poll();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        Thread producer = new Thread(() -> {
            try {
                Thread.sleep(1000);
                queue.offer(1);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        consumer.start();
        producer.start();

        consumer.join();
        producer.join();

        assertTrue(queue.isEmpty(), "Очередь должна быть пустой после извлечения единственного элемента");
    }

    @Test
    @DisplayName("Многопоточная работа с несколькими производителями и потребителями")
    void multipleProducersAndConsumers() throws InterruptedException {
        SimpleBlockingQueue<Integer> queue = new SimpleBlockingQueue<>(10);
        final int count = 100;

        Thread producer1 = new Thread(() -> {
            for (int i = 0; i < count; i++) {
                queue.offer(i);
            }
        });

        Thread producer2 = new Thread(() -> {
            for (int i = count; i < count * 2; i++) {
                queue.offer(i);
            }
        });

        Thread consumer1 = new Thread(() -> {
            for (int i = 0; i < count; i++) {
                try {
                    queue.poll();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });

        Thread consumer2 = new Thread(() -> {
            for (int i = 0; i < count; i++) {
                try {
                    queue.poll();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });

        producer1.start();
        producer2.start();
        consumer1.start();
        consumer2.start();

        producer1.join();
        producer2.join();
        consumer1.join();
        consumer2.join();

        assertTrue(queue.isEmpty(), "Очередь должна быть пустой после работы всех потоков");
    }
}
