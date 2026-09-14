package t04_equality_hashing.exercises.ex03;

public class Exercise {

    public static void run() {
        int h0 = 0;
        int h1 = 31 * h0 + 67; // C
        int h2 = 31 * h1 + 65; // A
        int h3 = 31 * h2 + 66; // B

        System.out.println("h0 = " + h0);
        System.out.println("h1 = " + h1);
        System.out.println("h2 = " + h2);
        System.out.println("h3 = " + h3 + "  // final hash for \"CAB\"");
    }
}
