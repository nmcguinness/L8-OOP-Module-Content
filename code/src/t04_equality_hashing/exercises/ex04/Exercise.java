package t04_equality_hashing.exercises.ex04;

public class Exercise {

    public static void run() {
        int hashId = 20123;
        int hashEmail = 881234;

        int h = 31 * hashId + hashEmail;

        System.out.println("Combined hash = " + h);

        System.out.println(
            "The same fields used in equals(...) must also be used in hashCode();\n" +
            "otherwise two objects that are \"equal\" might have different hash codes,\n" +
            "which breaks hashed collections like HashMap or HashSet.\n" +
            "If those identifying fields are immutable, then the hash code cannot change\n" +
            "after insertion, which keeps lookups stable and predictable."
        );
    }
}
