package t09_interface.exercises.ex07;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("t09 ex07 - XmlSerializable round trip")
class ExerciseTest {

    @Test
    void toXml_producesTheExpectedElement() {
        PlayerProfile p = new PlayerProfile("Ash", 120);
        assertEquals("<player name=\"Ash\" score=\"120\" />", p.toXml());
    }

    @Test
    void fromXml_readsBothAttributes() {
        PlayerProfile p = PlayerProfile.fromXml("<player name=\"Misty\" score=\"75\" />");
        assertEquals("Misty", p.getName());
        assertEquals(75, p.getScore());
    }

    @Test
    void roundTrip_preservesNameAndScore() {
        PlayerProfile original = new PlayerProfile("Brock", 42);
        PlayerProfile restored = PlayerProfile.fromXml(original.toXml());

        assertEquals(original.getName(), restored.getName());
        assertEquals(original.getScore(), restored.getScore());
    }

    @Test
    void toXml_isReachableThroughTheInterface() {
        XmlSerializable s = new PlayerProfile("Ash", 1);
        assertTrue(s.toXml().startsWith("<player "));
    }

    @Test
    void fromXml_missingAttribute_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> PlayerProfile.fromXml("<player score=\"10\" />"));
    }

    @Test
    void fromXml_nonNumericScore_throwsNumberFormatException() {
        assertThrows(NumberFormatException.class,
                () -> PlayerProfile.fromXml("<player name=\"Ash\" score=\"high\" />"));
    }
}
