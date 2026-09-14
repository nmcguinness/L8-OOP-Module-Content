package t04_equality_hashing.exercises.ex07;

public class Exercise {

    private static int hSum(String s) {
        int sum = 0;
        for (int i = 0; i < s.length(); i++) {
            sum += s.charAt(i);
        }
        return sum;
    }

    public static void run() {
        String s1 = "ABC";
        String s2 = "ACB";

        int h1 = hSum(s1);
        int h2 = hSum(s2);

        System.out.println(s1 + " -> " + h1);
        System.out.println(s2 + " -> " + h2);
        System.out.println("Collision? " + (h1 == h2));

        System.out.println(
            "h_sum(S) = sum of ASCII codes is a poor real-world hash because\n" +
            "many different strings collide easily (any reordering of characters\n" +
            "with the same total sum gives the same hash), so buckets fill unevenly."
        );
    }
}
