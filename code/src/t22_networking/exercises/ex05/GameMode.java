package t22_networking.exercises.ex05;

import java.util.Optional;

public enum GameMode {
    RANKED, CASUAL, PRACTICE;

    // Turns client text such as "ranked" into a mode; empty if it is not a valid one
    public static Optional<GameMode> parse(String text) {
        if (text == null)
            return Optional.empty();
        try {
            return Optional.of(valueOf(text.trim().toUpperCase()));
        }
        catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }
}
