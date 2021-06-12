package dev.mruniverse.pixelmotd.spigot.listener;

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
