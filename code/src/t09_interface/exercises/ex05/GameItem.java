package t09_interface.exercises.ex05;

public class GameItem implements Named {  //IHaveWeight::getWeight()

    private final String name;

    public GameItem(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }
}
