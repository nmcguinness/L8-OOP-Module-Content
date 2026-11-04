package t11_generics_1.exercises.ex06;

public class Exercise {

    public static void run() {
        Registry<String, Integer> scores = new Registry<>();
        scores.put("Zara", 100);
        scores.put("Kai", 55);
        System.out.println("Has Zara: " + scores.containsKey("Zara"));
        System.out.println("Kai: " + scores.get("Kai"));
        System.out.println("Nope: " + scores.get("Nope"));

        Registry<Integer, String> names = new Registry<>();
        names.put(7, "Ana");
        System.out.println("Id 7: " + names.get(7));

        try {
            scores.put(null, 1);
        } catch (NullPointerException e) {
            System.out.println("Null key rejected: " + e.getMessage());
        }
    }
}
