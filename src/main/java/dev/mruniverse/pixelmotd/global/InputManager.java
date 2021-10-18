package dev.mruniverse.pixelmotd.global;

import java.io.InputStream;

public interface InputManager {

    InputStream getInputStream(String resource);

    boolean isBungee();
}
