package t12_generics_2.exercises.ex02;

class Entity { }

class Lists {
    public static void printAll(java.util.List<?> items) {
        if (items == null)
            return;

        for (Object obj : items)
            System.out.println(obj);
    }
}

public class Exercise {
    public static void run() {
        Lists.printAll(java.util.List.of("A", "B"));
        Lists.printAll(java.util.List.of(10, 20));

        java.util.List<Entity> entities = java.util.List.of(new Entity(), new Entity());
        Lists.printAll(entities);
    }
}
