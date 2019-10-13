package host.linox.tinyadditions.Logging;

public enum DebugLevel {

    ERRORS(0),
    LOW_LEVEL(1),
    HIGH_LEVEL(2);

    private final int level;

    DebugLevel(final int level) {
        this.level = level;
    }

    public boolean isHigher(final DebugLevel debugLevel) {
        return debugLevel.level <= level;
    }
}
