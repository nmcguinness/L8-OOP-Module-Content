package t01_arrays.exercises.ex09;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("t01 ex09 - tic-tac-toe checker")
class ExerciseTest {

    private static final char E = ' ';

    @Test
    void hasWon_completeTopRow_returnsTrue() {
        char[][] b = {{'X', 'X', 'X'}, {'O', E, 'O'}, {E, E, E}};
        assertTrue(Exercise.hasWon(b, 'X'));
    }

    @Test
    void hasWon_completeColumn_returnsTrue() {
        char[][] b = {{'O', 'X', E}, {'O', 'X', E}, {'O', E, E}};
        assertTrue(Exercise.hasWon(b, 'O'));
    }

    @Test
    void hasWon_leadingDiagonal_returnsTrue() {
        char[][] b = {{'X', 'O', E}, {'O', 'X', E}, {E, E, 'X'}};
        assertTrue(Exercise.hasWon(b, 'X'));
    }

    @Test
    void hasWon_antiDiagonal_returnsTrue() {
        char[][] b = {{E, 'O', 'X'}, {'O', 'X', E}, {'X', E, E}};
        assertTrue(Exercise.hasWon(b, 'X'));
    }

    @Test
    void hasWon_noLine_returnsFalse() {
        char[][] b = {{'X', 'O', 'X'}, {'O', 'X', E}, {E, E, 'O'}};
        assertFalse(Exercise.hasWon(b, 'O'));
    }

    @Test
    void hasWon_emptyBoardCheckedForSpace_wouldReportAFalseWin() {
        // Documents a real limitation: the method compares against the given char,
        // so passing ' ' finds a "winning line" of blanks. Callers must pass 'X' or 'O'.
        char[][] b = {{E, E, E}, {E, E, E}, {E, E, E}};
        assertTrue(Exercise.hasWon(b, E));
    }

    @Test
    void isFull_boardWithABlank_returnsFalse() {
        char[][] b = {{'X', 'O', 'X'}, {'O', 'X', E}, {E, E, 'O'}};
        assertFalse(Exercise.isFull(b));
    }

    @Test
    void isFull_completelyFilledBoard_returnsTrue() {
        char[][] b = {{'X', 'O', 'X'}, {'O', 'X', 'O'}, {'O', 'X', 'O'}};
        assertTrue(Exercise.isFull(b));
    }
}
