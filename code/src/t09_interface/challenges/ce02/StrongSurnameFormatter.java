package t09_interface.challenges.ce02;

/**
 * Wraps the last word in the name in a <strong> tag.
 */
public class StrongSurnameFormatter implements IFormatter {

    @Override
    public Contact format(Contact input) {
        String name = input.getName();
        if (name == null || name.isBlank()) {
            return input;
        }

        String trimmed = name.trim();
        String[] parts = trimmed.split("\\s+");
        if (parts.length == 0) {
            return input;
        }

        StringBuilder sb = new StringBuilder(trimmed.length() + 16);
        for (int i = 0; i < parts.length; i++) {
            if (i > 0) {
                sb.append(' ');
            }
            if (i == parts.length - 1) {
                sb.append("<strong>").append(parts[i]).append("</strong>");
            } else {
                sb.append(parts[i]);
            }
        }
        return input.withName(sb.toString());
    }
}
