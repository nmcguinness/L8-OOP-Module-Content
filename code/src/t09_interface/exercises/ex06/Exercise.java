package t09_interface.exercises.ex06;

import java.util.LinkedList;

public class Exercise {

    public static void run() {
        LinkedList<Command> history = new LinkedList<>();
        history.add(new PrintCommand("Hello, world!"));
        history.add(new AddCommand(2, 3));
        history.add(new PrintCommand("Done."));

        for (Command command : history) {
            command.execute();
        }
    }
}
