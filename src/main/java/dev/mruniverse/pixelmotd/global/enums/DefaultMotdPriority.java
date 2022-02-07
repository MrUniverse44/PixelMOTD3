package dev.mruniverse.pixelmotd.global.enums;

public enum DefaultMotdPriority {
    NORMAL (MotdType.NORMAL, MotdType.WHITELIST, MotdType.BLACKLIST),
    HEX (MotdType.NORMAL_HEX, MotdType.WHITELIST_HEX, MotdType.BLACKLIST);

    private final MotdType type;
    private final MotdType whitelist;
    private final MotdType blacklist;

    DefaultMotdPriority(MotdType type, MotdType whitelist, MotdType blacklist) {
        this.type = type;
        this.whitelist = whitelist;
        this.blacklist = blacklist;
    }

    public MotdType get(Type type) {
        switch (type) {
            case BLACKLISTED:
                return this.blacklist;
            case WHITELISTED:
                return this.whitelist;
            default:
            case DEFAULT:
                return this.type;
        }
    }

    public static DefaultMotdPriority getFromText(String verifier) {
        if (verifier.equalsIgnoreCase("HEX")) return HEX;
        return NORMAL;
    }
}
