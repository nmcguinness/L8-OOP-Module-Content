package t09_interface.exercises.ex09;

import t09_interface.exercises.ex08.LowercaseFilter;
import t09_interface.exercises.ex08.TextFilter;
import t09_interface.exercises.ex08.TextFilters;
import t09_interface.exercises.ex08.TrimFilter;

import java.util.LinkedList;
import java.util.List;

public class Exercise {

    public static void run() {

        // Load data from CSV
        System.out.println("Put your data file in: " + System.getProperty("user.dir"));
        String filePath = "data/exercises/";

        List<String> badWords = ProfanityLoader.loadFromCsv(filePath + "profane_words.csv");
        TextFilter blacklistFilter = new ProfanityFilter(badWords);

        List<TextFilter> filters = new LinkedList<>();
        filters.add(new TrimFilter());
        filters.add(new LowercaseFilter());
        filters.add(blacklistFilter);

        String input = "   This is SOME foo bar text and I dont give a DOODLE who reads it, you doodle!   ";
        String output = TextFilters.applyAll(filters, input);

        System.out.println("Original: [" + input + "]");
        System.out.println("Filtered: [" + output + "]");
    }
}
