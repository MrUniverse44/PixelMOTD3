package dev.mruniverse.pixelmotd.commons;

public enum Priority {
    HIGHEST,
    HIGH,
    NORMAL,
    LOW,
    LOWEST,
    MONITOR,
    FIRST,
    EARLY,
    LATE,
    LAST;

    /**
     * Get the priority for motd ping or whitelist / blacklist listeners.
     * @return Priority
     **/
    public static Priority getFromText(String text) {
        try {
            return Priority.valueOf(text.toUpperCase());
        }catch (IllegalArgumentException ignored) {
            return Priority.HIGH;
        }
    }
}
