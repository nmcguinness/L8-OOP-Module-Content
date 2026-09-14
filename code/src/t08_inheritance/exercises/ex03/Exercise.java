package t08_inheritance.exercises.ex03;

public class Exercise {

    public static void run() {
        Entity e = new Entity("Rock");
        Player p = new Player("Zara", 100);
        Enemy g = new Enemy("Goblin");
        BossEnemy dragon = new BossEnemy("Dragon", 5);

        System.out.println(e.describe());
        System.out.println(p.describe());
        System.out.println(g.describe());
        System.out.println(dragon.describe());

        /*
         * super(name) in BossEnemy's constructor calls the Enemy(String)
         * constructor, which then calls Entity(String). This ensures all
         * base-class fields are initialised before BossEnemy adds its own.
         *
         * Calling super.describe() in BossEnemy lets us reuse the Enemy
         * description and only append the boss-specific detail, instead of
         * rebuilding the whole string from scratch.
         */
    }
}
