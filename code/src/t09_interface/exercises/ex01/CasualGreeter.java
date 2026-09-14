package t09_interface.exercises.ex01;

public class CasualGreeter implements Greeter {

    @Override
    public void greet(String name) {
        System.out.println("Hey, " + name + "!");
    }
}
