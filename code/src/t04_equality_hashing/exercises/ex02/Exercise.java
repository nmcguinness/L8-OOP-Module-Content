package t04_equality_hashing.exercises.ex02;

public class Exercise {

    public static void run() {
        System.out.println("equals/hashCode contract (summary):");
        System.out.println(" - Reflexive: x.equals(x) is always true.");
        System.out.println(" - Symmetric: x.equals(y) == y.equals(x).");
        System.out.println(" - Transitive: if x==y and y==z then x==z.");
        System.out.println(" - Consistent: repeated calls give same result while objects unchanged.");
        System.out.println(" - Non-null: x.equals(null) is always false.");

        /*
         * Tiny counterexample that BREAKS SYMMETRY:
         *
         * class WeirdString {
         *     private final String text;
         *
         *     WeirdString(String text) {
         *         this.text = text;
         *     }
         *
         *     @Override
         *     public boolean equals(Object o) {
         *         // BAD: treats a raw String as equal, but String
         *         // itself does NOT know about WeirdString.
         *         if (o instanceof String s) {
         *             return text.equalsIgnoreCase(s);
         *         }
         *         if (o instanceof WeirdString other) {
         *             return text.equalsIgnoreCase(other.text);
         *         }
         *         return false;
         *     }
         * }
         *
         * WeirdString w = new WeirdString("hello");
         * String s = "HELLO";
         * w.equals(s)  // true
         * s.equals(w)  // false (String.equals only handles String)
         *
         * This is dangerous because sets/maps that rely on equals(...)
         * can become inconsistent: membership tests and lookups may give
         * different answers depending on which side of the comparison
         * is used.
         */
    }
}
