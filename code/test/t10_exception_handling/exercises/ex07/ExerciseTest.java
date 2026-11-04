package t10_exception_handling.exercises.ex07;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("t10 ex07 - a custom AutoCloseable resource")
class ExerciseTest {

    @Test
    void runSession_normal_closesAfterLastEvent() {
        List<String> sink = new ArrayList<>();

        Exercise.runSession(sink, false);

        assertEquals(List.of("opened", "login", "logout", "closed"), sink);
    }

    @Test
    void runSession_exceptionMidway_stillCloses_andExceptionPropagates() {
        List<String> sink = new ArrayList<>();

        assertThrows(IllegalStateException.class, () -> Exercise.runSession(sink, true));

        assertEquals(List.of("opened", "login", "closed"), sink);
    }

    @Test
    void auditLog_writeAfterClose_throws() {
        AuditLog log = new AuditLog(new ArrayList<>());
        log.close();

        assertTrue(log.isClosed());
        assertThrows(IllegalStateException.class, () -> log.write("late"));
    }

    @Test
    void auditLog_closeTwice_recordsCloseOnce() {
        List<String> sink = new ArrayList<>();
        AuditLog log = new AuditLog(sink);

        log.close();
        log.close();

        assertEquals(List.of("opened", "closed"), sink);
    }

    @Test
    void flakyResource_closeFailure_isSuppressed_notLost() {
        RuntimeException e = Exercise.useFlakyResource();

        assertEquals("work failed", e.getMessage());
        assertEquals(1, e.getSuppressed().length);
        assertEquals("close failed", e.getSuppressed()[0].getMessage());
    }
}
