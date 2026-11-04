package t10_exception_handling.exercises.ex02;

public class Exercise {

    public static void run() {
        ScoreRecord record = new ScoreRecord();
        int[] attempts = {75, 101, -5, 100};

        for (int value : attempts) {
            try {
                record.setScore(value);
                System.out.println("Set score to " + record.score());
            } catch (InvalidScoreException e) {
                System.out.println("Rejected " + e.getScore() + ": " + e.getMessage()
                        + " (score stays " + record.score() + ")");
            }
        }
    }
}
