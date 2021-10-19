package dev.mruniverse.pixelmotd.global;

import dev.mruniverse.pixelmotd.global.enums.FileSaveMode;
import dev.mruniverse.pixelmotd.global.enums.GuardianFiles;
import dev.mruniverse.pixelmotd.global.enums.MotdType;

import java.io.File;

public interface FileStorage {

    File getIconsFolder(MotdType motdType);

    File getIconsFolder(MotdType motdType, String motdName);

    File getMainIcons();

    File getFile(GuardianFiles fileToGet);

    Control getControl(GuardianFiles file);

    void checkIconFolder();

    void setMessages(String code);

    void reloadFile(FileSaveMode mode);

    void save(FileSaveMode mode);
}


