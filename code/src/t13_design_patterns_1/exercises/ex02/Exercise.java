package t13_design_patterns_1.exercises.ex02;

public class Exercise
{
    public static void run()
    {
        double[] basePrices = new double[] { 10.00, 4.00 };

        Checkout none = new Checkout(new NoDiscount());
        Checkout student = new Checkout(new StudentDiscount());
        Checkout bf = new Checkout(new BlackFridayDiscount());

        for (double p : basePrices)
        {
            System.out.println("Base " + p + " -> none: " + none.price(p));
            System.out.println("Base " + p + " -> student: " + student.price(p));
            System.out.println("Base " + p + " -> black friday: " + bf.price(p));
        }
    }
}

interface PriceStrategy
{
    double finalPrice(double basePrice);
}

class NoDiscount implements PriceStrategy
{
    @Override
    public double finalPrice(double basePrice)
    {
        return basePrice;
    }
}

class StudentDiscount implements PriceStrategy
{
    @Override
    public double finalPrice(double basePrice)
    {
        return basePrice * 0.9;
    }
}

class BlackFridayDiscount implements PriceStrategy
{
    @Override
    public double finalPrice(double basePrice)
    {
        double discounted = basePrice * 0.7;

        if (discounted < 5.0)
            return 5.0;

        return discounted;
    }
}

class Checkout
{
    private PriceStrategy _pricing;

    public Checkout(PriceStrategy pricing)
    {
        _pricing = pricing;
    }

    public double price(double basePrice)
    {
        return _pricing.finalPrice(basePrice);
    }
}
