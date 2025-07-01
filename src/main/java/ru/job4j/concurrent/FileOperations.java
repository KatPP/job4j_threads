package ru.job4j.concurrent;

import java.io.*;
import java.util.function.Predicate;

// Исправил ошибки + вынес отдельно класс FileContainer
public final class FileOperations {

    // Было: Дублирование кода в getContent() и getContentWithoutUnicode()
    // Исправил: Общий метод content() с фильтром (Strategy pattern)
    public static String content(FileContainer container,
                                 Predicate<Character> filter) throws IOException {
        StringBuilder output = new StringBuilder();  // Было: String += (неэффективно)
        try (BufferedInputStream input = new BufferedInputStream(  // Было: FileInputStream без буфера
                new FileInputStream(container.getFile()))) {          // Было: Ресурс не закрывался
            int data;
            while ((data = input.read()) != -1) {  // Было: > 0 (думаю что неверное условие конца файла поэтому исправил)
                if (filter.test((char) data)) {
                    output.append((char) data);  // Оптимизированное добавление символов
                }
            }
        }  // Автоматическое закрытие ресурса (try-with-resources)
        return output.toString();
    }

    // Было: Дублирование кода (копипаста)
    // Исправил: Переиспользование content() с фильтром "все символы"
    public static String getContent(FileContainer container) throws IOException {
        return content(container, c -> true);
    }

    // Было: Дублирование кода + неправильное условие Unicode (0x80 вместо <= 0x7F)
    // Стало: Переиспользование content() с ASCII-фильтром
    public static String getContentWithoutUnicode(FileContainer container) throws IOException {
        return content(container, c -> c < 0x80);  // ASCII-символы (0-127)
    }

    // Было: Посимвольная запись без буфера + и так же ресурс не закрывался
    // Исправил: Буферизация + массовая запись + try-with-resources
    public static void saveContent(FileContainer container,
                                   String content) throws IOException {
        try (BufferedOutputStream output = new BufferedOutputStream(  // Буферизация
                new FileOutputStream(container.getFile()))) {
            output.write(content.getBytes());  // Запись всего содержимого сразу
        }  // Автоматическое закрытие
    }
}
