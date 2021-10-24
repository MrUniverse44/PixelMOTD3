package dev.mruniverse.pixelmotd.global.enums;

import dev.mruniverse.pixelmotd.global.Control;
import dev.mruniverse.pixelmotd.global.Players;

import java.util.List;

public enum MotdPlayersMode implements Players {
    ADD {
        @Override
        public int execute(Control control,MotdType motdType, MotdSettings path, int value) {
            int singleValue = control.getInt(motdType.getSettings(path));
            return value + singleValue;
        }
    },
    ADD_MIDDLE {
        @Override
        public int execute(Control control,MotdType motdType, MotdSettings path, int value) {
            int calc;
            if(value >= 2) {
                calc = value / 2;
            } else {
                calc = 0;
            }
            return value + calc;
        }
    },
    ADD_DOUBLE{
        @Override
        public int execute(Control control,MotdType motdType, MotdSettings path, int value) {
            return value + value;
        }
    },
    REMOVE {
        @Override
        public int execute(Control control,MotdType motdType, MotdSettings path, int value) {
            int singleValue = control.getInt(motdType.getSettings(path));
            return value - singleValue;
        }
    },
    REMOVE_MIDDLE {
        @Override
        public int execute(Control control,MotdType motdType, MotdSettings path, int value) {
            int calc;
            if(value >= 2) {
                calc = value / 2;
            } else {
                calc = 0;
            }
            return value - calc;
        }
    },
    VALUES{
        @Override
        public int execute(Control control,MotdType motdType, MotdSettings path, int value) {
            List<Integer> values = control.getIntList(motdType.getSettings(path));
            return values.get(control.getRandom().nextInt(values.size()));
        }
    },
    SINGLE_VALUE{
        @Override
        public int execute(Control control,MotdType motdType, MotdSettings path, int value) {
            return control.getInt(motdType.getSettings(path));
        }
    },
    EQUALS{
        @Override
        public int execute(Control control,MotdType motdType, MotdSettings path, int value) {
            return value;
        }
    };

    public static MotdPlayersMode getModeFromText(String paramText) {
        paramText = paramText.toUpperCase();
        if(paramText.contains("EQUAL") || paramText.contains("DEFAULT")) return MotdPlayersMode.EQUALS;
        if(paramText.equalsIgnoreCase("ADD_MIDDLE")) return MotdPlayersMode.ADD_MIDDLE;
        if(paramText.equalsIgnoreCase("REMOVE_MIDDLE")) return MotdPlayersMode.REMOVE_MIDDLE;
        if(paramText.equalsIgnoreCase("ADD_DOUBLE")) return MotdPlayersMode.ADD_DOUBLE;
        if(paramText.contains("ADD")) return MotdPlayersMode.ADD;
        if(paramText.contains("REMOVE")) return MotdPlayersMode.REMOVE;
        if(paramText.contains("VALUES")) return MotdPlayersMode.VALUES;
        return MotdPlayersMode.SINGLE_VALUE;
    }
}
