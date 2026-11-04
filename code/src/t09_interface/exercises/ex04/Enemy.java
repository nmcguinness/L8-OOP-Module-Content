package t09_interface.exercises.ex04;

/**
 * An enemy whose way of attacking is a separate object that can be swapped at runtime.
 */
public class Enemy {

    private AttackStrategy strategy;

    public Enemy(AttackStrategy strategy) {
        setStrategy(strategy);
    }

    public void setStrategy(AttackStrategy strategy) {
        if (strategy == null) {
            throw new IllegalArgumentException("strategy must not be null");
        }
        this.strategy = strategy;
    }

    // The enemy does not know how the attack works; it asks its strategy
    public String performAttack(String targetName) {
        return strategy.attack(targetName);
    }
}
