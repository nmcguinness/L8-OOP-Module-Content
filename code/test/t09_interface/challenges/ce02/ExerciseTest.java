package t09_interface.challenges.ce02;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("t09 ce02 - contact formatter pipeline")
class ExerciseTest {

    private static Contact contact(String name, String email) {
        return new Contact("C1", name, email, "123", "Sales", "IE", "Acme", false, 10);
    }

    @Test
    void constructor_blankEmail_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> contact("Ann", "   "));
        assertThrows(IllegalArgumentException.class, () -> contact("Ann", null));
    }

    @Test
    void equality_isBasedOnTheNormalisedEmail() {
        assertEquals(contact("Ann", "A@Example.COM"), contact("Different Name", "a@example.com"));
        assertNotEquals(contact("Ann", "a@example.com"), contact("Ann", "b@example.com"));
    }

    @Test
    void equalContacts_collapseInAHashSet() {
        Set<Contact> set = new HashSet<>();
        set.add(contact("Ann", "A@Example.COM"));
        set.add(contact("Ann", "a@example.com"));
        assertEquals(1, set.size(), "equal contacts must share a hash bucket");
    }

    @Test
    void getEmailDomain_returnsThePartAfterTheAt() {
        assertEquals("example.com", contact("Ann", "ann@Example.com").getEmailDomain());
    }

    @Test
    void getEmailDomain_addressWithNoDomain_returnsEmptyString() {
        assertEquals("", contact("Ann", "no-at-sign").getEmailDomain());
    }

    @Test
    void trimAndNormaliseFormatter_trimsNameAndLowercasesEmail() {
        Contact out = new TrimAndNormaliseFormatter().format(contact("  Ann  ", "  A@Example.COM  "));
        assertEquals("Ann", out.getName());
        assertEquals("a@example.com", out.getEmail());
    }

    @Test
    void initialsAvatarFormatter_usesFirstAndLastInitials() {
        Contact out = new InitialsAvatarFormatter().format(contact("ann marie bloggs", "a@b.com"));
        assertTrue(out.getName().startsWith("<span class=\"avatar\">AB</span> "), out.getName());
    }

    @Test
    void initialsAvatarFormatter_singleWordName_usesOneInitial() {
        Contact out = new InitialsAvatarFormatter().format(contact("cher", "a@b.com"));
        assertTrue(out.getName().startsWith("<span class=\"avatar\">C</span> "), out.getName());
    }

    @Test
    void pipeline_appliesFormattersInOrder() {
        ContactFormatterPipeline pipeline = new ContactFormatterPipeline(
                List.of(new TrimAndNormaliseFormatter(), new InitialsAvatarFormatter()));

        Contact out = pipeline.apply(contact("  ann bloggs  ", "  A@Example.COM  "));

        assertEquals("a@example.com", out.getEmail());
        assertTrue(out.getName().startsWith("<span class=\"avatar\">AB</span> "), out.getName());
    }

    @Test
    void pipeline_emptyFormatterList_returnsTheInputUnchanged() {
        Contact in = contact("Ann", "a@example.com");
        assertEquals(in.getName(), new ContactFormatterPipeline(List.of()).apply(in).getName());
    }

    @Test
    void pipeline_copiesItsFormatterListSoLaterChangesDoNotAffectIt() {
        java.util.ArrayList<IFormatter> formatters = new java.util.ArrayList<>();
        formatters.add(new TrimAndNormaliseFormatter());

        ContactFormatterPipeline pipeline = new ContactFormatterPipeline(formatters);
        formatters.add(new InitialsAvatarFormatter());   // must not reach the pipeline

        Contact out = pipeline.apply(contact("  ann bloggs  ", "a@b.com"));

        assertEquals("ann bloggs", out.getName(), "the avatar formatter was added after construction");
    }
}
