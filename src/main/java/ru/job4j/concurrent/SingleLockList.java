package ru.job4j.concurrent;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Потокобезопасная реализация списка с синхронизированным доступом.
 * Гарантирует безопасность при работе в многопоточной среде.
 * @param <T> тип элементов в списке
 */
@ThreadSafe
public class SingleLockList<T> implements Iterable<T> {
    /**
     * Внутренний список для хранения элементов.
     * Доступ синхронизирован через монитор текущего объекта.
     */
    @GuardedBy("this")
    private final List<T> list;

    /**
     * Создает новый потокобезопасный список на основе переданной коллекции.
     * Выполняет глубокое копирование исходной коллекции.
     * @param list исходная коллекция элементов
     * @throws NullPointerException если переданная коллекция равна null
     */
    public SingleLockList(List<T> list) {
        if (list == null) {
            throw new NullPointerException("Source list cannot be null");
        }
        this.list = copy(list);
    }

    /**
     * Добавляет элемент в список.
     * @param value элемент для добавления
     */
    public synchronized void add(T value) {
        list.add(value);
    }

    /**
     * Возвращает элемент по указанному индексу.
     * @param index индекс элемента
     * @return элемент списка
     * @throws IndexOutOfBoundsException если индекс выходит за границы списка
     */
    public synchronized T get(int index) {
        return list.get(index);
    }

    /**
     * Возвращает итератор по копии списка (snapshot).
     * Итератор работает в режиме fail-safe - изменения в исходном списке
     * не влияют на уже полученный итератор.
     * @return итератор по элементам списка
     */
    @Override
    public synchronized Iterator<T> iterator() {
        return copy(list).iterator();
    }

    /**
     * Создает копию переданной коллекции
     * @param origin исходная коллекция
     * @return новая коллекция с элементами из origin
     */
    private List<T> copy(List<T> origin) {
        return new ArrayList<>(origin);
    }
}
