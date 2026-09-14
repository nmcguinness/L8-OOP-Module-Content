package t09_interface.challenges.ce02;

/**
 * Wraps the email in a simple mailto: HTML link.
 */
public class EmailHtmlFormatter implements IFormatter {

    @Override
    public Contact format(Contact input) {
        String email = input.getEmail();
        if (email == null || email.isBlank()) {
            return input;
        }

        String trimmed = email.trim();
        String html = "<a href=\"mailto:" + trimmed + "\">" + trimmed + "</a>";
        return input.withEmail(html);
    }
}
