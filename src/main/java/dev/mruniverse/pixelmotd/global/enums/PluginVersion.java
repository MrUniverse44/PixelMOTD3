package dev.mruniverse.pixelmotd.global.enums;

import dev.mruniverse.pixelmotd.global.utils.Config;

public enum PluginVersion implements Config {
    OLDER{
        @Override
        public int getVersionID() {
            return 0;
        }
    },
    V9_1_0 {
        @Override
        public int getVersionID() {
            return 1;
        }
    };

    private int code = 1;

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
}
