package t11_generics_1.exercises.ex05;

public class Exercise {

    public static void run() {
        TopN<Integer> best = new TopN<>(3);
        for (int score : new int[] {10, 7, 25, 3, 19}) {
            best.add(score);
            System.out.println("Added " + score + ": " + best.valuesDescending());
        }

        TopN<String> lastNames = new TopN<>(2);
        lastNames.add("pear");
        lastNames.add("apple");
        lastNames.add("zucchini");
        System.out.println("Strings: " + lastNames.valuesDescending());
    }
}
