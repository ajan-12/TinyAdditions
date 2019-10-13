package host.linox.tinyadditions.Logging;

import org.bukkit.plugin.Plugin;

import java.util.logging.Level;
import java.util.logging.Logger;

public final class Logging {

    private static Logger LOGGER = null;
    private static DebugLevel debugLevel = null;

    public Logging(final Plugin plugin) {
        if (LOGGER == null) LOGGER = plugin.getLogger();

        final int level = plugin.getConfig().getInt("settings.debug-level", 0);
        switch (level) {
            case 1:
                debugLevel = DebugLevel.LOW_LEVEL;
                break;
            case 2:
                debugLevel = DebugLevel.HIGH_LEVEL;
                break;
            default:
                debugLevel = DebugLevel.ERRORS;
                break;
        }
    }

    /**
     * Logs down the given debug messages.
     *
     * @param debugLevel Debug level of the messages.
     * @param level      Level of the log messages.
     * @param logs       Debug messages to log down.
     */
    public static void debug(final DebugLevel debugLevel, final Level level, final String... logs) {
        if (Logging.debugLevel.isHigher(debugLevel)) {
            log(level, logs);
        }
    }

    /**
     * Logs down the given log messages.
     *
     * @param level Level of the log messages.
     * @param logs  Log messages to log down.
     */
    public static void log(final Level level, final String... logs) {
        if (logs.length == 0) return;
        for (final String log : logs) {
            LOGGER.log(level, log);
        }
    }

    /**
     * Logs down the given log message with the given args placed into them.
     *
     * @param level Level of the log message.
     * @param log   Log message to log down.
     * @param args  Arguments in order to place into the log message.
     */
    public static void logArgs(final Level level, final String log, final Object... args) {
        if (args.length == 0) return;
        LOGGER.log(level, String.format(log, args));
    }

    /**
     * Logs down the given exception with the given message.
     *
     * @param exception The exception to log down.
     * @param messages  Messages to log down together with the exception.
     */
    public static void logException(final Exception exception, final String... messages) {
        Logging.log(Level.SEVERE, messages);
        Logging.logArgs(Level.SEVERE, "Please report this on: %s", "https://github.com/ajan-12/TinyAdditions");
        Logging.logArgs(Level.SEVERE, "Detailed stacktrace: %s", exception.toString());
    }

    public static void dump() {
        debug(DebugLevel.HIGH_LEVEL, Level.INFO, "Dumping Logging fields.");
        LOGGER = null;
        debugLevel = null;
    }
}
