package dev.mruniverse.pixelmotd.global;

import dev.mruniverse.pixelmotd.global.enums.MotdSettings;
import dev.mruniverse.pixelmotd.global.enums.MotdType;

public interface Players {
    int execute(Control control, MotdType motdType, MotdSettings path, int value);
}
