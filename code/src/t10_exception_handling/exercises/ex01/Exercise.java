package t10_exception_handling.exercises.ex01;

import java.util.Optional;

public class Exercise {

    // Parses input as an int; an empty Optional means "not a valid int"
    public static Optional<Integer> parseIntSafe(String input) {
        if (input == null) {
            return Optional.empty();
        }

        try {
            return Optional.of(Integer.parseInt(input.trim()));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    public static void run() {
        String[] inputs = {"42", " -7 ", "abc", "", "99999999999", null};
        for (String in : inputs) {
            System.out.println("\"" + in + "\" -> " + parseIntSafe(in));
        }
    }
}
