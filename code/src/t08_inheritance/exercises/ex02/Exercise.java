package t08_inheritance.exercises.ex02;

public class Exercise {

    public static void run() {
        Entity e = new Entity("Rock");
        Player p = new Player("Zara", 100);
        Enemy g = new Enemy("Goblin");

        System.out.println(e.describe());
        System.out.println(p.describe());
        System.out.println(g.describe());
    }
}
