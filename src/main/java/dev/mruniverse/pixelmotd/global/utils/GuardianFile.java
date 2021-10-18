package dev.mruniverse.pixelmotd.global.utils;

public interface GuardianFile {
    String getFileName();

    boolean isInDifferentFolder();

    String getFolderName();
}
