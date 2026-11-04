package t09_interface.exercises.ex04;

public class MeleeAttack implements AttackStrategy {

    @Override
    public String attack(String targetName) {
        return "Swings sword at " + targetName + "!";
    }
}
