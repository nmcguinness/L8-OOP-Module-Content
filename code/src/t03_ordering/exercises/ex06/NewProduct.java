package t03_ordering.exercises.ex06;

public class NewProduct {
    private String name;
    private double price;
    private Double rating; //note: now its a nullable Double

    public NewProduct(String name, double price, Double rating) {
        this.name = name;
        this.price = price;
        this.rating = rating;
    }

    public String name() { return name; }
    public double price() { return price; }
    public Double rating() { return rating; }

    @Override public String toString() {
        return name + "(EUR " + price + ", " + rating + " stars)";
    }
}
