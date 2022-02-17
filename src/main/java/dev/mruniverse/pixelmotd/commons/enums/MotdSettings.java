package dev.mruniverse.pixelmotd.commons.enums;

import dev.mruniverse.pixelmotd.commons.Settings;

public enum MotdSettings implements Settings {
    LINE1(".line1"),
    LINE2(".line2"),
    HOVER_TOGGLE(".hover.toggle"),
    HOVER_MORE_PLAYERS(".hover.hasMoreOnline"),
    HOVER_LINES(".hover.lines"),
    ICONS_FOLDER(".icons.folder"),
    ICONS_ICON(".icons.icon"),
    PROTOCOL_TOGGLE(".protocol.toggle"),
    PROTOCOL_MODIFIER(".protocol.modifier"),
    PROTOCOL_MESSAGE(".protocol.message"),
    PLAYERS_MAX_TOGGLE(".players.max.toggle"),
    PLAYERS_MAX_TYPE(".players.max.type"),
    PLAYERS_MAX_VALUES(".players.max.values"),
    PLAYERS_MAX_SINGLE_VALUE(".players.max.single-value"),
    PLAYERS_ONLINE_TOGGLE(".players.online.toggle"),
    PLAYERS_ONLINE_TYPE(".players.online.type"),
    PLAYERS_ONLINE_VALUES(".players.online.values"),
    PLAYERS_ONLINE_SINGLE_VALUE(".players.online.single-value");

    private final String path;

    MotdSettings(String path) {
        this.path = path;
    }

    @Override
    public String getPath() {
        return path;
    }

    public static MotdSettings getValuePath(MotdPlayersMode mode, boolean isMaxPlayerPath) {
        if (isMaxPlayerPath) {
            if (mode.equals(MotdPlayersMode.VALUES)) {
                return PLAYERS_MAX_VALUES;
            }
            return PLAYERS_MAX_SINGLE_VALUE;
        }
        if (mode.equals(MotdPlayersMode.VALUES)) {
            return PLAYERS_ONLINE_VALUES;
        }
        return PLAYERS_ONLINE_SINGLE_VALUE;
    }
}
