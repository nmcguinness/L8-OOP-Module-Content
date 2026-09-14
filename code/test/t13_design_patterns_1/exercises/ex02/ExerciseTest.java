package t13_design_patterns_1.exercises.ex02;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("t13 ex02 - Strategy: pricing rules")
class ExerciseTest {

    /** Money computed with double is never exactly equal, so compare with a tolerance. */
    private static final double TOLERANCE = 1e-9;

    @Test
    void noDiscount_returnsTheBasePriceUnchanged() {
        assertEquals(10.00, new NoDiscount().finalPrice(10.00), TOLERANCE);
    }

    @Test
    void studentDiscount_takesTenPercentOff() {
        assertEquals(9.00, new StudentDiscount().finalPrice(10.00), TOLERANCE);
    }

    @Test
    void blackFriday_takesThirtyPercentOff() {
        assertEquals(7.00, new BlackFridayDiscount().finalPrice(10.00), TOLERANCE);
    }

    @Test
    void blackFriday_neverGoesBelowTheFiveEuroFloor() {
        // 4.00 * 0.7 is 2.80, but the floor clamps it.
        assertEquals(5.00, new BlackFridayDiscount().finalPrice(4.00), TOLERANCE);
    }

    @Test
    void blackFriday_exactlyAtTheFloor_isNotClamped() {
        // 7.15 * 0.7 is just above 5.00, so the discount survives.
        assertEquals(5.005, new BlackFridayDiscount().finalPrice(7.15), TOLERANCE);
    }

    @Test
    void checkout_delegatesToItsStrategy() {
        assertEquals(9.00, new Checkout(new StudentDiscount()).price(10.00), TOLERANCE);
        assertEquals(10.00, new Checkout(new NoDiscount()).price(10.00), TOLERANCE);
    }

    @Test
    void checkout_sameBasePriceThroughEachStrategy_givesThreeDifferentTotals() {
        double base = 10.00;
        assertEquals(10.00, new Checkout(new NoDiscount()).price(base), TOLERANCE);
        assertEquals(9.00, new Checkout(new StudentDiscount()).price(base), TOLERANCE);
        assertEquals(7.00, new Checkout(new BlackFridayDiscount()).price(base), TOLERANCE);
    }

    @Test
    void discounts_ofZero_stayZero() {
        assertEquals(0.0, new StudentDiscount().finalPrice(0.0), TOLERANCE);
        assertEquals(5.0, new BlackFridayDiscount().finalPrice(0.0), TOLERANCE);
    }
}
