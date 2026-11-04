package t08_inheritance.exercises.ex09;

/**
 * Uses getClass() in equals so that equality stays symmetric when a subclass
 * adds a field that takes part in equality.
 */
public class Item {
    private final String id;

    public Item(String id) {
        if (id == null || id.isBlank())
            throw new IllegalArgumentException("id is required");
        this.id = id;
    }

    public String id() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item other = (Item) o;
        return id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[" + id + "]";
    }
}
