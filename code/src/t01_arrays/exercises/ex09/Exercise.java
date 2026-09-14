package t01_arrays.exercises.ex09;

public class Exercise {

    /**
     * Checks if player ('X' or 'O') has a complete row/column/diagonal.
     * Board is a 3x3 matrix containing 'X','O',' ' (space).
     */
    static boolean hasWon(char[][] b, char player) {
        // rows
        for (int r = 0; r < 3; r++)
            if (b[r][0] == player && b[r][1] == player && b[r][2] == player) return true;
        // cols
        for (int c = 0; c < 3; c++)
            if (b[0][c] == player && b[1][c] == player && b[2][c] == player) return true;
        // diags
        if (b[0][0] == player && b[1][1] == player && b[2][2] == player) return true;
        if (b[0][2] == player && b[1][1] == player && b[2][0] == player) return true;
        return false;
    }

    static boolean isFull(char[][] b) {
        for (int r = 0; r < 3; r++)
            for (int c = 0; c < 3; c++)
                if (b[r][c] == ' ') return false;
        return true;
    }

    public static void run() {
        char[][] winRow = {
                {'X','X','X'},
                {'O',' ','O'},
                {' ',' ',' '}
        };
        char[][] notWinO = {
                {'X','O','X'},
                {'O','X',' '},
                {' ',' ','O'}
        };
        System.out.println(hasWon(winRow,'X'));    // true
        System.out.println(hasWon(notWinO,'O'));   // false
        System.out.println(isFull(notWinO));       // false
    }
}
