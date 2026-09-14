package t11_generics_1.demos.de04;

public class Demo {
    public static void run()
    {
        new Demo().start();
    }

    public void start()
    {
        java.util.List<String> a = java.util.List.of("A", "B");
        java.util.List<Integer> b = java.util.List.of(10, 20);

        String s = first(a);
        Integer n = first(b);

        System.out.println(s);
        System.out.println(n);
    }

    public static <T> T first(java.util.List<T> items) {
        if (items == null || items.isEmpty())
            return null;
        return items.get(0);
    }
}

