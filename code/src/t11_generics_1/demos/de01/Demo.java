package t11_generics_1.demos.de01;

public class Demo {
    public static void run()
    {
        new t11_generics_1.demos.de01.Demo().start();
    }

    public void start()
    {
        BoxObject box = new BoxObject("hi");

        String s = (String) box.value(); // OK at runtime
        System.out.println(s.toUpperCase());

        // This compiles, but can crash at runtime:
        //Integer n = (Integer) box.value(); // ClassCastException at runtime
        //System.out.println(n + 1);
    }
}

