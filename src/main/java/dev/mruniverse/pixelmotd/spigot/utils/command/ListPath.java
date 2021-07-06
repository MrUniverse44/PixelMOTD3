package dev.mruniverse.pixelmotd.spigot.utils.command;

public enum ListPath {
    TOGGLE,
    AUTHOR,
    NAME_TOGGLE,
    NAME,
    KICK_MESSAGE,
    PLAYERS,
    UUIDS;

    public String getPath() {
        switch (this) {
            case NAME_TOGGLE:
                return "customConsoleName.toggle";
            case TOGGLE:
                return "toggle";
            case AUTHOR:
                return "author";
            case PLAYERS:
                return "players-name";
            case UUIDS:
                return "players-uuid";
            case NAME:
                return "customConsoleName.name";
            default:
            case KICK_MESSAGE:
                return "kick-message";
        }
    }
}