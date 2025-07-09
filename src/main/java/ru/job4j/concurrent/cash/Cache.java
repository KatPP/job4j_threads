package ru.job4j.concurrent.cash;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

/**
 * Потокобезопасный кеш для хранения объектов типа {@link Base}.
 * Использует {@link ConcurrentHashMap} для хранения данных в памяти.
 * Обеспечивает атомарные операции добавления, обновления и удаления объектов.
 * При обновлении проверяет версию объекта для предотвращения конфликтов.
 */
public class Cache {
    /**
     * Потокобезопасное хранилище объектов, где ключ - ID объекта
     */
    private final Map<Integer, Base> memory = new ConcurrentHashMap<>();

    /**
     * Добавляет объект в кеш, если объект с таким ID еще не существует.
     *
     * @param model объект для добавления
     * @return true если объект успешно добавлен, false если объект с таким ID уже существует
     */
    public boolean add(Base model) {
        return memory.putIfAbsent(model.id(), model) == null;
    }

    /**
     * Обновляет объект в кеше, проверяя версию объекта.
     * Если версия объекта не совпадает с версией в кеше, выбрасывает {@link OptimisticException}.
     * При успешном обновлении увеличивает версию объекта на 1.
     *
     * @param model объект с новыми данными для обновления
     * @return true если объект успешно обновлен, false если объект с таким ID не найден
     * @throws OptimisticException если версии объекта не совпадают
     */
    public boolean update(Base model) throws OptimisticException {
        return memory.computeIfPresent(model.id(), (key, stored) -> {
            if (stored.version() != model.version()) {
                try {
                    throw new OptimisticException("Versions are not equal");
                } catch (OptimisticException e) {
                    throw new RuntimeException(e);
                }
            }
            return new Base(model.id(), model.name(), model.version() + 1);
        }) != null;
    }

    /**
     * Удаляет объект из кеша по его ID.
     *
     * @param id ID объекта для удаления
     */
    public void delete(int id) {
        memory.remove(id);
    }

    /**
     * Находит объект в кеше по его ID.
     *
     * @param id ID объекта для поиска
     * @return {@link Optional} содержащий найденный объект или пустой, если объект не найден
     */
    public Optional<Base> findById(int id) {
        return Stream.of(memory.get(id))
                .filter(Objects::nonNull)
                .findFirst();
    }
}
