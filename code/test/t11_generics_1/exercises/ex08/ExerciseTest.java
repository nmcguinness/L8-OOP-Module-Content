package t11_generics_1.exercises.ex08;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("t11 ex08 - ObjectPool")
class ExerciseTest {

    @Test
    void acquire_emptyPool_createsANewObject() {
        ObjectPool<StringBuilder> pool = new ObjectPool<>(StringBuilder::new);

        assertNotSame(pool.acquire(), pool.acquire());
        assertEquals(2, pool.created());
    }

    @Test
    void releaseThenAcquire_reusesTheSameObject() {
        ObjectPool<StringBuilder> pool = new ObjectPool<>(StringBuilder::new);
        StringBuilder first = pool.acquire();
        pool.release(first);

        assertSame(first, pool.acquire());
        assertEquals(1, pool.created());
    }

    @Test
    void nullFactoryOrNullRelease_throws() {
        assertThrows(NullPointerException.class, () -> new ObjectPool<String>(null));
        assertThrows(NullPointerException.class,
                () -> new ObjectPool<>(StringBuilder::new).release(null));
    }
}
