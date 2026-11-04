package t09_interface.exercises.ex03;

import java.util.LinkedList;
import java.util.List;

public class Exercise {

    // Moves every actor once, front to back
    public static void moveAll(List<Moveable> actors) {
        for (Moveable m : actors) {
            m.move();
        }
    }

    // Damages every actor that can be damaged; returns how many were hit
    public static int damageAll(List<Moveable> actors, int amount) {
        int hit = 0;
        for (Moveable m : actors) {
            // Check before casting: a Cloud is Moveable but not Damageable
            if (m instanceof Damageable d) {
                d.takeDamage(amount);
                hit++;
            }
        }
        return hit;
    }

    public static void run() {
        LinkedList<Moveable> actors = new LinkedList<>();
        actors.add(new Player("Hero"));
        actors.add(new Enemy("Slime"));
        actors.add(new Cloud());

        moveAll(actors);
        int hit = damageAll(actors, 10);

        System.out.println("Hit " + hit + " of " + actors.size() + " actors");
        for (Moveable m : actors) {
            System.out.println("  " + m);
        }
    }
}
