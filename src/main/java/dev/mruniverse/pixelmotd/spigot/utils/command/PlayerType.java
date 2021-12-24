package dev.mruniverse.pixelmotd.spigot.utils.command;

public enum PlayerType {
    PLAYER("Player"),
    UNKNOWN("Unknown"),
    ID("UUID");

    private final String name;

    private String value = "";

    PlayerType(String name) {
        this.name = name;
    }

    public PlayerType setPlayer(String value) {
        this.value = value;
        return this;
    }

    public String getValue() {
        return value;
    }

    public String getName() {
        return this.name;
    }

    public static PlayerType fromUnknown(String paramString) {
        if(paramString.contains("-")) {
            return PlayerType.ID;
        }
        return PlayerType.PLAYER;
    }

}
