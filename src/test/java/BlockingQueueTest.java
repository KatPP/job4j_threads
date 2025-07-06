import org.junit.jupiter.api.Test;
import ru.job4j.concurrent.SimpleBlockingQueue;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Тестирование функциональности блокирующей очереди.
 * Проверяет корректность взаимодействия производителя и потребителя.
 */
public class BlockingQueueTest {

    /**
     * Тестирует сценарий, когда производитель добавляет элементы в очередь,
     * а потребитель извлекает их, проверяя корректность обработки всех элементов.
     */
    @Test
    public void whenProduceAndConsumeThenAllElementsProcessed() throws InterruptedException {
        final CopyOnWriteArrayList<Integer> buffer = new CopyOnWriteArrayList<>();

        final SimpleBlockingQueue<Integer> queue = new SimpleBlockingQueue<>(3);
        /**
         * Поток-производитель, добавляющий элементы в очередь.
         * Добавляет числа от 0 до 4 включительно.
         */
        Thread producer = new Thread(() -> {
            IntStream.range(0, 5).forEach(value -> {
                queue.offer(value);
                System.out.println("Produced: " + value);
            });
        });

        /**
         * Поток-потребитель, извлекающий элементы из очереди.
         * Работает пока очередь не пуста или пока не получит сигнал прерывания.
         * Все извлеченные элементы сохраняются в буфер для последующей проверки.
         */
        Thread consumer = new Thread(() -> {
            while (!queue.isEmpty() || !Thread.currentThread().isInterrupted()) {
                try {
                    Integer value = queue.poll();
                    if (value != null) {
                        buffer.add(value);
                        System.out.println("Consumed: " + value);
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });

        producer.start();
        consumer.start();
        producer.join();
        consumer.interrupt();
        consumer.join();

        /**
         * Проверка результатов:
         * - что все элементы были обработаны
         * - что порядок элементов соответствует FIFO
         * - что не было пропущенных или лишних элементов
         */
        assertThat(buffer)
                .hasSize(5)
                .containsExactly(0, 1, 2, 3, 4);
    }
}