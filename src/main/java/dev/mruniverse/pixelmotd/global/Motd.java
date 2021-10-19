package dev.mruniverse.pixelmotd.global;

import dev.mruniverse.pixelmotd.global.enums.IconFolders;
import dev.mruniverse.pixelmotd.global.enums.MotdSettings;

public interface Motd {

    String getPath();

    String getName();

    String getSettings(MotdSettings settings);

    IconFolders getIconFolders();

    boolean isHexMotd();

}
