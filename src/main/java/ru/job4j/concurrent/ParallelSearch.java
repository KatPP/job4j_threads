package ru.job4j.concurrent;

public class ParallelSearch {

    /**
     * Основной метод программы, запускающий производителя и потребителя.
     * Производитель генерирует числа от 0 до 2 и помещает их в очередь.
     * Потребитель извлекает числа из очереди и выводит их в консоль.
     * После завершения работы производитель отправляет сигнал (-1),
     * чтобы потребитель мог завершить свою работу.
     *
     * @param args аргументы командной строки (не используются)
     */
    public static void main(String[] args) {

        SimpleBlockingQueue<Integer> queue = new SimpleBlockingQueue<Integer>();


        final Thread consumer = new Thread(
                () -> {
                    while (true) {
                        try {
                            Integer value = queue.poll();
                            if (value == -1) {
                                break;
                            }
                            System.out.println(value);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            Thread.currentThread().interrupt();
                            break;
                        }
                    }
                    System.out.println("Consumer stopped.");
                }
        );
        consumer.start();

        new Thread(
                () -> {
                    for (int index = 0; index != 3; index++) {
                        queue.offer(index);
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    queue.offer(-1);
                    System.out.println("Producer stopped.");
                }
        ).start();
    }
}