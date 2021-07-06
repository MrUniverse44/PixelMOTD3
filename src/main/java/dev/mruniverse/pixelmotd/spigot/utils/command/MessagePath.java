package dev.mruniverse.pixelmotd.spigot.utils.command;

public enum MessagePath {
    ALREADY_WHITELISTED,
    ALREADY_BLACKLISTED,
    BLACKLIST_TOGGLE_ON,
    BLACKLIST_TOGGLE_OFF,
    BLACKLIST_PLAYER_ADD,
    BLACKLIST_PLAYER_REMOVE,
    NO_PERMISSION,
    RELOAD,
    WHITELIST_TOGGLE_ON,
    WHITELIST_TOGGLE_OFF,
    WHITELIST_PLAYER_ADD,
    NOT_WHITELISTED,
    NOT_BLACKLISTED,
    WHITELIST_PLAYER_REMOVE;

    public String getPath() {
        switch (this) {
            case NOT_WHITELISTED:
                return "messages.not-whitelisted";
            case NOT_BLACKLISTED:
                return "messages.not-blacklisted";
            case NO_PERMISSION:
                return "messages.no-perms";
            case ALREADY_BLACKLISTED:
                return "messages.already-blacklisted";
            case ALREADY_WHITELISTED:
                return "messages.already-whitelisted";
            case BLACKLIST_TOGGLE_ON:
                return "messages.blacklist-enabled";
            case WHITELIST_TOGGLE_ON:
                return "messages.whitelist-enabled";
            case BLACKLIST_PLAYER_ADD:
                return "messages.blacklist-player-add";
            case BLACKLIST_TOGGLE_OFF:
                return "messages.blacklist-disabled";
            case WHITELIST_PLAYER_ADD:
                return "messages.whitelist-player-add";
            case WHITELIST_TOGGLE_OFF:
                return "messages.whitelist-disabled";
            case BLACKLIST_PLAYER_REMOVE:
                return "messages.blacklist-player-remove";
            case WHITELIST_PLAYER_REMOVE:
                return "messages.whitelist-player-remove";
            default:
            case RELOAD:
                return "messages.reload";
        }
    }


}
