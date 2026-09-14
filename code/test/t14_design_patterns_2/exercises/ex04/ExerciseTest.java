package t14_design_patterns_2.exercises.ex04;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("t14 e04 - Observer with a payload")
class ExerciseTest {

    /** Records the values it is handed, so the payload itself can be asserted. */
    private static final class RecordingListener implements TemperatureListener {
        private final List<Double> seen = new ArrayList<>();

        @Override
        public void onTemperatureChanged(double celsius) {
            seen.add(celsius);
        }
    }

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

    @Test
    void setTemperature_passesTheValueToTheListener() {
        TemperatureSensor sensor = new TemperatureSensor();
        RecordingListener listener = new RecordingListener();
        sensor.addListener(listener);

        sensor.setTemperature(35.0);
        sensor.setTemperature(60.0);

        assertEquals(List.of(35.0, 60.0), listener.seen);
    }

    @Test
    void setTemperature_notifiesEveryListenerWithTheSameValue() {
        TemperatureSensor sensor = new TemperatureSensor();
        RecordingListener a = new RecordingListener();
        RecordingListener b = new RecordingListener();
        sensor.addListener(a);
        sensor.addListener(b);

        sensor.setTemperature(21.5);

        assertEquals(List.of(21.5), a.seen);
        assertEquals(List.of(21.5), b.seen);
    }

    @Test
    void overheatAlarm_firesAtOrAboveTheThreshold() {
        String out = capture(() -> new OverheatAlarmListener(55.0).onTemperatureChanged(55.0));
        assertTrue(out.contains("ALARM"), "55.0 is at the threshold, so it must alarm");
    }

    @Test
    void overheatAlarm_staysSilentBelowTheThreshold() {
        String out = capture(() -> new OverheatAlarmListener(55.0).onTemperatureChanged(54.9));
        assertFalse(out.contains("ALARM"), out);
    }

    @Test
    void removeListener_stopsFurtherNotifications() {
        TemperatureSensor sensor = new TemperatureSensor();
        RecordingListener listener = new RecordingListener();
        sensor.addListener(listener);

        sensor.setTemperature(10.0);
        assertTrue(sensor.removeListener(listener));
        sensor.setTemperature(20.0);

        assertEquals(List.of(10.0), listener.seen);
    }

    @Test
    void removeListener_thatWasNeverAdded_returnsFalse() {
        assertFalse(new TemperatureSensor().removeListener(new RecordingListener()));
    }

    @Test
    void addOrRemoveNull_throwsIllegalArgumentException() {
        TemperatureSensor sensor = new TemperatureSensor();
        assertThrows(IllegalArgumentException.class, () -> sensor.addListener(null));
        assertThrows(IllegalArgumentException.class, () -> sensor.removeListener(null));
    }
}
