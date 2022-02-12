package dev.mruniverse.pixelmotd.commons;

import dev.mruniverse.pixelmotd.commons.enums.IconFolders;
import dev.mruniverse.pixelmotd.commons.enums.MotdSettings;

public interface Motd {

    String getPath();

    String getName();

    String getSettings(MotdSettings settings);

    IconFolders getIconFolders();

    boolean isHexMotd();

}
