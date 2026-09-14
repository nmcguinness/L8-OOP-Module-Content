package t06_collections_2.exercises.ex06;

public class InputEvent {
    String type;

    InputEvent(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return type;
    }
}

