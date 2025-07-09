import org.junit.jupiter.api.Test;
import ru.job4j.concurrent.cash.Base;
import ru.job4j.concurrent.cash.Cache;
import ru.job4j.concurrent.cash.OptimisticException;

import static org.assertj.core.api.Assertions.assertThat;

class CacheTest {
    @Test
    public void whenAddFind() throws OptimisticException {
        var base = new Base(1, "Base", 1);
        var cache = new Cache();
        cache.add(base);
        var find = cache.findById(base.id());
        assertThat(find.get().name())
                .isEqualTo("Base");
    }

    @Test
    public void whenAddUpdateFind() throws OptimisticException {
        var base = new Base(1, "Base", 1);
        var cache = new Cache();
        cache.add(base);
        cache.update(new Base(1, "Base updated", 1));
        var find = cache.findById(base.id());
        assertThat(find.get().name())
                .isEqualTo("Base updated");
        assertThat(find.get().version())
                .isEqualTo(2);
    }

    @Test
    public void whenAddDeleteFind() throws OptimisticException {
        var base = new Base(1, "Base", 1);
        var cache = new Cache();
        cache.add(base);
        cache.delete(1);
        var find = cache.findById(base.id());
        assertThat(find.isEmpty()).isTrue();
    }

    @Test
    public void whenAddSameElementTwice() throws OptimisticException {
        var base = new Base(1, "Base", 1);
        var cache = new Cache();
        assertThat(cache.add(base)).isTrue();
        assertThat(cache.add(base)).isFalse();
    }

    @Test
    public void whenUpdateNonExistingElement() throws OptimisticException {
        var cache = new Cache();
        assertThat(cache.update(new Base(1, "Base", 1))).isFalse();
    }
}
