package t08_inheritance.exercises.ex07;

public final class Square extends Shape {
    private final double side;

    public Square(double side) {
        if (side <= 0)
            throw new IllegalArgumentException("side must be positive");
        this.side = side;
    }

    @Override
    public double area() {
        return side * side;
    }
}
