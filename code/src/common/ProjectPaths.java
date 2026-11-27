package common;

import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Finds files that live in the {@code code} folder, such as {@code data/ce02/contacts_hr.csv}
 * or {@code config/db.properties}, wherever the program was started from.
 *
 * <p>IntelliJ runs a program from the project root, so a relative path such as
 * {@code data/ce02/} is looked up in the wrong folder. A terminal or Maven usually runs it
 * from the {@code code} folder, where the same path works. This class checks which of the
 * two applies and adds {@code code/} to the front of the path when it is needed.
 */
public final class ProjectPaths {

    /** "" when the program runs inside the code folder, "code/" when it runs from the root. */
    private static final String PREFIX = findPrefix();

    private ProjectPaths() {
    }

    private static String findPrefix() {
        if (Files.isDirectory(Path.of("src")) && Files.isDirectory(Path.of("data"))) {
            return "";
        }
        if (Files.isDirectory(Path.of("code", "src"))) {
            return "code/";
        }
        return "";
    }

    /**
     * Returns a path inside the code folder as a String, for example
     * {@code path("data/ce02/")} gives {@code "code/data/ce02/"} when run from the root.
     * A trailing "/" is kept, so a file name can be added to the end.
     */
    public static String path(String pathInCodeFolder) {
        return PREFIX + pathInCodeFolder;
    }

    /** Returns a path inside the code folder as a {@link Path}. */
    public static Path resolve(String pathInCodeFolder) {
        return Path.of(path(pathInCodeFolder));
    }
}
