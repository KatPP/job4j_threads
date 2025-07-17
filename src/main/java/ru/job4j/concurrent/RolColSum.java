package ru.job4j.concurrent;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * Класс для вычисления сумм элементов по строкам и столбцам матрицы.
 * Предоставляет как последовательную, так и асинхронную реализацию.
 */
public class RolColSum {
    /**
     * Класс для хранения сумм элементов строки и столбца матрицы.
     */
    public static class Sums {
        private int rowSum;
        private int colSum;

        /**
         * Конструктор.
         *
         * @param rowSum сумма элементов строки
         * @param colSum сумма элементов столбца
         */
        public Sums(int rowSum, int colSum) {
            this.rowSum = rowSum;
            this.colSum = colSum;
        }

        public int getRowSum() {
            return rowSum;
        }

        public void setRowSum(int rowSum) {
            this.rowSum = rowSum;
        }

        public int getColSum() {
            return colSum;
        }

        public void setColSum(int colSum) {
            this.colSum = colSum;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Sums sums = (Sums) o;
            return rowSum == sums.rowSum && colSum == sums.colSum;
        }

        @Override
        public int hashCode() {
            int result = rowSum;
            result = 31 * result + colSum;
            return result;
        }
    }

    /**
     * Последовательное вычисление сумм элементов по строкам и столбцам матрицы.
     *
     * @param matrix входная квадратная матрица
     * @return массив объектов Sums, где каждый элемент содержит суммы для соответствующей строки и столбца
     */
    public static Sums[] sum(int[][] matrix) {
        int n = matrix.length;
        Sums[] sums = new Sums[n];

        for (int i = 0; i < n; i++) {
            int rowSum = 0;
            int colSum = 0;

            for (int j = 0; j < n; j++) {
                rowSum += matrix[i][j];
                colSum += matrix[j][i];
            }

            sums[i] = new Sums(rowSum, colSum);
        }

        return sums;
    }

    /**
     * Асинхронное вычисление сумм элементов по строкам и столбцам матрицы.
     *
     * @param matrix входная квадратная матрица
     * @return массив объектов Sums, где каждый элемент содержит суммы для соответствующей строки и столбца
     * @throws ExecutionException   при ошибке выполнения асинхронной задачи
     * @throws InterruptedException при прерывании выполнения
     */
    public static Sums[] asyncSum(int[][] matrix) throws ExecutionException, InterruptedException {
        int n = matrix.length;
        Sums[] sums = new Sums[n];
        CompletableFuture<Sums>[] futures = new CompletableFuture[n];

        for (int i = 0; i < n; i++) {
            futures[i] = getTask(matrix, i);
        }

        for (int i = 0; i < n; i++) {
            sums[i] = futures[i].get();
        }

        return sums;
    }

    /**
     * Создает асинхронную задачу для вычисления сумм строки и столбца по указанному индексу.
     *
     * @param matrix входная матрица
     * @param index  строки/столбца
     * @return CompletableFuture с задачей для асинхронного выполнения
     */
    private static CompletableFuture<Sums> getTask(int[][] matrix, int index) {
        return CompletableFuture.supplyAsync(() -> {
            int rowSum = 0;
            int colSum = 0;
            int n = matrix.length;

            for (int j = 0; j < n; j++) {
                rowSum += matrix[index][j];
                colSum += matrix[j][index];
            }

            return new Sums(rowSum, colSum);
        });
    }
}
