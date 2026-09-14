package t09_interface.exercises.ex08;

import java.util.List;

public final class TextFilters {

    private TextFilters() {
    }

    public static String applyAll(List<TextFilter> filters, String text) {
        String result = text;
        for (TextFilter f : filters) {
            result = f.apply(result);
        }
        return result;
    }
}
