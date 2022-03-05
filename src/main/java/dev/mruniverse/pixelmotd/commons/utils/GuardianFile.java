package dev.mruniverse.pixelmotd.commons.utils;

public interface GuardianFile {
    String getFileName();

    boolean isInDifferentFolder();

    String getFolderName();

    String getPath();
}
