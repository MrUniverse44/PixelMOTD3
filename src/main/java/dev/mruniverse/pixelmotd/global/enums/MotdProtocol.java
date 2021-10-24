package dev.mruniverse.pixelmotd.global.enums;

public enum MotdProtocol {
    ALWAYS_POSITIVE,
    ALWAYS_NEGATIVE,
    DEFAULT;

    public static MotdProtocol getFromText(String paramText) {
        if(paramText.contains("POSITIVE")) return ALWAYS_POSITIVE;
        if(paramText.contains("NEGATIVE")) return ALWAYS_NEGATIVE;
        return DEFAULT;
    }
}
