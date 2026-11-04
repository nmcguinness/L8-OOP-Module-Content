package t14_design_patterns_2.exercises.ex04;

import java.util.ArrayList;
import java.util.List;

public class Exercise {
    public static void run() {
        TemperatureSensor sensor = new TemperatureSensor();

        sensor.addListener(new ConsoleDisplayListener());
        sensor.addListener(new OverheatAlarmListener(55.0));

        sensor.setTemperature(35.0);
        sensor.setTemperature(60.0);
        sensor.setTemperature(54.9);
        sensor.setTemperature(55.0);
    }
}

interface TemperatureListener {
    void onTemperatureChanged(double celsius);
}

class ConsoleDisplayListener implements TemperatureListener {
    @Override
    public void onTemperatureChanged(double celsius) {
        System.out.println("TEMP: " + celsius + "C");
    }
}

class OverheatAlarmListener implements TemperatureListener {
    private final double threshold;

    public OverheatAlarmListener(double threshold) {
        this.threshold = threshold;
    }

    @Override
    public void onTemperatureChanged(double celsius) {
        if (celsius >= threshold) {
            System.out.println("ALARM: " + celsius + "C");
        }
    }
}

class TemperatureSensor {
    private final List<TemperatureListener> listeners = new ArrayList<>();

    public void addListener(TemperatureListener l) {
        if (l == null) {
            throw new IllegalArgumentException("listener is null.");
        }

        listeners.add(l);
    }

    public boolean removeListener(TemperatureListener l) {
        if (l == null) {
            throw new IllegalArgumentException("listener is null.");
        }

        return listeners.remove(l);
    }

    public void setTemperature(double celsius) {
        for (TemperatureListener l : listeners) {
            l.onTemperatureChanged(celsius);
        }
    }
}
