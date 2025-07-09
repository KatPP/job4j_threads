package ru.job4j.concurrent.cash;

/**
 * Исключение, выбрасываемое при попытке обновления объекта с устаревшей версией.
 */
public class OptimisticException extends Exception {
    /**
     * Создает новое исключение с указанным сообщением.
     *
     * @param message сообщение об ошибке
     */
    public OptimisticException(String message) {
        super(message);
    }
}
