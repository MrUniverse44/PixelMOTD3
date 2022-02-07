package dev.mruniverse.pixelmotd.bungeecord.utils.command;

public enum PlayerType {
    PLAYER,
    UNKNOWN,
    ID;

    private String value = "";

    public PlayerType setPlayer(String value) {
        this.value = value;
        return this;
    }

    public String getValue() {
        return value;
    }

    public String getName() {
        switch (this) {
            case ID:
                return "UUID";
            case UNKNOWN:
                return "Unknown";
            default:
            case PLAYER:
                return "Player";

        }
    }

    public static PlayerType fromUnknown(String paramString) {
        if (paramString.contains("-")) {
            return PlayerType.ID;
        }
        return PlayerType.PLAYER;
    }
}

