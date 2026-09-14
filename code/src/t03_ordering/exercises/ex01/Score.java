package t03_ordering.exercises.ex01;

/**
 * value desc, then player asc
 */
public class Score implements Comparable<Score> {

    private String player;
    private int value;

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getPlayer() {
        return player;
    }

    public void setPlayer(String player) {
        this.player = player;
    }

    public Score(String player, int value) {
        this.player = player;
        this.value = value;
    }

    @Override
    public int compareTo(Score other) {
        int byValue = Integer.compare(other.value, this.value); // desc
        if (byValue != 0) return byValue;
        return this.player.compareTo(other.player);             // asc
    }

    @Override
    public String toString() {
        return player + ":" + value;
    }
}

