package ru.job4j.concurrent;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

import java.util.concurrent.ExecutionException;

@DisplayName("Тесты для вычисления сумм строк и столбцов матрицы")
class RolColSumTest {

    @Test
    @DisplayName("Последовательное вычисление для матрицы 3x3")
    void whenSequentialSum() {
        int[][] matrix = {
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 9}
        };
        RolColSum.Sums[] expected = {
                new RolColSum.Sums(6, 12),
                new RolColSum.Sums(15, 15),
                new RolColSum.Sums(24, 18)
        };
        RolColSum.Sums[] result = RolColSum.sum(matrix);
        assertThat(result).isEqualTo(expected);
    }

    @Test
    @DisplayName("Асинхронное вычисление для матрицы 3x3")
    void whenAsyncSum() throws ExecutionException, InterruptedException {
        int[][] matrix = {
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 9}
        };
        RolColSum.Sums[] expected = {
                new RolColSum.Sums(6, 12),
                new RolColSum.Sums(15, 15),
                new RolColSum.Sums(24, 18)
        };
        RolColSum.Sums[] result = RolColSum.asyncSum(matrix);
        assertThat(result).isEqualTo(expected);
    }

    @Test
    @DisplayName("Последовательное вычисление для пустой матрицы")
    void whenEmptyMatrix() {
        int[][] matrix = {};
        RolColSum.Sums[] expected = {};
        RolColSum.Sums[] result = RolColSum.sum(matrix);
        assertThat(result).isEqualTo(expected);
    }

    @Test
    @DisplayName("Асинхронное вычисление для пустой матрицы")
    void whenAsyncEmptyMatrix() throws ExecutionException, InterruptedException {
        int[][] matrix = {};
        RolColSum.Sums[] expected = {};
        RolColSum.Sums[] result = RolColSum.asyncSum(matrix);
        assertThat(result).isEqualTo(expected);
    }

    @Test
    @DisplayName("Последовательное вычисление для матрицы 1x1")
    void whenSingleElementMatrix() {
        int[][] matrix = {{5}};
        RolColSum.Sums[] expected = {new RolColSum.Sums(5, 5)};
        RolColSum.Sums[] result = RolColSum.sum(matrix);
        assertThat(result).isEqualTo(expected);
    }

    @Test
    @DisplayName("Асинхронное вычисление для матрицы 1x1")
    void whenAsyncSingleElementMatrix() throws ExecutionException, InterruptedException {
        int[][] matrix = {{5}};
        RolColSum.Sums[] expected = {new RolColSum.Sums(5, 5)};
        RolColSum.Sums[] result = RolColSum.asyncSum(matrix);
        assertThat(result).isEqualTo(expected);
    }
}