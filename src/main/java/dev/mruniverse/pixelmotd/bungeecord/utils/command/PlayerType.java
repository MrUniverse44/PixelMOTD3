package dev.mruniverse.pixelmotd.bungeecord.utils.command;

import dev.mruniverse.pixelmotd.global.enums.ListPath;

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

    public ListPath getListPath() {
        switch (this) {
            case ID:
                return ListPath.UUIDS;
            default:
            case UNKNOWN:
            case PLAYER:
                return ListPath.PLAYERS;

        }
    }

    public String getUnknownType(String paramString) {
        if(paramString.contains("-")) {
            return "UUID";
        }
        return "Player";
    }

    public static PlayerType fromUnknown(String paramString) {
        if(paramString.contains("-")) {
            return PlayerType.ID;
        }
        return PlayerType.PLAYER;
    }
}

