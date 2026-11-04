package t13_design_patterns_1.exercises.ex01;

public class Exercise
{
    public static void run()
    {
        Enemy skeleton = new Enemy("Skeleton", 10, new MeleeAttack());
        Enemy archer = new Enemy("Archer", 10, new RangedAttack());

        int[] distances = new int[] { 1, 5, 12 };

        for (int d : distances)
        {
            System.out.println(skeleton.getName() + " at " + d + "m: " + skeleton.attack(d));
        }

        for (int d : distances)
        {
            System.out.println(archer.getName() + " at " + d + "m: " + archer.attack(d));
        }
    }
}

interface AttackStrategy
{
    int computeDamage(int baseDamage, int distanceMeters);
}

class MeleeAttack implements AttackStrategy
{
    @Override
    public int computeDamage(int baseDamage, int distanceMeters)
    {
        if (distanceMeters > 2)
            return 0;

        return baseDamage + 5;
    }
}

class RangedAttack implements AttackStrategy
{
    @Override
    public int computeDamage(int baseDamage, int distanceMeters)
    {
        if (distanceMeters > 10)
            return 0;

        return baseDamage;
    }
}

class Enemy
{
    private final String name;
    private final int baseDamage;
    private final AttackStrategy strategy;

    public Enemy(String name, int baseDamage, AttackStrategy strategy)
    {
        this.name = name;
        this.baseDamage = baseDamage;
        this.strategy = strategy;
    }

    public String getName()
    {
        return name;
    }

    public int attack(int distanceMeters)
    {
        return strategy.computeDamage(baseDamage, distanceMeters);
    }
}
