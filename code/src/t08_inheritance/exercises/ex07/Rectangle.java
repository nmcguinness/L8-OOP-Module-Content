package t08_inheritance.exercises.ex07;

/**
 * Rectangle is non-sealed: anyone may extend it, so new kinds of rectangle
 * can be added later without editing Shape.
 */
public non-sealed class Rectangle extends Shape {
    private final double width;
    private final double height;

    public Rectangle(double width, double height) {
        if (width <= 0 || height <= 0)
            throw new IllegalArgumentException("sides must be positive");
        this.width = width;
        this.height = height;
    }

    @Override
    public double area() {
        return width * height;
    }
}
