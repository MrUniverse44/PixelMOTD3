package dev.mruniverse.pixelmotd.global.enums;

import dev.mruniverse.pixelmotd.global.utils.GuardianFile;

public enum GuardianFiles implements GuardianFile {
    SETTINGS("settings.yml"),
    MOTDS("motds.yml"),
    WHITELIST("whitelist.yml","modes"),
    BLACKLIST("blacklist.yml","modes"),
    EVENTS("events.yml"),
    MESSAGES_EN("messages_en.yml","translations"),
    MESSAGES_ES("messages_es.yml","translations"),
    MESSAGES_PL("messages_pl.yml","translations"),
    MESSAGES_JP("messages_jp.yml","translations"),
    MESSAGES("messages_en.yml","translations"),
    EMERGENCY("emergency.yml");

    private final String file;

    private final String folderName;

    private final boolean differentFolder;

    GuardianFiles(String file,String folder) {
        this.file = file;
        this.folderName = folder;
        this.differentFolder = true;
    }

    GuardianFiles(String file) {
        this.differentFolder = false;
        this.folderName = "";
        this.file = file;
    }


    @Override
    public String getFileName() {
        return this.file;
    }

    @Override
    public boolean isInDifferentFolder() {
        return this.differentFolder;
    }

    @Override
    public String getFolderName() {
        return this.folderName;
    }
}
