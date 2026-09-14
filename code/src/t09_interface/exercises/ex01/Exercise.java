package t09_interface.exercises.ex01;

public class Exercise {

    public static void run() {
        Greeter greeter = new CasualGreeter();
        greeter.greet("Alex");

        greeter = new FormalGreeter();
        greeter.greet("Alex");

        greeter = new CasualGreeter();
        greeter.greet("Taylor");
    }
}
