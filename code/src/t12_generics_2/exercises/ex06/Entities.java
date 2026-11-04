package t12_generics_2.exercises.ex06;

import java.util.List;

public final class Entities {

    private Entities() {
    }

    public static void addAllEntities(List<? extends Entity> src, List<? super Entity> dst) {
        if (src == null) {
            throw new NullPointerException("src must not be null");
        }
        if (dst == null) {
            throw new NullPointerException("dst must not be null");
        }
        for (Entity entity : src) {
            dst.add(entity);
        }
    }
}
