package t11_generics_1.exercises.ex08;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * Hands out reused objects when it has some, and makes new ones when it does not.
 *
 * <p>Pooling helps when objects are expensive to create, or when creating many short-lived
 * ones would make the garbage collector pause a game mid-frame.
 */
public class ObjectPool<T> {

    private final Supplier<T> factory;
    private final List<T> pool = new ArrayList<>();
    private int created;

    public ObjectPool(Supplier<T> factory) {
        if (factory == null) {
            throw new NullPointerException("factory must not be null");
        }
        this.factory = factory;
    }

    public T acquire() {
        if (pool.isEmpty()) {
            created++;
            return factory.get();
        }
        return pool.remove(pool.size() - 1);
    }

    public void release(T object) {
        if (object == null) {
            throw new NullPointerException("object must not be null");
        }
        pool.add(object);
    }

    public int created() {
        return created;
    }

    public int available() {
        return pool.size();
    }
}
