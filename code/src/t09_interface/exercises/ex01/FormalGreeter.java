package t09_interface.exercises.ex01;

public class FormalGreeter implements Greeter {

    @Override
    public void greet(String name) {
        System.out.println("Good evening, " + name + ".");
    }
}
