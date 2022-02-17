package dev.mruniverse.pixelmotd.commons.enums;

import dev.mruniverse.pixelmotd.commons.Motd;

public enum MotdType implements Motd {
    NORMAL("motds.","Normal", IconFolders.NORMAL, false),
    NORMAL_HEX("motds-hex.","Normal", IconFolders.NORMAL, true),
    WHITELIST("whitelist.","Whitelist", IconFolders.WHITELIST, false),
    WHITELIST_HEX("whitelist-hex.","Whitelist", IconFolders.WHITELIST, true),
    BLACKLIST("blacklist.","Blacklist", IconFolders.GENERAL, false),
    OUTDATED_SERVER("outdated-server.","outdatedServer", IconFolders.OUTDATED_SERVER, false),
    OUTDATED_CLIENT("outdated-client.","outdatedClient", IconFolders.OUTDATED_CLIENT, false);

    private final String path;
    private final String name;
    private final IconFolders folder;
    private final boolean isHexMotd;

    MotdType(String path, String name, IconFolders folder, boolean isHexMotd) {
        this.path = path;
        this.name = name;
        this.folder = folder;
        this.isHexMotd = isHexMotd;
    }

    public String motd = "";
    public boolean hasMotd = false;

    public void setMotd(String motd) {
        this.motd = motd;
        hasMotd = true;
    }

    @Override
    public String getPath() {
        return this.path;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getSettings(MotdSettings settings) {
        if (hasMotd) {
            return path + motd + settings.getPath();
        }
        return settings.getPath();
    }

    @Override
    public IconFolders getIconFolders() {
        return this.folder;
    }

    @Override
    public boolean isHexMotd() {
        return this.isHexMotd;
    }
}
