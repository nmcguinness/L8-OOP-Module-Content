package t04_equality_hashing.exercises.ex01;

public class Exercise {

    public static void run() {
        String a = new String("hello");
        String b = new String("hello");

        System.out.println(a == b);        // expect false
        System.out.println(a.equals(b));   // expect true

        System.out.println(
            "Identity (==) checks if two variables refer to the exact same object; " +
            "equals(...) checks if two objects have the same value/content."
        );
    }
}
