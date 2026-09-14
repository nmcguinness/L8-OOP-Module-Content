package t03_ordering.exercises.common;

public class Product {
        private String name;
        private double price;
        private double rating;

        public Product(String name, double price, double rating) {
            this.name = name;
            this.price = price;
            this.rating = rating;
        }

        public String name() { return name; }
        public double price() { return price; }
        public double rating() { return rating; }

        @Override public String toString() {
            return name + "(EUR " + price + ", " + rating + " stars)";
        }
    }
