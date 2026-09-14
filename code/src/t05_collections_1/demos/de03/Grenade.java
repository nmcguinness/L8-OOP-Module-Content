package t05_collections_1.demos.de03;

public class Grenade extends Weapon{
    private ExplosiveType explosiveType;
    private float range;

    public ExplosiveType getExplosiveType() {
        return explosiveType;
    }

    public void setExplosiveType(ExplosiveType explosiveType) {
        this.explosiveType = explosiveType;
    }

    public float getRange() {
        return range;
    }

    public void setRange(float range) {
        this.range = range;
    }

    public Grenade(String make, String model, float calibre, int clipSize, float cost, float weight, ExplosiveType explosiveType, float range) {
        this(make, model, calibre, clipSize, cost,
                weight, false, explosiveType, range);
    }

    public Grenade(String make, String model, float calibre, int clipSize, float cost, float weight, boolean isOnceOnly, ExplosiveType explosiveType, float range) {
        super(make, model, calibre, clipSize, cost, weight, isOnceOnly);
        this.explosiveType = explosiveType;
        this.range = range;
    }

    @Override
    public String toString() {
        return "Grenade{" +
                "explosiveType=" + explosiveType +
                ", range=" + range +
                "," + super.toString() +
                '}';
    }
}
