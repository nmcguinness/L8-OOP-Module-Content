package t09_interface.exercises.ex03;

public class Enemy implements Moveable, Damageable {

    private final String name;
    private int x;
    private int health = 30;

    public Enemy(String name) {
        this.name = name;
    }

    // Enemies move 1 square at a time
    @Override
    public void move() {
        x += 1;
    }

    @Override
    public void takeDamage(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("amount must not be negative");
        }
        health = Math.max(0, health - amount);
    }

    public int getX() {
        return x;
    }

    public int getHealth() {
        return health;
    }

    @Override
    public String toString() {
        return name + " at x=" + x + ", health=" + health;
    }
}
