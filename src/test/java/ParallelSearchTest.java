import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.job4j.concurrent.ParallelSearchNew;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Тестирование параллельного поиска в массиве")
public class ParallelSearchTest {

    @Test
    @DisplayName("Поиск в массиве целых чисел")
    public void testSearchWithIntegerArray() {
        Integer[] array = {1, 3, 5, 7, 9, 2, 4, 6, 8, 10, 11, 13, 15, 12, 14};
        int index = ParallelSearchNew.search(array, 8);
        assertEquals(8, index);
    }

    @Test
    @DisplayName("Поиск в массиве строк")
    public void testSearchWithStringArray() {
        String[] array = {"apple", "banana", "orange", "grape", "melon"};
        int index = ParallelSearchNew.search(array, "orange");
        assertEquals(2, index);
    }

    @Test
    @DisplayName("Поиск в маленьком массиве (должен использовать линейный поиск)")
    public void testSearchWithSmallArrayUsesLinearSearch() {
        Integer[] array = {5, 3, 7, 1, 9};
        int index = ParallelSearchNew.search(array, 7);
        assertEquals(2, index);
    }

    @Test
    @DisplayName("Поиск в большом массиве (должен использовать параллельный поиск)")
    public void testSearchWithLargeArrayUsesParallelSearch() {
        Integer[] array = new Integer[100];
        for (int i = 0; i < array.length; i++) {
            array[i] = i;
        }
        int index = ParallelSearchNew.search(array, 75);
        assertEquals(75, index);
    }

    @Test
    @DisplayName("Поиск отсутствующего элемента")
    public void testSearchWhenElementNotFound() {
        Integer[] array = {1, 2, 3, 4, 5};
        int index = ParallelSearchNew.search(array, 6);
        assertEquals(-1, index);
    }

    @Test
    @DisplayName("Поиск в массиве пользовательских объектов")
    public void testSearchWithCustomObjects() {
        class Person {
            String name;
            int age;

            Person(String name, int age) {
                this.name = name;
                this.age = age;
            }

            @Override
            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                if (obj == null || getClass() != obj.getClass()) {
                    return false;
                }
                Person person = (Person) obj;
                return age == person.age && name.equals(person.name);
            }

            @Override
            public int hashCode() {
                int result = name != null ? name.hashCode() : 0;
                result = 31 * result + age;
                return result;
            }
        }

        Person[] people = {
                new Person("Alice", 25),
                new Person("Bob", 30),
                new Person("Charlie", 35)
        };

        int index = ParallelSearchNew.search(people, new Person("Bob", 30));
        assertEquals(1, index);
    }

    @Test
    @DisplayName("Поиск в пустом массиве")
    public void testSearchWithEmptyArray() {
        Integer[] array = {};
        int index = ParallelSearchNew.search(array, 1);
        assertEquals(-1, index);
    }

    @Test
    @DisplayName("Поиск при null массиве")
    public void testSearchWithNullArray() {
        int index = ParallelSearchNew.search(null, 1);
        assertEquals(-1, index);
    }
}