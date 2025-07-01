package ru.job4j.concurrent;

import java.io.*;
import java.util.function.Predicate;

/**
 * Класс для операций с файлами.
 * Исправлены ошибки:
 * - Разделение ответственности (вынесен FileContainer)
 * - Применен шаблон Strategy
 * - Исправлены проблемы с IO
 */
public final class FileOperations {

    /**
     * Чтение содержимого файла с фильтрацией.
     *
     * @param container контейнер с файлом
     * @param filter    предикат для фильтрации символов
     * @return отфильтрованное содержимое
     * @throws IOException при ошибках чтения
     */
    public static String content(FileContainer container,
                                 Predicate<Character> filter) throws IOException {
        StringBuilder output = new StringBuilder();
        try (BufferedInputStream input = new BufferedInputStream(
                new FileInputStream(container.getFile()))) {
            int data;
            while ((data = input.read()) != -1) {
                if (filter.test((char) data)) {
                    output.append((char) data);
                }
            }
        }
        return output.toString();
    }

    /**
     * Получение полного содержимого файла.
     *
     * @param container контейнер с файлом
     * @return содержимое файла
     * @throws IOException при ошибках чтения
     */
    public static String getContent(FileContainer container) throws IOException {
        return content(container, c -> true);
    }

    /**
     * Получение содержимого без Unicode символов.
     *
     * @param container контейнер с файлом
     * @return содержимое только с ASCII символами
     * @throws IOException при ошибках чтения
     */
    public static String getContentWithoutUnicode(FileContainer container) throws IOException {
        return content(container, c -> c < 0x80);
    }

    /**
     * Сохранение содержимого в файл.
     *
     * @param container контейнер с файлом
     * @param content   содержимое для сохранения
     * @throws IOException при ошибках записи
     */
    public static void saveContent(FileContainer container,
                                   String content) throws IOException {
        try (BufferedOutputStream output = new BufferedOutputStream(
                new FileOutputStream(container.getFile()))) {
            output.write(content.getBytes());
        }
    }
}