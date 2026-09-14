package t14_design_patterns_2.exercises.ex05;

public class Exercise {
    public static void run() {
        LegacyLogger legacy = new LegacyLogger();
        Logger logger = new LegacyLoggerAdapter(legacy);

        logger.log("Server started");
        logger.log("Player connected");
    }
}

interface Logger {
    void log(String msg);
}

class LegacyLogger {
    public void logMessage(String msg) {
        System.out.println("LEGACY: " + msg);
    }
}

class LegacyLoggerAdapter implements Logger {
    private LegacyLogger _legacy;

    public LegacyLoggerAdapter(LegacyLogger legacy) {
        if (legacy == null)
            throw new IllegalArgumentException("legacy is null.");

        _legacy = legacy;
    }

    @Override
    public void log(String msg) {
        _legacy.logMessage(msg);
    }
}
