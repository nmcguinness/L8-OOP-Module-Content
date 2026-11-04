package t10_exception_handling.exercises.ex02;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("t10 ex02 - custom unchecked exception")
class ScoreRecordTest {

    @Test
    void setScore_boundaries_areAccepted() {
        ScoreRecord record = new ScoreRecord();

        record.setScore(0);
        assertEquals(0, record.score());
        record.setScore(100);
        assertEquals(100, record.score());
    }

    @Test
    void setScore_outOfRange_throwsWithTheBadValue() {
        ScoreRecord record = new ScoreRecord();

        InvalidScoreException e = assertThrows(InvalidScoreException.class,
                () -> record.setScore(101));

        assertEquals(101, e.getScore());
    }

    @Test
    void setScore_rejected_leavesPreviousScoreUnchanged() {
        ScoreRecord record = new ScoreRecord();
        record.setScore(60);

        assertThrows(InvalidScoreException.class, () -> record.setScore(-1));

        assertEquals(60, record.score());
    }

    @Test
    void invalidScoreException_isUnchecked() {
        assertInstanceOf(RuntimeException.class, new InvalidScoreException(5));
    }
}
