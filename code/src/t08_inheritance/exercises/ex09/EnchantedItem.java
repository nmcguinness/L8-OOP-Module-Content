package t08_inheritance.exercises.ex09;

public class EnchantedItem extends Item {
    private final int level;

    public EnchantedItem(String id, int level) {
        super(id);
        this.level = level;
    }

    public int level() {
        return level;
    }

    @Override
    public boolean equals(Object o) {
        // super.equals has already checked that o has exactly this class
        if (!super.equals(o)) return false;
        return level == ((EnchantedItem) o).level;
    }

    @Override
    public int hashCode() {
        return 31 * super.hashCode() + level;
    }

    @Override
    public String toString() {
        return super.toString() + "+" + level;
    }
}
