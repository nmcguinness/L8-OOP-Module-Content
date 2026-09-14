package t11_generics_1.demos.de02;

public class Demo {
    public static void run()
    {
        new Demo().start();
    }

    public void start()
    {
        Box<String> a = new Box<>("hi");
        System.out.println(a.value().toUpperCase()); // no cast needed

        Box<Integer> b = new Box<>(42);
        System.out.println(b.value() + 1);

        // This does NOT compile (and that's the point):
        // Box<Integer> c = new Box<>("oops");
    }
}

