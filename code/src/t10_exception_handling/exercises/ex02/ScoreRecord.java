package t10_exception_handling.exercises.ex02;

public class ScoreRecord {

    public static final int MIN = 0;
    public static final int MAX = 100;

    private int score;

    public void setScore(int score) {
        if (score < MIN || score > MAX) {
            throw new InvalidScoreException(score);
        }
        this.score = score;
    }

    public int score() {
        return score;
    }
}
