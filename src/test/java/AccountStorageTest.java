import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.job4j.concurrent.Account;
import ru.job4j.concurrent.AccountStorage;

import static org.assertj.core.api.Assertions.assertThat;

class AccountStorageTest {

    @Test
    @DisplayName("Добавление нового счёта")
    void whenAdd() {
        var storage = new AccountStorage();
        storage.add(new Account(1, 100));
        var firstAccount = storage.getById(1)
                .orElseThrow(() -> new IllegalStateException("Not found account by id = 1"));
        assertThat(firstAccount.amount()).isEqualTo(100);
    }

    @Test
    @DisplayName("Обновление существующего счёта")
    void whenUpdate() {
        var storage = new AccountStorage();
        storage.add(new Account(1, 100));
        storage.update(new Account(1, 200));
        var firstAccount = storage.getById(1)
                .orElseThrow(() -> new IllegalStateException("Not found account by id = 1"));
        assertThat(firstAccount.amount()).isEqualTo(200);
    }

    @Test
    @DisplayName("Удаление счёта по ID")
    void whenDelete() {
        var storage = new AccountStorage();
        storage.add(new Account(1, 100));
        storage.delete(1);
        assertThat(storage.getById(1)).isEmpty();
    }

    @Test
    @DisplayName("Успешный перевод средств между счетами")
    void whenTransfer() {
        var storage = new AccountStorage();
        storage.add(new Account(1, 100));
        storage.add(new Account(2, 100));
        storage.transfer(1, 2, 100);
        var firstAccount = storage.getById(1)
                .orElseThrow(() -> new IllegalStateException("Not found account by id = 1"));
        var secondAccount = storage.getById(2)
                .orElseThrow(() -> new IllegalStateException("Not found account by id = 1"));
        assertThat(firstAccount.amount()).isEqualTo(0);
        assertThat(secondAccount.amount()).isEqualTo(200);
    }

    @Test
    @DisplayName("Попытка перевода при недостаточных средствах")
    void whenTransferWithInsufficientFunds() {
        var storage = new AccountStorage();
        storage.add(new Account(1, 50));
        storage.add(new Account(2, 100));
        boolean result = storage.transfer(1, 2, 100);
        assertThat(result).isFalse();
        assertThat(storage.getById(1).get().amount()).isEqualTo(50);
        assertThat(storage.getById(2).get().amount()).isEqualTo(100);
    }

    @Test
    @DisplayName("Попытка перевода на несуществующий счёт")
    void whenTransferToNonExistentAccount() {
        var storage = new AccountStorage();
        storage.add(new Account(1, 100));
        boolean result = storage.transfer(1, 2, 50);
        assertThat(result).isFalse();
        assertThat(storage.getById(1).get().amount()).isEqualTo(100);
    }

    @Test
    @DisplayName("Попытка перевода с несуществующего счёта")
    void whenTransferFromNonExistentAccount() {
        var storage = new AccountStorage();
        storage.add(new Account(2, 100));
        boolean result = storage.transfer(1, 2, 50);
        assertThat(result).isFalse();
        assertThat(storage.getById(2).get().amount()).isEqualTo(100);
    }
}
