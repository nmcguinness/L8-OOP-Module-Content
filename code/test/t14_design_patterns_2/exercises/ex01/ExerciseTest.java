package t14_design_patterns_2.exercises.ex01;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("t14 e01 - Factory chosen by file extension")
class ExerciseTest {

    @Test
    void createFor_csv_returnsACsvParser() {
        assertInstanceOf(CsvParser.class, new ParserFactory().createFor("csv"));
    }

    @Test
    void createFor_json_returnsAJsonParser() {
        assertInstanceOf(JsonParser.class, new ParserFactory().createFor("json"));
    }

    @Test
    void createFor_ignoresCaseAndSurroundingSpace() {
        assertInstanceOf(CsvParser.class, new ParserFactory().createFor("  CSV  "));
    }

    @Test
    void createFor_unknownExtension_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new ParserFactory().createFor("xml"));
    }

    @Test
    void createFor_nullOrBlank_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new ParserFactory().createFor(null));
        assertThrows(IllegalArgumentException.class, () -> new ParserFactory().createFor("   "));
    }

    @Test
    void csvParser_countsNonBlankLines() {
        assertEquals(3, new CsvParser().parseCount("a,b,c\n1,2,3\n4,5,6\n"));
    }

    @Test
    void csvParser_skipsBlankAndWhitespaceOnlyLines() {
        assertEquals(2, new CsvParser().parseCount("a,b\n\n   \n1,2\n"));
    }

    @Test
    void jsonParser_countsOpeningBraces() {
        assertEquals(2, new JsonParser().parseCount("{ \"id\": 1 }\n{ \"id\": 2 }\n"));
    }

    @Test
    void parsers_nullInput_returnZero() {
        assertEquals(0, new CsvParser().parseCount(null));
        assertEquals(0, new JsonParser().parseCount(null));
    }

    @Test
    void callerDependsOnlyOnTheParserInterface() {
        // The factory hides which concrete class is in use.
        Parser parser = new ParserFactory().createFor("csv");
        assertEquals(1, parser.parseCount("only one line"));
    }
}
