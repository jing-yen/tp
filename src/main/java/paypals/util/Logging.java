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
    private static Logger paypalsLogger = Logger.getLogger("PayPals");

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

    public static void disableConsoleLogging() {
        Handler[] handlers = paypalsLogger.getHandlers();
        for (Handler handler : handlers) {
            if (handler instanceof ConsoleHandler) {
                handler.setLevel(Level.OFF);
            }
        }
    }

    public static void log(Level level, String message) {
        paypalsLogger.log(level, message);
    }

    public static void logInfo(String message) {
        paypalsLogger.log(Level.INFO, message);
    }

    public static void logWarning(String message) {
        paypalsLogger.log(Level.WARNING, message);
    }
}
