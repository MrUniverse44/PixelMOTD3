package dev.mruniverse.pixelmotd.global.enums;

import dev.mruniverse.pixelmotd.global.utils.Folder;

public enum IconFolders implements Folder {
    GENERAL {
        @Override
        public String getName() {
            return "icons";
        }
    },
    NORMAL {
        @Override
        public String getName() {
            return "normal";
        }
    },
    WHITELIST {
        @Override
        public String getName() {
            return "whitelist";
        }
    },
    OUTDATED_SERVER {
        @Override
        public String getName() {
            return "outdatedServer";
        }
    },
    OUTDATED_CLIENT {
        @Override
        public String getName() {
            return "outdatedClient";
        }
    };

}
