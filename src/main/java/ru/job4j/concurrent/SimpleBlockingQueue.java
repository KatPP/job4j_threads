package ru.job4j.concurrent;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

import java.util.LinkedList;
import java.util.Queue;

@ThreadSafe
public class SimpleBlockingQueue<T> {
    private final Object monitor = this;

    @GuardedBy("this")
    private final Queue<T> queue = new LinkedList<>();
    private final int capacity;

    public SimpleBlockingQueue(int capacity) {
        this.capacity = capacity;
    }

    /**
     * Добавляет элемент в очередь
     *
     * @param value элемент для добавления
     */
    public void offer(T value) {
        synchronized (monitor) {
            while (queue.size() >= capacity) {
                try {
                    monitor.wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            queue.offer(value);
            monitor.notifyAll();
        }
    }

    /**
     * Извлекает элемент из очереди
     *
     * @return извлеченный элемент
     * @throws InterruptedException если поток был прерван
     */
    public T poll() throws InterruptedException {
        synchronized (monitor) {
            while (queue.isEmpty()) {
                monitor.wait();
            }
            T value = queue.poll();
            monitor.notifyAll();
            return value;
        }
    }

    /**
     * Проверяет, пуста ли очередь
     *
     * @return true если очередь пуста
     */
    public synchronized boolean isEmpty() {
        return queue.isEmpty();
    }
}