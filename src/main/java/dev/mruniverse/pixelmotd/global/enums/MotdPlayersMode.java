package dev.mruniverse.pixelmotd.global.enums;

public enum MotdPlayersMode {
    //ADD-<number>,REMOVE-<number>, VALUES, ADD_MIDDLE, REMOVE_MIDDLE, EQUALS
    ADD,
    ADD_MIDDLE,
    REMOVE,
    REMOVE_MIDDLE,
    VALUES,
    EQUALS;

    public String getReplace() {
        switch (this) {
            case ADD:
                return "ADD-";
            case REMOVE:
                return "REMOVE-";
            default:
                return "";
        }
    }

    public static MotdPlayersMode getModeFromText(String paramText) {
        paramText = paramText.toUpperCase();
        if(paramText.contains("EQUALS")) return MotdPlayersMode.EQUALS;
        if(paramText.contains("ADD-")) return MotdPlayersMode.ADD;
        if(paramText.contains("REMOVE-")) return MotdPlayersMode.REMOVE;
        if(paramText.contains("VALUES")) return MotdPlayersMode.VALUES;
        if(paramText.contains("ADD_")) return MotdPlayersMode.ADD_MIDDLE;
        return MotdPlayersMode.REMOVE_MIDDLE;

    }
}
