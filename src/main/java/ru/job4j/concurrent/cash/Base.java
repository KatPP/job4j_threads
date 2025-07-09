package ru.job4j.concurrent.cash;

/**
 * Модель данных для хранения в кеше.
 * Содержит идентификатор, имя и версию объекта.
 * Версия используется для контроля целостности данных при обновлении.
 *
 * @param id      уникальный идентификатор объекта
 * @param name    имя объекта
 * @param version текущая версия объекта
 */
public record Base(int id, String name, int version) {
}
