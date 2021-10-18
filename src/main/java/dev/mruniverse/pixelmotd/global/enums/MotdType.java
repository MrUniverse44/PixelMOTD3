package dev.mruniverse.pixelmotd.global.enums;

public enum MotdType {
    NORMAL,
    WHITELIST,
    OUTDATED_SERVER,
    OUTDATED_CLIENT;

    public String getMotdPath() {
        switch (this){
            case WHITELIST:
                return "whitelist.motds.";
            case OUTDATED_SERVER:
                return "outdated-server.motds.";
            case OUTDATED_CLIENT:
                return "outdated-client.motds.";
            default:
            case NORMAL:
                return "normal.motds.";
        }
    }

    public String getSettings(MotdSettings settings) {
        switch (settings) {
            case CUSTOM_PROTOCOL_VERSION_TOGGLE:
                return getPath() + "settings.custom-protocol.change-protocol-version.toggle";
            case CUSTOM_PROTOCOL_TOGGLE:
                return getPath() + "settings.custom-protocol.enable";
            case CUSTOM_PROTOCOL_VALUE:
                return getPath() + "settings.custom-protocol.change-protocol-version.value";
            case CUSTOM_PROTOCOL_NAME:
                return getPath() + "settings.custom-protocol.name";
            default:
            case ICON_SYSTEM:
                return getPath() + "settings.icon";
        }
    }

    public String getEmergencyPath() {
        switch (this){
            case WHITELIST:
                return "emergency.whitelist";
            case OUTDATED_SERVER:
                return "emergency.outdatedServer";
            case OUTDATED_CLIENT:
                return "emergency.outdatedClient";
            default:
            case NORMAL:
                return "emergency.normal";
        }
    }

    public IconFolders getIconFolder() {
        switch (this){
            case WHITELIST:
                return IconFolders.WHITELIST;
            case OUTDATED_SERVER:
                return IconFolders.OUTDATED_SERVER;
            case OUTDATED_CLIENT:
                return IconFolders.OUTDATED_CLIENT;
            default:
            case NORMAL:
                return IconFolders.NORMAL;
        }
    }

    public String getMotdsUsingPath() {
        switch (this){
            case WHITELIST:
                return "whitelist.motds";
            case OUTDATED_SERVER:
                return "outdated-server.motds";
            case OUTDATED_CLIENT:
                return "outdated-client.motds";
            default:
            case NORMAL:
                return "normal.motds";
        }
    }

    public String getName() {
        switch (this) {
            case WHITELIST:
                return "whitelist";
            case OUTDATED_CLIENT:
                return "outdatedClient";
            case OUTDATED_SERVER:
                return "outdatedServer";
            default:
            case NORMAL:
                return "normal";
        }
    }

    public String getPath() {
        switch (this){
            case WHITELIST:
                return "whitelist.";
            case OUTDATED_SERVER:
                return "outdated-server.";
            case OUTDATED_CLIENT:
                return "outdated-client.";
            default:
            case NORMAL:
                return "normal.";
        }
    }
}
