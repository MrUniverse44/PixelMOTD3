package dev.mruniverse.pixelmotd.global.enums;

public enum MotdPlayersMode {
    ADD,
    ADD_MIDDLE,
    REMOVE,
    REMOVE_MIDDLE,
    VALUES,
    EQUALS;

    public static MotdPlayersMode getModeFromText(String paramText) {
        paramText = paramText.toUpperCase();
        if(paramText.contains("EQUALS") || paramText.contains("DEFAULT")) return MotdPlayersMode.EQUALS;
        if(paramText.contains("ADD")) return MotdPlayersMode.ADD;
        if(paramText.contains("REMOVE")) return MotdPlayersMode.REMOVE;
        if(paramText.contains("VALUES")) return MotdPlayersMode.VALUES;
        if(paramText.contains("MIDDLE")) return MotdPlayersMode.ADD_MIDDLE;
        return MotdPlayersMode.REMOVE_MIDDLE;

    }
}
