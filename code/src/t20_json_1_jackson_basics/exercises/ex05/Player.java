package t20_json_1_jackson_basics.exercises.ex05;

import java.util.Objects;

/**
 * The Exercise 01 player, plus equals and hashCode so that a round-trip test
 * can compare the object before and after with assertEquals.
 */
public class Player {

    private String name;
    private int score;

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

    @Override
    public int hashCode() {
        return Objects.hash(name, score);
    }

    @Override
    public String toString() {
        return "Player{name='" + name + "', score=" + score + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Player other)) {
            return false;
        }
        return score == other.score && Objects.equals(name, other.name);
    }
}
