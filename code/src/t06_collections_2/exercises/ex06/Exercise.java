package t06_collections_2.exercises.ex06;

import java.util.LinkedList;
import java.util.ListIterator;

public class Exercise {

    public static void run() {
        LinkedList<InputEvent> events = new LinkedList<>();
        events.add(new InputEvent("Move"));
        events.add(new InputEvent("Attack"));
        events.add(new InputEvent("Heal"));

        ListIterator<InputEvent> it = events.listIterator();
        while (it.hasNext()) {
            InputEvent e = it.next();
            if ("Attack".equals(e.type)) {
                it.add(new InputEvent("CameraShake"));
            } else if ("Heal".equals(e.type)) {
                it.remove();
            }
        }

        for (InputEvent e : events) {
            System.out.println(e.type);
        }
    }
}
