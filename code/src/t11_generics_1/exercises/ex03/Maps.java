package t11_generics_1.exercises.ex03;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public final class Maps {

    private Maps() {
    }

    // Applies transform to each item in order; T is the input type, R the output type
    public static <T, R> ArrayList<R> map(List<T> items, Function<T, R> transform) {
        if (transform == null) {
            throw new NullPointerException("transform must not be null");
        }
        ArrayList<R> out = new ArrayList<>();
        if (items == null) {
            return out;
        }
        for (T item : items) {
            out.add(transform.apply(item));
        }
        return out;
    }
}
