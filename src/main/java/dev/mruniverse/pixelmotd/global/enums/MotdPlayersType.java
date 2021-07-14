package dev.mruniverse.pixelmotd.global.enums;

public enum MotdPlayersType {
    MAX,
    ONLINE;


    public String getPath(MotdType motdType) {
        switch (this) {
            case MAX:
                return motdType.getPath() + "settings.custom-online.";
            default:
            case ONLINE:
                return motdType.getPath() + "settings.custom-max.";
        }
    }
}
