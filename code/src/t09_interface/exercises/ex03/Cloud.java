package t09_interface.exercises.ex03;

// Moves, but cannot be damaged: it implements only one of the two interfaces
public class Cloud implements Moveable {

    private int x;

    @Override
    public void move() {
        x += 3;
    }

    public int getX() {
        return x;
    }

    @Override
    public String toString() {
        return "Cloud at x=" + x;
    }
}
