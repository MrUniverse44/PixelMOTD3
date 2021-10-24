package dev.mruniverse.pixelmotd.global.enums;

import dev.mruniverse.pixelmotd.global.Settings;

public enum MotdSettings implements Settings {
    LINE1{
        @Override
        public String getPath() {
            return ".line1";
        }
    },
    LINE2{
        @Override
        public String getPath() {
            return ".line2";
        }
    },
    HOVER_TOGGLE{
        @Override
        public String getPath() {
            return ".hover.toggle";
        }
    },
    HOVER_MORE_PLAYERS{
        @Override
        public String getPath() {
            return ".hover.hasMoreOnline";
        }
    },
    HOVER_LINES{
        @Override
        public String getPath() {
            return ".hover.lines";
        }
    },
    ICONS_FOLDER{
        @Override
        public String getPath() {
            return ".icons.folder";
        }
    },
    ICONS_ICON{
        @Override
        public String getPath() {
            return ".icons.icon";
        }
    },
    PROTOCOL_TOGGLE{
        @Override
        public String getPath() {
            return ".protocol.toggle";
        }
    },
    PROTOCOL_MODIFIER{
        @Override
        public String getPath() {
            return ".protocol.modifier";
        }
    },
    PROTOCOL_MESSAGE{
        @Override
        public String getPath() {
            return ".protocol.message";
        }
    },
    PLAYERS_MAX_TOGGLE{
        @Override
        public String getPath() {
            return ".players.max.toggle";
        }
    },
    PLAYERS_MAX_TYPE{
        @Override
        public String getPath() {
            return ".players.max.type";
        }
    },
    PLAYERS_MAX_VALUES{
        @Override
        public String getPath() {
            return ".players.max.values";
        }
    },
    PLAYERS_MAX_SINGLE_VALUE{
        @Override
        public String getPath() {
            return ".players.max.single-value";
        }
    },
    PLAYERS_ONLINE_TOGGLE{
        @Override
        public String getPath() {
            return ".players.online.toggle";
        }
    },
    PLAYERS_ONLINE_TYPE{
        @Override
        public String getPath() {
            return ".players.online.type";
        }
    },
    PLAYERS_ONLINE_VALUES{
        @Override
        public String getPath() {
            return ".players.online.values";
        }
    },
    PLAYERS_ONLINE_SINGLE_VALUE{
        @Override
        public String getPath() {
            return ".players.online.single-value";
        }
    };

    public static MotdSettings getValuePath(MotdPlayersMode mode,boolean isMaxPlayerPath) {
        if(isMaxPlayerPath) {
            if (mode.equals(MotdPlayersMode.VALUES)) return PLAYERS_MAX_VALUES;
            return PLAYERS_MAX_SINGLE_VALUE;
        }
        if (mode.equals(MotdPlayersMode.VALUES)) return PLAYERS_ONLINE_VALUES;
        return PLAYERS_ONLINE_SINGLE_VALUE;
    }
}
