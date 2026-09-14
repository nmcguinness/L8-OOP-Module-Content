package t04_equality_hashing.exercises.ex08;

import java.util.Objects;

public class Exercise {

    public static void run() {
        Tag t = new Tag("enemy");
        System.out.println("Initial tag: " + t);

        t.setName("friend");

        System.out.println("Mutated tag: " + t);

        System.out.println(
            "If Tag were used as a key in a hash-based collection (e.g. HashMap),\n" +
            "changing the 'name' after insertion could change its hash code. The\n" +
            "object might no longer be found in the bucket where it was stored,\n" +
            "breaking lookups and making the collection behave \"randomly\"."
        );
    }

    public static final class Tag {
        private String name;

        public Tag(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String newName) {
            this.name = newName;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof Tag other)) {
                return false;
            }
            return Objects.equals(this.name, other.name);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name);
        }

        @Override
        public String toString() {
            return "Tag{name='" + name + "'}";
        }
    }
}
