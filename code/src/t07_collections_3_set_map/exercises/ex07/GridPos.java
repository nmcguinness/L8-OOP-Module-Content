package t07_collections_3_set_map.exercises.ex07;

import java.util.Objects;

/**
 * An immutable grid position. The starter version on the exercise sheet overrides
 * equals but not hashCode; this is the fixed version.
 */
public final class GridPos {

    private final int row;
    private final int col;

    public GridPos(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public int row() {
        return row;
    }

    public int col() {
        return col;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GridPos other)) return false;
        return row == other.row && col == other.col;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, col);
    }

    @Override
    public String toString() {
        return "(" + row + "," + col + ")";
    }
}
