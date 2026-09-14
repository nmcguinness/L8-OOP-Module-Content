package t12_generics_2.exercises.ex10;

/*
 * Wildcard cheat sheet
 *
 * List<?> a
 *   Add:  null only (element type unknown - compiler rejects everything else)
 *   Read: Object (the only safe upper bound when type is completely unknown)
 *
 * List<? extends Number> b
 *   Add:  null only (could be List<Integer>, List<Double>... - unsafe to add anything)
 *   Read: Number (safe upper bound)
 *
 * List<? super Integer> c
 *   Add:  Integer (and its subclasses), plus null (list is known to accept Integer)
 *   Read: Object (list might actually be List<Number> or List<Object> - only Object is safe)
 */
public class Exercise {
    public static void run() {
        java.util.List<? extends Number> b = java.util.List.of(1, 2.0);
        Number n = b.get(0); // safe read as Number

        java.util.ArrayList<Number> c = new java.util.ArrayList<>();
        c.add(42);       // OK - Integer accepted
        c.add(null);     // OK - null always accepted
        Object o = c.get(0); // read as Object only

        System.out.println(n);
        System.out.println(o);
    }
}
