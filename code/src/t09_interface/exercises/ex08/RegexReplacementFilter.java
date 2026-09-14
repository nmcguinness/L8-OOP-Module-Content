package t09_interface.exercises.ex08;

public class RegexReplacementFilter implements TextFilter {

    private final String regex;
    private final String replacement;
    public RegexReplacementFilter(String regex, String replacement)
    {
        this.regex = regex;
        this.replacement = replacement;
    }
    @Override
    public String apply(String input) {
        if (input == null) {
            return null;
        }
        return input.replaceAll(regex, replacement);
       // return input.replaceAll("(\\d{7,})", "<u>$1</u>");
    }
}
