package t08_inheritance.exercises.ex06;

public class FancyFormatter extends BaseFormatter {

    @Override
    public String format() {
        return "Fancy format";
    }

    // Overloaded convenience method with a prefix parameter.
    public String format(String prefix) {
        return prefix + " " + format();
    }

}
