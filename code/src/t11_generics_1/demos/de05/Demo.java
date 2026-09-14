package t11_generics_1.demos.de05;

public class Demo {
    public static void run()
    {
        new Demo().start();
    }

    public void start()
    {
        java.util.List<String> a = java.util.List.of("A", "B", "C", "Z", "D");
        java.util.List<Integer> b = java.util.List.of(10, 20, 5, 12);

        String s = max(a);
        Integer n = max(b);

        System.out.println(s);
        System.out.println(n);
    }

    public static <T extends Comparable<T>> T max(java.util.List<T> items) {
        if (items == null || items.isEmpty())
            return null;

        T best = items.get(0);

        for (int i = 1; i < items.size(); i++) {
            T candidate = items.get(i);
            if (candidate.compareTo(best) > 0)
                best = candidate;
        }
        return best;
    }
}

