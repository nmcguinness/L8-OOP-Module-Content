package t09_interface.exercises.ex09;

import t09_interface.exercises.ex08.TextFilter;

import java.util.Locale;

public class ProfanityFilter implements TextFilter {

    private final Iterable<String> badWords;

    public ProfanityFilter(Iterable<String> badWords) {
        this.badWords = badWords;
    }

    @Override
    public String apply(String input) {
        if (input == null) {
            return null;
        }

        String result = input;

        for (String bad : badWords) {
            if (bad == null || bad.isBlank()) {
                continue;
            }

            String badLower = bad.toLowerCase(Locale.ROOT).trim();
            if (badLower.isEmpty()) {
                continue;
            }

            result = result.replaceAll("(?i)" + badLower, redactWord(badLower));
        }

        return result;
    }

    private String redactWord(String word)
    {
        if (word == null || word.isEmpty())
            return word;

        if (word.length() == 1)
            return word;

        // Keep the first char, replace the rest with '*'
        int starCount = word.length() - 1;
        return word.charAt(0) + "*".repeat(starCount);
    }

}
