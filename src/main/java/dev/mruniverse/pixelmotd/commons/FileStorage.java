package dev.mruniverse.pixelmotd.commons;

import dev.mruniverse.pixelmotd.commons.enums.FileSaveMode;
import dev.mruniverse.pixelmotd.commons.enums.GuardianFiles;
import dev.mruniverse.pixelmotd.commons.enums.MotdType;

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


