package ru.job4j.concurrent;

import java.util.HashMap;
import java.util.Optional;

/**
 * Потокобезопасное хранилище банковских счетов.
 * Обеспечивает атомарные операции добавления, обновления, удаления счетов,
 * а также перевод средств между счетами.
 * Все операции защищены синхронизацией для работы в многопоточной среде.
 */
public class AccountStorage {
    private final HashMap<Integer, Account> accounts = new HashMap<>();

    /**
     * Добавляет новый счет в хранилище.
     * Если счет с таким ID уже существует, добавление не происходит.
     *
     * @param account добавляемый счет
     * @return true если счет был успешно добавлен, false если счет с таким ID уже существует
     */
    public synchronized boolean add(Account account) {
        return accounts.putIfAbsent(account.id(), account) == null;
    }

    /**
     * Обновляет существующий счет в хранилище.
     *
     * @param account обновляемый счет
     * @return true если счет был успешно обновлен, false если счет с таким ID не найден
     */
    public synchronized boolean update(Account account) {
        return accounts.replace(account.id(), account) != null;
    }

    /**
     * Удаляет счет из хранилища по указанному ID.
     *
     * @param id идентификатор удаляемого счета
     */
    public synchronized void delete(int id) {
        accounts.remove(id);
    }

    /**
     * Возвращает счет по указанному ID.
     *
     * @param id идентификатор запрашиваемого счета
     * @return {@link Optional} содержащий найденный счет или пустой, если счет не найден
     */
    public synchronized Optional<Account> getById(int id) {
        return Optional.ofNullable(accounts.get(id));
    }

    /**
     * Переводит указанную сумму денег с одного счета на другой.
     * Операция атомарна - либо выполняется полностью, либо не выполняется вообще.
     * Перед переводом проверяет:
     * 1. Существование обоих счетов
     * 2. Достаточность средств на счете отправителя
     *
     * @param fromId ID счета отправителя
     * @param toId   ID счета получателя
     * @param amount переводимая сумма
     * @return true если перевод выполнен успешно, false если перевод невозможен
     */
    public synchronized boolean transfer(int fromId, int toId, int amount) {
        boolean result = false;
        Account fromAccount = accounts.get(fromId);
        Account toAccount = accounts.get(toId);

        if (fromAccount != null && toAccount != null && fromAccount.amount() >= amount) {
            update(new Account(fromId, fromAccount.amount() - amount));
            update(new Account(toId, toAccount.amount() + amount));
            result = true;
        }
        return result;
    }
}
