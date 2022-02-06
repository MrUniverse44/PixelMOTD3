package dev.mruniverse.pixelmotd.global;

public enum Priority {
    HIGHEST,
    HIGH,
    NORMAL,
    LOW,
    LOWEST,
    MONITOR;

    public static Priority getFromText(String text) {
        try {
            return Priority.valueOf(text.toUpperCase());
        }catch (IllegalArgumentException ignored) {
            return Priority.HIGH;
        }
    }
}
