package t11_generics_1.exercises.ex01;

public class Exercise {

    public static void run() {
        InventoryLegacy legacy = new InventoryLegacy();
        legacy.add("sword");
        legacy.add(123);

        String first = (String) legacy.get(0);
        System.out.println("Legacy item 0: " + first.toUpperCase());

        try {
            // Compiles without complaint, but item 1 is an Integer
            String second = (String) legacy.get(1);
            System.out.println("Legacy item 1: " + second);
        } catch (ClassCastException e) {
            System.out.println("Legacy item 1 crashed: ClassCastException");
        }

        Inventory<String> safe = new Inventory<>();
        safe.add("shield");
        // safe.add(123);  <- does not compile: Integer cannot be converted to String
        System.out.println("Generic item 0: " + safe.get(0).toUpperCase());
    }
}
