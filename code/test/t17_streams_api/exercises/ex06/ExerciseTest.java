package t17_streams_api.exercises.ex06;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import t17_streams_api.exercises.ex06.Exercise.Student;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("t17 ex06 - a record pipeline")
class ExerciseTest {

    private final List<Student> students = List.of(
            new Student("Ana", "Maths", 72),
            new Student("Ben", "Maths", 38),
            new Student("Cara", "Art", 40),
            new Student("Dev", "Art", 90));

    @Test
    void avgGradeBySubject_example() {
        assertEquals(Map.of("Art", 65.0, "Maths", 55.0), Exercise.avgGradeBySubject(students));
    }

    @Test
    void passedNames_includesExactlyThePassMark() {
        assertEquals(List.of("Ana", "Cara", "Dev"), Exercise.passedNames(students));
    }

    @Test
    void emptyOrNull_givesEmptyResults() {
        assertTrue(Exercise.avgGradeBySubject(List.of()).isEmpty());
        assertTrue(Exercise.passedNames(null).isEmpty());
    }
}
