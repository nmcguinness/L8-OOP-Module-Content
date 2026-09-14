package t05_collections_1.demos.de03;

;

public class MachineGun extends Weapon{
    private float recoilRate;
    private FireMode fireMode;

    public MachineGun(String make, String model, float calibre,
                      int clipSize, float cost, float weight,
                      float recoilRate, FireMode fireMode)
    {
        // call parent constructor
        this(make, model, calibre, clipSize, cost, weight,
                false,recoilRate, fireMode);
    }

    public MachineGun(String make, String model, float calibre,
                      int clipSize, float cost, float weight, boolean isOnceOnly,
                      float recoilRate, FireMode fireMode)
    {
        // call parent constructor
        super(make, model, calibre, clipSize, cost, weight, isOnceOnly);

        this.recoilRate = recoilRate;
        this.fireMode = fireMode;
    }
    //getters & setters

    public float getRecoilRate() {
        return recoilRate;
    }

    public void setRecoilRate(float recoilRate) {
        this.recoilRate = recoilRate;
    }

    public FireMode getFireMode() {
        return fireMode;
    }

    public void setFireMode(FireMode fireMode) {
        this.fireMode = fireMode;
    }

    //toString
    @Override
    public String toString()
    {
        return "MachineGun{" +
                "recoilRate='" + recoilRate + '\'' +
                "fireMode='" + fireMode + '\'' +
                "," + super.toString() +
                "}";
    }
}
