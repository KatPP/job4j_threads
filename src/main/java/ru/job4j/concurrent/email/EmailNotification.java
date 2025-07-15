package ru.job4j.concurrent.email;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Сервис для асинхронной рассылки email-уведомлений пользователям.
 * Использует пул потоков для выполнения задач отправки.
 */
public class EmailNotification {
    /**
     * Пул потоков для выполнения задач отправки email
     */
    private final ExecutorService pool;

    /**
     * Конструктор создает фиксированный пул потоков,
     * размер которого равен количеству доступных процессоров.
     */
    public EmailNotification() {
        this.pool = Executors.newFixedThreadPool(
                Runtime.getRuntime().availableProcessors()
        );
    }

    /**
     * Формирует и отправляет email-уведомление пользователю.
     * Задача отправки выполняется асинхронно через пул потоков.
     *
     * @param user пользователь, которому отправляется уведомление
     */
    public void emailTo(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }

        pool.submit(() -> {
            String subject = String.format("Notification %s to email %s",
                    user.getUsername(), user.getEmail());
            String body = String.format("Add a new event to %s",
                    user.getUsername());

            send(subject, body, user.getEmail());
        });
    }

    /**
     * Метод для отправки email. В текущей реализации не выполняет действий.
     *
     * @param subject тема письма
     * @param body    содержимое письма
     * @param email   адрес получателя
     */
    public void send(String subject, String body, String email) {
    }

    /**
     * Завершает работу пула потоков.
     * Пул перестает принимать новые задачи и завершается после выполнения всех текущих задач.
     */
    public void close() {
        pool.shutdown();
        while (!pool.isTerminated()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("Pool shutdown was interrupted: " + e.getMessage());
            }
        }
    }
}

/**
 * Модель пользователя для системы уведомлений.
 */
class User {
    private final String username;
    private final String email;

    /**
     * Создает нового пользователя.
     *
     * @param username имя пользователя
     * @param email    email пользователя
     */
    public User(String username, String email) {
        this.username = username;
        this.email = email;
    }

    /**
     * @return имя пользователя
     */
    public String getUsername() {
        return username;
    }

    /**
     * @return email пользователя
     */
    public String getEmail() {
        return email;
    }
}