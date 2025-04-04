package paypals.util;

import java.io.File;
import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Logging {
    private static final String loggerDir = "./log";
    private static final String loggerFile = "./log/PayPals.log";
    private static final Logger paypalsLogger = Logger.getLogger("PayPals");

    public Logging() {
        try {
            File f = new File(loggerDir);
            if (!f.exists()) {
                f.mkdir();
            }
            f = new File(loggerFile);
            if (!f.exists()) {
                f.createNewFile();
            }

            FileHandler handler = new FileHandler(loggerFile);
            handler.setFormatter(new SimpleFormatter());
            paypalsLogger.addHandler(handler);
            paypalsLogger.setUseParentHandlers(false);
            disableConsoleLogging();
        } catch (IOException e) {
            paypalsLogger.log(Level.WARNING, "Log data will not saved");
        }
    }

    /**
     * Disables console logging by setting the level of any {@link ConsoleHandler} attached to the logger
     * to {@code Level.OFF}.
     * This ensures that log messages are only written to the file and not printed to the console.
     */
    public static void disableConsoleLogging() {
        Handler[] handlers = paypalsLogger.getHandlers();
        for (Handler handler : handlers) {
            if (handler instanceof ConsoleHandler) {
                handler.setLevel(Level.OFF);
            }
        }
    }

    /**
     * Logs a message at the specified logging level.
     *
     * @param level   the logging level (e.g., {@code Level.INFO}, {@code Level.WARNING})
     * @param message the message to be logged
     */
    public static void log(Level level, String message) {
        paypalsLogger.log(level, message);
    }

    /**
     * Logs an informational message.
     *
     * @param message the informational message to be logged
     */
    public static void logInfo(String message) {
        paypalsLogger.log(Level.INFO, message);
    }

    /**
     * Logs a warning message.
     *
     * @param message the warning message to be logged
     */
    public static void logWarning(String message) {
        paypalsLogger.log(Level.WARNING, message);
    }
}
