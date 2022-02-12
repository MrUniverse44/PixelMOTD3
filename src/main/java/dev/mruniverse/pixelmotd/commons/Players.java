package dev.mruniverse.pixelmotd.commons;

import dev.mruniverse.pixelmotd.commons.enums.MotdSettings;
import dev.mruniverse.pixelmotd.commons.enums.MotdType;

public interface Players {
    int execute(Control control, MotdType motdType, MotdSettings path, int value);
}
