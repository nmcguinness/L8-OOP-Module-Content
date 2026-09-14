package t14_design_patterns_2.exercises.ex03;

import java.util.ArrayList;
import java.util.List;

public class Exercise {
    public static void run() {
        Button button = new Button();

        ClickListener sfx = new SoundListener();
        ClickListener analytics = new AnalyticsListener();

        button.addListener(sfx);
        button.addListener(analytics);

        button.click();
        button.click();

        button.removeListener(sfx);

        button.click();
    }
}

interface ClickListener {
    void onClick();
}

class SoundListener implements ClickListener {
    @Override
    public void onClick() {
        System.out.println("SFX: click");
    }
}

class AnalyticsListener implements ClickListener {
    @Override
    public void onClick() {
        System.out.println("ANALYTICS: click");
    }
}

class Button {
    private List<ClickListener> _listeners = new ArrayList<>();

    public void addListener(ClickListener l) {
        if (l == null)
            throw new IllegalArgumentException("listener is null.");

        _listeners.add(l);
    }

    public boolean removeListener(ClickListener l) {
        if (l == null)
            throw new IllegalArgumentException("listener is null.");

        return _listeners.remove(l);
    }

    public void click() {
        for (ClickListener l : _listeners)
            l.onClick();
    }
}
