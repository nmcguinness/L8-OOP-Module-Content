package t09_interface.exercises.ex06;

public class AddCommand implements Command {

    private final int a;
    private final int b;

    public AddCommand(int a, int b) {
        this.a = a;
        this.b = b;
    }

    @Override
    public void execute() {
        int result = a + b;
        System.out.println(a + " + " + b + " = " + result);
    }
}
