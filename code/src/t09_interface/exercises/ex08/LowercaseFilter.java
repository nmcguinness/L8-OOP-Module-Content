package t09_interface.exercises.ex08;

public class LowercaseFilter implements TextFilter {

    @Override
    public String apply(String input) {
        if (input == null) {
            return null;
        }
        return input.toLowerCase();
    }
}
