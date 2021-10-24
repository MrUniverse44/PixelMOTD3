package dev.mruniverse.pixelmotd.global.enums;

import dev.mruniverse.pixelmotd.global.FileStorage;
import dev.mruniverse.pixelmotd.global.utils.Folder;

import java.io.File;

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

    public static File getIconFolderFromText(FileStorage storage, String text,MotdType motdType,String motd) {
        if(!text.equalsIgnoreCase("DEFAULT") && !text.equalsIgnoreCase("MAIN_FOLDER")) return storage.getIconsFolder(motdType,motd);
        if(text.equalsIgnoreCase("DEFAULT")) return storage.getIconsFolder(motdType);
        return storage.getMainIcons();
    }

}
