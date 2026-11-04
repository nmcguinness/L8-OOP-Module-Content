package t10_exception_handling.exercises.ex07;

import java.util.ArrayList;
import java.util.List;

public class Exercise {

    // Writes two events; if failMidway is true, throws between them
    public static void runSession(List<String> sink, boolean failMidway) {
        try (AuditLog log = new AuditLog(sink)) {
            log.write("login");
            if (failMidway) {
                throw new IllegalStateException("session crashed");
            }
            log.write("logout");
        }
    }

    // Returns the exception that escapes, so its suppressed list can be inspected
    public static RuntimeException useFlakyResource() {
        try (FlakyResource r = new FlakyResource()) {
            r.work();
            return null;
        } catch (RuntimeException e) {
            return e;
        }
    }

    public static void run() {
        List<String> ok = new ArrayList<>();
        runSession(ok, false);
        System.out.println("Normal session: " + ok);

        List<String> crashed = new ArrayList<>();
        try {
            runSession(crashed, true);
        } catch (IllegalStateException e) {
            System.out.println("Crashed session: " + crashed + " (" + e.getMessage() + ")");
        }

        RuntimeException e = useFlakyResource();
        System.out.println("Primary: " + e.getMessage());
        for (Throwable s : e.getSuppressed()) {
            System.out.println("Suppressed: " + s.getMessage());
        }
    }
}
