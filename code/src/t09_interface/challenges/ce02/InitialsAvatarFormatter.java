package t09_interface.challenges.ce02;

/**
 * Adds a simple avatar span with initials before the name.
 */
public class InitialsAvatarFormatter implements IFormatter {

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

        String initials;
        if (parts.length == 1) {
            initials = parts[0].substring(0, 1).toUpperCase();
        } else {
            initials = (parts[0].substring(0, 1)
                    + parts[parts.length - 1].substring(0, 1)).toUpperCase();
        }

        String newName = "<span class=\"avatar\">" + initials + "</span> " + trimmed;
        return input.withName(newName);
    }
}
