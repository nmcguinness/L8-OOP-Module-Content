package t14_design_patterns_2.exercises.ex03;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("t14 e03 - Observer: button click listeners")
class ExerciseTest {

    /**
     * A listener that records instead of printing. Observer hands us the seam,
     * so there is no need to capture System.out here.
     */
    private static final class CountingListener implements ClickListener {
        private int clicks;

        @Override
        public void onClick() {
            clicks++;
        }
    }

    @Test
    void click_noListeners_doesNothingAndDoesNotThrow() {
        new Button().click();
    }

    @Test
    void click_notifiesTheRegisteredListener() {
        Button button = new Button();
        CountingListener listener = new CountingListener();
        button.addListener(listener);

        button.click();

        assertEquals(1, listener.clicks);
    }

    @Test
    void click_notifiesEveryListener() {
        Button button = new Button();
        CountingListener a = new CountingListener();
        CountingListener b = new CountingListener();
        button.addListener(a);
        button.addListener(b);

        button.click();
        button.click();

        assertEquals(2, a.clicks);
        assertEquals(2, b.clicks);
    }

    @Test
    void removeListener_stopsFurtherNotifications() {
        Button button = new Button();
        CountingListener staying = new CountingListener();
        CountingListener leaving = new CountingListener();
        button.addListener(staying);
        button.addListener(leaving);

        button.click();
        assertTrue(button.removeListener(leaving));
        button.click();

        assertEquals(2, staying.clicks);
        assertEquals(1, leaving.clicks, "the removed listener must not hear the second click");
    }

    @Test
    void removeListener_thatWasNeverAdded_returnsFalse() {
        assertFalse(new Button().removeListener(new CountingListener()));
    }

    @Test
    void addOrRemoveNull_throwsIllegalArgumentException() {
        Button button = new Button();
        assertThrows(IllegalArgumentException.class, () -> button.addListener(null));
        assertThrows(IllegalArgumentException.class, () -> button.removeListener(null));
    }

    @Test
    void theButtonNeverNeedsToKnowWhatAListenerDoes() {
        // Adding a brand-new kind of listener requires no change to Button.
        Button button = new Button();
        StringBuilder log = new StringBuilder();
        button.addListener(() -> log.append("custom"));

        button.click();

        assertEquals("custom", log.toString());
    }
}
