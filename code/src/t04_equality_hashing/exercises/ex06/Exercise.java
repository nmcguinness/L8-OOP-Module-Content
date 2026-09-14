package t04_equality_hashing.exercises.ex06;

import java.util.Objects;

public class Exercise {

    public static void run() {
        Customer a = new Customer("C1", "a@example.com");
        Customer b = new Customer("C1", "different@example.com");
        Customer c = new Customer("C2", "a@example.com");

        System.out.println("a.equals(b) = " + a.equals(b));
        System.out.println("a.equals(c) = " + a.equals(c));
        System.out.println("hash(a) == hash(b) ? " + (a.hashCode() == b.hashCode()));
    }

    public static final class Customer {
        private final String id;
        private final String email;

        public Customer(String id, String email) {
            this.id = id;
            this.email = email;
        }

        public String getId() {
            return id;
        }

        public String getEmail() {
            return email;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof Customer other)) {
                return false;
            }
            return Objects.equals(this.id, other.id);
        }

        @Override
        public int hashCode() {
            return Objects.hash(id);
        }

        @Override
        public String toString() {
            return "Customer{id='" + id + "', email='" + email + "'}";
        }
    }
}
