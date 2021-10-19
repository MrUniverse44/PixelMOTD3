package dev.mruniverse.pixelmotd.global.enums;

import dev.mruniverse.pixelmotd.global.Message;

@SuppressWarnings("unused")
public enum MessagePath implements Message {
    ALREADY_WHITELISTED{
        @Override
        public String getPath() {
            return "messages.not-whitelisted";
        }
    },
    ALREADY_BLACKLISTED{
        @Override
        public String getPath() {
            return "messages.already-blacklisted";
        }
    },
    BLACKLIST_TOGGLE_ON{
        @Override
        public String getPath() {
            return "messages.blacklist-enabled";
        }
    },
    BLACKLIST_TOGGLE_OFF{
        @Override
        public String getPath() {
            return "messages.blacklist-disabled";
        }
    },
    BLACKLIST_PLAYER_ADD{
        @Override
        public String getPath() {
            return "messages.blacklist-player-add";
        }
    },
    BLACKLIST_PLAYER_REMOVE{
        @Override
        public String getPath() {
            return "messages.blacklist-player-remove";
        }
    },
    NO_PERMISSION{
        @Override
        public String getPath() {
            return "messages.no-perms";
        }
    },
    RELOAD{
        @Override
        public String getPath() {
            return "messages.reload";
        }
    },
    WHITELIST_TOGGLE_ON{
        @Override
        public String getPath() {
            return "messages.whitelist-enabled";
        }
    },
    WHITELIST_TOGGLE_OFF{
        @Override
        public String getPath() {
            return "messages.whitelist-disabled";
        }
    },
    WHITELIST_PLAYER_ADD{
        @Override
        public String getPath() {
            return "messages.whitelist-player-add";
        }
    },
    WHITELIST_PLAYER_REMOVE{
        @Override
        public String getPath() {
            return "messages.whitelist-player-remove";
        }
    },
    NOT_WHITELISTED{
        @Override
        public String getPath() {
            return "messages.not-whitelisted";
        }
    },
    NOT_BLACKLISTED{
        @Override
        public String getPath() {
            return "messages.not-blacklisted";
        }
    };


}
