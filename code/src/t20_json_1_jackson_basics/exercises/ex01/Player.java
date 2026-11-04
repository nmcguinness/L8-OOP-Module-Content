package t20_json_1_jackson_basics.exercises.ex01;

/**
 * A player that Jackson can read and write: it has a no-argument constructor,
 * plus a getter and a setter for each field.
 */
public class Player {

    private String name;
    private int score;

    // Jackson calls this, then fills the fields through the setters
    public Player() {
        this("", 0);
    }

    public Player(String name, int score) {
        this.name = name;
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
