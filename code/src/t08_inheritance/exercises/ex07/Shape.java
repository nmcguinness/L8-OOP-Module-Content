package t08_inheritance.exercises.ex07;

/**
 * A closed hierarchy: only Circle, Square and Rectangle may extend Shape.
 */
public sealed abstract class Shape permits Circle, Square, Rectangle {

    public abstract double area();
}
