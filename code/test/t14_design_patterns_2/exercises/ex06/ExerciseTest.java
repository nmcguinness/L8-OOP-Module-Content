package t14_design_patterns_2.exercises.ex06;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("t14 e06 - Adapter translating cents to the gateway's euro strings")
class ExerciseTest {

    private static String capture(Runnable action) {
        PrintStream original = System.out;
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        System.setOut(new PrintStream(buffer, true, StandardCharsets.UTF_8));
        try {
            action.run();
        } finally {
            System.setOut(original);
        }
        return buffer.toString(StandardCharsets.UTF_8);
    }

    private static CheckoutService service() {
        return new GatewayCheckoutAdapter(new ThirdPartyGateway());
    }

    @Test
    void checkout_zero_isRejectedByTheGateway() {
        assertFalse(service().checkout(0));
    }

    @Test
    void checkout_ordinaryAmount_succeeds() {
        assertTrue(service().checkout(199));
    }

    @Test
    void checkout_padsASingleDigitRemainder() {
        // 1205 cents must become "12.05", not "12.5".
        String out = capture(() -> service().checkout(1205));
        assertTrue(out.contains("PAID: 12.05"), out);
    }

    @Test
    void checkout_twoDigitRemainder_isNotPadded() {
        String out = capture(() -> service().checkout(1299));
        assertTrue(out.contains("PAID: 12.99"), out);
    }

    @Test
    void checkout_underOneEuro_stillFormatsWithALeadingZero() {
        String out = capture(() -> service().checkout(5));
        assertTrue(out.contains("PAID: 0.05"), out);
    }

    @Test
    void checkout_negativeCents_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> service().checkout(-1));
    }

    @Test
    void constructor_nullGateway_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new GatewayCheckoutAdapter(null));
    }

    @Test
    void gateway_rejectsAZeroAmountString() {
        // The adapter exists because the gateway speaks strings, not cents.
        assertFalse(new ThirdPartyGateway().makePayment("0.00"));
        assertFalse(new ThirdPartyGateway().makePayment(null));
    }
}
