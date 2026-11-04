package t08_inheritance.exercises.ex07;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("t08 ex07 - sealed shapes and instanceof pattern matching")
class ExerciseTest {

    @Test
    void totalArea_mixedShapes_sumsEveryArea() {
        List<Shape> shapes = List.of(new Square(2), new Rectangle(2, 5), new Circle(1));

        assertEquals(4 + 10 + Math.PI, Exercise.totalArea(shapes), 1e-9);
    }

    @Test
    void largestRadius_ignoresNonCircles() {
        List<Shape> shapes = List.of(new Circle(1), new Square(50), new Circle(3));

        assertEquals(3, Exercise.largestRadius(shapes), 1e-9);
    }

    @Test
    void largestRadius_noCircles_returnsZero() {
        assertEquals(0, Exercise.largestRadius(List.of(new Square(1))), 1e-9);
    }

    @Test
    void shape_isSealed_withExactlyThreePermittedSubclasses() {
        assertTrue(Shape.class.isSealed());
        assertArrayEquals(new Class<?>[]{Circle.class, Square.class, Rectangle.class},
                Shape.class.getPermittedSubclasses());
    }

    @Test
    void circle_nonPositiveRadius_throws() {
        assertThrows(IllegalArgumentException.class, () -> new Circle(0));
    }
}
