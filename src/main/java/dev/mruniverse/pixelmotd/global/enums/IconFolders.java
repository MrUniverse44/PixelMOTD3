package dev.mruniverse.pixelmotd.global.enums;

import dev.mruniverse.pixelmotd.global.FileStorage;
import dev.mruniverse.pixelmotd.global.utils.Folder;

import java.io.File;

public enum IconFolders implements Folder {
    GENERAL("icons"),
    NORMAL("normal"),
    WHITELIST("whitelist"),
    OUTDATED_SERVER("outdatedServer"),
    OUTDATED_CLIENT("outdatedClient");

    private final String name;

    IconFolders(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    public static File getIconFolderFromText(FileStorage storage, String text,MotdType motdType,String motd) {
        if(!text.equalsIgnoreCase("DEFAULT") && !text.equalsIgnoreCase("MAIN_FOLDER")) return storage.getIconsFolder(motdType,motd);
        if(text.equalsIgnoreCase("DEFAULT")) return storage.getIconsFolder(motdType);
        return storage.getMainIcons();
    }

}
