package dev.mruniverse.pixelmotd.global.enums;

public enum MotdProtocol {
    ALWAYS_POSITIVE,
    ALWAYS_NEGATIVE(-1),
    DEFAULT;

    MotdProtocol() {
        code = 0;
    }

    MotdProtocol(int code) {
        this.code = code;
    }

    private int code;

    public MotdProtocol setCode(int code) {
        this.code = code;
        return this;
    }

    public int getCode() {
        return code;
    }

    public static MotdProtocol getFromText(String paramText,int code) {
        if (paramText.contains("POSITIVE")) return ALWAYS_POSITIVE.setCode(code);
        if (paramText.contains("NEGATIVE")) return ALWAYS_NEGATIVE.setCode(code);
        return DEFAULT.setCode(code);
    }
}
