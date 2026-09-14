package t11_generics_1.demos.de03;

public class Demo {
    public static void run()
    {
        new Demo().start();
    }

    public void start()
    {
        java.util.ArrayList<String> names = new java.util.ArrayList<>();
        names.add("Zara");
        names.add("Kai");
        // names.add(123); // does not compile

        for (String n : names) {
            System.out.println(n.toUpperCase());
        }

        java.util.HashMap<String, Integer> scores = new java.util.HashMap<>();
        scores.put("Zara", 100);
        scores.put("Kai", 55);

        int z = scores.get("Zara"); // auto-unboxing from Integer to int
        System.out.println(z);
    }
}

