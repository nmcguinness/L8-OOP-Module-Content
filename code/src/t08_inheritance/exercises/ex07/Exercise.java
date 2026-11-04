package t08_inheritance.exercises.ex07;

import java.util.List;

public class Exercise {

    // Polymorphism: no type checks needed, every Shape has area()
    public static double totalArea(List<Shape> shapes) {
        double total = 0;
        for (Shape s : shapes) {
            total += s.area();
        }
        return total;
    }

    // Subtype-only feature: only circles have a radius
    public static double largestRadius(List<Shape> shapes) {
        double largest = 0;
        for (Shape s : shapes) {
            if (s instanceof Circle c && c.radius() > largest) {
                largest = c.radius();
            }
        }
        return largest;
    }

    public static void run() {
        List<Shape> shapes = List.of(new Circle(1), new Square(2), new Circle(3),
                new Rectangle(2, 5));
        System.out.printf("Total area: %.2f%n", totalArea(shapes));
        System.out.println("Largest radius: " + largestRadius(shapes));
    }
}
