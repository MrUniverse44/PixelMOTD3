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

    public static File fromText(FileStorage storage,MotdType motdType) {
        return storage.getIconsFolder(motdType);
    }
}
