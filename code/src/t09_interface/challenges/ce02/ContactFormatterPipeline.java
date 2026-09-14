package t09_interface.challenges.ce02;

import java.util.ArrayList;
import java.util.List;

/**
 * Applies each IFormatter in order to a Contact.
 */
public class ContactFormatterPipeline {

    private List<IFormatter> formatters;

    public ContactFormatterPipeline(List<IFormatter> formatters) {

        // copy the formatters into this internal this.formatters list so we dont corrupt the originals
        this.formatters = new ArrayList<IFormatter>(formatters);
    }

    public Contact apply(Contact input) {
        Contact current = input;
        for (IFormatter formatter : formatters) {
            current = formatter.format(current);
        }
        return current;
    }
}
