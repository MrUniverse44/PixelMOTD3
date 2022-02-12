package dev.mruniverse.pixelmotd.commons.enums;

public enum ListMode {
    CONTAINS,
    NAMES;

    private String key;

    public String getKey() {
        return this.key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public static ListMode getFromText(String key, String text) {
        ListMode mode;
        if (text.contains("CONTAIN")) {
            mode = ListMode.CONTAINS;
        } else {
            mode = ListMode.NAMES;
        }
        mode.setKey(key);
        return mode;
    }
}
