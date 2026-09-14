package t14_design_patterns_2.exercises.ex06;

public class Exercise {
    public static void run() {
        ThirdPartyGateway gateway = new ThirdPartyGateway();
        CheckoutService checkout = new GatewayCheckoutAdapter(gateway);

        System.out.println("checkout(0) -> " + checkout.checkout(0));
        System.out.println("checkout(199) -> " + checkout.checkout(199));
        System.out.println("checkout(1299) -> " + checkout.checkout(1299));
        System.out.println("checkout(1205) -> " + checkout.checkout(1205));
    }
}

interface CheckoutService {
    boolean checkout(int cents);
}

class ThirdPartyGateway {
    public boolean makePayment(String euroAmount) {
        if (euroAmount == null || euroAmount.equals("0.00"))
            return false;

        if (euroAmount.startsWith("0.00"))
            return false;

        System.out.println("PAID: " + euroAmount);
        return true;
    }
}

class GatewayCheckoutAdapter implements CheckoutService {
    private ThirdPartyGateway _gateway;

    public GatewayCheckoutAdapter(ThirdPartyGateway gateway) {
        if (gateway == null)
            throw new IllegalArgumentException("gateway is null.");

        _gateway = gateway;
    }

    @Override
    public boolean checkout(int cents) {
        if (cents < 0)
            throw new IllegalArgumentException("cents must be >= 0.");

        return _gateway.makePayment(formatEuro(cents));
    }

    private String formatEuro(int cents) {
        int euros = cents / 100;
        int remainder = cents % 100;
        String rem = remainder < 10 ? "0" + remainder : String.valueOf(remainder);
        return euros + "." + rem;
    }
}
