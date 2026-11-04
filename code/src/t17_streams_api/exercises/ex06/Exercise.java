package t17_streams_api.exercises.ex06;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class Exercise {

    public static final int PASS_MARK = 40;

    public record Student(String name, String subject, int grade) { }

    // Average grade for each subject, subjects in alphabetical order
    public static Map<String, Double> avgGradeBySubject(List<Student> students) {
        if (students == null) {
            return Map.of();
        }
        return students.stream()
                .collect(Collectors.groupingBy(Student::subject, TreeMap::new,
                        Collectors.averagingInt(Student::grade)));
    }

    // Names of students with grade >= PASS_MARK, alphabetical
    public static List<String> passedNames(List<Student> students) {
        if (students == null) {
            return List.of();
        }
        return students.stream()
                .filter(s -> s.grade() >= PASS_MARK)
                .map(Student::name)
                .sorted()
                .toList();
    }

    public static void run() {
        List<Student> students = List.of(
                new Student("Ana", "Maths", 72),
                new Student("Ben", "Maths", 38),
                new Student("Cara", "Art", 40),
                new Student("Dev", "Art", 90));
        System.out.println(avgGradeBySubject(students));
        System.out.println(passedNames(students));
    }
}
