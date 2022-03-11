package dev.mruniverse.pixelmotd.commons.enums;

import dev.mruniverse.pixelmotd.commons.Control;
import dev.mruniverse.pixelmotd.commons.Players;

import java.util.List;

public enum MotdPlayersMode implements Players {
    ADD {
        @Override
        public int execute(Control control, MotdType motdType, MotdSettings path, int value) {
            int singleValue = control.getInt(motdType.getSettings(path));
            return value + singleValue;
        }
    },
    ADD_MIDDLE {
        @Override
        public int execute(Control control, MotdType motdType, MotdSettings path, int value) {
            int calc;
            if (value >= 2) {
                calc = value / 2;
            } else {
                calc = 0;
            }
            return value + calc;
        }
    },
    ADD_DOUBLE{
        @Override
        public int execute(Control control, MotdType motdType, MotdSettings path, int value) {
            return value + value;
        }
    },
    REMOVE {
        @Override
        public int execute(Control control, MotdType motdType, MotdSettings path, int value) {
            int singleValue = control.getInt(motdType.getSettings(path));
            return value - singleValue;
        }
    },
    REMOVE_MIDDLE {
        @Override
        public int execute(Control control, MotdType motdType, MotdSettings path, int value) {
            int calc;
            if (value >= 2) {
                calc = value / 2;
            } else {
                calc = 0;
            }
            return value - calc;
        }
    },
    VALUES{
        @Override
        public int execute(Control control, MotdType motdType, MotdSettings path, int value) {
            List<Integer> values = control.getIntList(motdType.getSettings(path));
            return values.get(control.getRandom().nextInt(values.size()));
        }
    },
    SINGLE_VALUE{
        @Override
        public int execute(Control control, MotdType motdType, MotdSettings path, int value) {
            return control.getInt(motdType.getSettings(path));
        }
    },
    DEFAULT{
        @Override
        public int execute(Control control, MotdType motdType, MotdSettings path, int value) {
            return value;
        }
    },
    EQUALS{
        @Override
        public int execute(Control control, MotdType motdType, MotdSettings path, int value) {
            return value;
        }
    };

    public static MotdPlayersMode getModeFromText(String paramText) {
        paramText = paramText.toLowerCase();
        switch (paramText) {
            case "equal":
                return EQUALS;
            case "default":
                return DEFAULT;
            case "add_middle":
                return ADD_MIDDLE;
            case "remove_middle":
                return REMOVE_MIDDLE;
            case "add_double":
                return ADD_DOUBLE;
            case "add":
                return ADD;
            case "remove":
                return REMOVE;
            case "values":
                return VALUES;
            default:
            case "single":
            case "single_value":
                return SINGLE_VALUE;

        }
    }
}
