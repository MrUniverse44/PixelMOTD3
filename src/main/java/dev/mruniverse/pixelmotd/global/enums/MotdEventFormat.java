package dev.mruniverse.pixelmotd.global.enums;

public enum MotdEventFormat {
    FIRST,
    SECOND,
    THIRD;

    public static MotdEventFormat getFromText(String paramText) {
        if(paramText.contains("FIRST") || paramText.contains("1") ) return MotdEventFormat.FIRST;
        if(paramText.contains("SECOND") || paramText.contains("2") ) return MotdEventFormat.SECOND;
        return MotdEventFormat.THIRD;
    }
}
