package dev.mruniverse.pixelmotd.global;

public interface GLogger {

    void error(String message);

    void error(Throwable throwable);

    void warn(String message);

    void debug(String message);

    void info(String message);

    void sendMessage(String message);
}


