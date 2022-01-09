package dev.mruniverse.pixelmotd.global.enums;

import dev.mruniverse.pixelmotd.global.utils.Config;

public enum PluginVersion implements Config {
    OLDER(0),
    V9_1_0(1),
    V9_2_0(2),
    V9_2_1(2),
    V9_2_2(3),
    V9_2_3(4);

    private final int version;

    PluginVersion(int versionID) {
        this.version = versionID;
    }

    private int code = 4;

    public static PluginVersion getFromCode(int code) {
        for(PluginVersion value : PluginVersion.values()) {
            if(code == value.getVersionID()){
                return value.setCode(code);
            }
        }
        return PluginVersion.OLDER.setCode(code);
    }

    private PluginVersion setCode(int code) {
        this.code = code;
        return this;
    }

    public boolean isNewest(int currentCode) {
        return (code >= currentCode);
    }

    @Override
    public int getVersionID() {
        return this.version;
    }
}
