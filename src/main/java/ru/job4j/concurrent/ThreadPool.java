package ru.job4j.concurrent;

import java.util.LinkedList;
import java.util.List;

/**
 * Реализация пула потоков фиксированного размера.
 * Пул создает количество потоков, равное количеству ядер процессора.
 * Задачи добавляются в очередь и выполняются доступными потоками.
 */
public class ThreadPool {
    /**
     * Список рабочих потоков
     */
    private final List<Thread> threads = new LinkedList<>();
    /**
     * Очередь задач для выполнения
     */
    private final SimpleBlockingQueue<Runnable> tasks;

    /**
     * Конструктор пула потоков.
     * Инициализирует пул с количеством потоков, равным количеству ядер процессора.
     * Каждый поток ожидает задачи из очереди и выполняет их.
     */
    public ThreadPool() {
        int size = Runtime.getRuntime().availableProcessors();
        this.tasks = new SimpleBlockingQueue<>(size);

        for (int i = 0; i < size; i++) {
            Thread thread = new Thread(() -> {
                while (!Thread.currentThread().isInterrupted()) {
                    try {
                        Runnable task = tasks.poll();
                        task.run();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            });
            threads.add(thread);
            thread.start();
        }
    }

    /**
     * Добавляет задачу в очередь на выполнение.
     *
     * @param job задача для выполнения
     * @throws InterruptedException если поток был прерван во время добавления задачи
     */
    public void work(Runnable job) throws InterruptedException {
        if (Thread.currentThread().isInterrupted()) {
            throw new InterruptedException("Thread was interrupted");
        }
        tasks.offer(job);
    }

    /**
     * Завершает работу пула потоков.
     * Прерывает все рабочие потоки, позволяя им завершить выполнение.
     * Текущие выполняемые задачи будут завершены, но новые задачи приниматься не будут.
     */
    public void shutdown() {
        for (Thread thread : threads) {
            thread.interrupt();
        }
    }
}
