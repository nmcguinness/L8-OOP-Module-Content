package t05_collections_1.demos.de03;

public class Weapon {
    private String make;
    private String model;
    private float calibre;
    private int clipSize;
    private float cost;
    private float weight;
    private boolean isOnceOnly;
    private WeaponType weaponType = WeaponType.Default;

    public Weapon(String make, String model, float calibre,
                  int clipSize, float cost, float weight) {
        this(make, model, calibre, clipSize, cost, weight, false);
    }

    public Weapon(String make, String model, float calibre,
                  int clipSize, float cost, float weight, boolean isOnceOnly) {
        this.make = make;
        this.model = model;
        this.calibre = calibre;
        this.clipSize = clipSize;
        this.cost = cost;
        this.weight = weight;
        this.isOnceOnly = isOnceOnly;
    }

    public WeaponType getWeaponType() {
        return weaponType;
    }

    public void setWeaponType(WeaponType weaponType) {
        this.weaponType = weaponType;
    }

    public boolean isOnceOnly() {
        return isOnceOnly;
    }

    public void setOnceOnly(boolean onceOnly) {
        isOnceOnly = onceOnly;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public float getCost() {
        return cost;
    }

    public void setCost(float cost) {
        this.cost = cost;
    }

    public int getClipSize() {
        return clipSize;
    }

    public void setClipSize(int clipSize) {
        this.clipSize = clipSize;
    }

    public float getCalibre() {
        return calibre;
    }

    public void setCalibre(float calibre) {
        this.calibre = calibre;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    @Override
    public String toString() {
        return "Weapon{" +
                "type=`" + weaponType+ '\'' +
                "make='" + make + '\'' +
                ", model='" + model + '\'' +
                ", calibre=" + calibre +
                ", clipSize=" + clipSize +
                ", cost=" + cost +
                ", weight=" + weight +
                ", isOnceOnly=" + isOnceOnly +
                '}';
    }
}
