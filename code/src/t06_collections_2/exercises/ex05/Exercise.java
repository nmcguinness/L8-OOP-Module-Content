package t06_collections_2.exercises.ex05;

import java.util.Deque;
import java.util.LinkedList;

public class Exercise {

    private static String state = "";
    private static final Deque<String> undo = new LinkedList<>();
    private static final Deque<String> redo = new LinkedList<>();

    private static void type(String text) {
        undo.push(state);
        state = state + text;
        redo.clear();
    }

    private static void undo() {
        if (!undo.isEmpty()) {
            redo.push(state);
            state = undo.pop();
        }
    }

    private static void redo() {
        if (!redo.isEmpty()) {
            undo.push(state);
            state = redo.pop();
        }
    }

    public static void run() {
        type("a");
        type("b");
        type("c");

        undo();
        undo();
        redo();
        type("Z");

        System.out.println("Final state: " + state);
    }
}
