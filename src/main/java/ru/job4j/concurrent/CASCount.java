package ru.job4j.concurrent;

import net.jcip.annotations.ThreadSafe;

import java.util.concurrent.atomic.AtomicInteger;

@ThreadSafe
public class CASCount {
    private final AtomicInteger count = new AtomicInteger(0);

    /**
     * Увеличивает значение счетчика на 1.
     * Использует CAS операцию для атомарного обновления значения.
     */
    public void increment() {
        int current;
        int next;
        do {
            current = count.get();
            next = current + 1;
        } while (!count.compareAndSet(current, next));
    }

    /**
     * Возвращает текущее значение счетчика.
     * @return текущее значение счетчика
     */
    public int get() {
        return count.get();
    }
}