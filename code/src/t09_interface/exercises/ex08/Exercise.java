package t09_interface.exercises.ex08;

import java.util.LinkedList;
import java.util.List;

public class Exercise {

    public static void run() {
        List<TextFilter> filters = new LinkedList<>();
        filters.add(new TrimFilter());
        filters.add(new LowercaseFilter());
        filters.add(new RegexReplacementFilter("(\\d{7,})","<b>$1</b>"));

        String input = "   This is MY work number 0429470200, call me before 5PM   ";
        String output = TextFilters.applyAll(filters, input);

        System.out.println("Original: [" + input + "]");
        System.out.println("Filtered: [" + output + "]");
    }
}
