package ru.job4j.concurrent;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

/**
 * Класс для параллельного поиска элемента в массиве с использованием ForkJoinPool.
 * Если размер подмассива меньше или равен 10, используется линейный поиск.
 *
 * @param <T> тип элементов в массиве
 */
public class ParallelSearchNew<T> extends RecursiveTask<Integer> {
    /**
     * Массив, в котором выполняется поиск
     */
    private final T[] array;
    /**
     * Искомый элемент
     */
    private final T target;
    /**
     * Начальный индекс поиска
     */
    private final int from;
    /**
     * Конечный индекс поиска
     */
    private final int to;
    /**
     * Порог для переключения на линейный поиск
     */
    private static final int THRESHOLD = 10;

    /**
     * Конструктор для создания задачи поиска.
     *
     * @param array  массив для поиска
     * @param target искомый элемент
     * @param from   начальный индекс (включительно)
     * @param to     конечный индекс (включительно)
     */
    public ParallelSearchNew(T[] array, T target, int from, int to) {
        this.array = array;
        this.target = target;
        this.from = from;
        this.to = to;
    }

    /**
     * Основной метод вычисления результата.
     * Рекурсивно делит задачу на подзадачи, пока размер подмассива не станет меньше порога.
     *
     * @return индекс найденного элемента или -1, если элемент не найден
     */
    @Override
    protected Integer compute() {
        if (to - from <= THRESHOLD) {
            return linearSearch();
        }

        int mid = (from + to) / 2;
        ParallelSearchNew<T> leftSearch = new ParallelSearchNew<>(array, target, from, mid);
        ParallelSearchNew<T> rightSearch = new ParallelSearchNew<>(array, target, mid + 1, to);

        leftSearch.fork();
        Integer rightResult = rightSearch.compute();
        Integer leftResult = leftSearch.join();

        return leftResult != -1 ? leftResult : rightResult;
    }

    /**
     * Линейный поиск элемента в подмассиве.
     *
     * @return индекс найденного элемента или -1, если элемент не найден
     */
    private Integer linearSearch() {
        for (int i = from; i <= to; i++) {
            if (target.equals(array[i])) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Статический метод для запуска параллельного поиска.
     *
     * @param <T>    тип элементов массива
     * @param array  массив для поиска
     * @param target искомый элемент
     * @return индекс найденного элемента или -1, если элемент не найден или массив пустой/нулевой
     */
    public static <T> int search(T[] array, T target) {
        if (array == null || array.length == 0) {
            return -1;
        }
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        return forkJoinPool.invoke(new ParallelSearchNew<>(array, target, 0, array.length - 1));
    }
}