package dev.mruniverse.pixelmotd.global.enums;

import dev.mruniverse.pixelmotd.global.Message;

@SuppressWarnings("unused")
public enum MessagePath implements Message {
    ALREADY_WHITELISTED("messages.not-whitelisted"),
    ALREADY_BLACKLISTED("messages.already-blacklisted"),
    BLACKLIST_TOGGLE_ON("messages.blacklist-enabled"),
    BLACKLIST_TOGGLE_OFF("messages.blacklist-disabled"),
    BLACKLIST_PLAYER_ADD("messages.blacklist-player-add"),
    BLACKLIST_PLAYER_REMOVE("messages.blacklist-player-remove"),
    NO_PERMISSION("messages.no-perms"),
    RELOAD("messages.reload"),
    WHITELIST_TOGGLE_ON("messages.whitelist-enabled"),
    WHITELIST_TOGGLE_OFF("messages.whitelist-disabled"),
    WHITELIST_PLAYER_ADD("messages.whitelist-player-add"),
    WHITELIST_PLAYER_REMOVE("messages.whitelist-player-remove"),
    NOT_WHITELISTED("messages.not-whitelisted"),
    NOT_BLACKLISTED("messages.not-blacklisted");

    private final String path;

    MessagePath(String path) {
        this.path = path;
    }

    @Override
    public String getPath() {
        return path;
    }


}
