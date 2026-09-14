package t09_interface.exercises.ex06;

public class PrintCommand implements Command {

    private final String message;

    public PrintCommand(String message) {
        this.message = message;
    }

    @Override
    public void execute() {
        System.out.println(message);
    }
}
