package dev.mruniverse.pixelmotd.spigot.motd;

import dev.mruniverse.pixelmotd.global.enums.GuardianFiles;
import dev.mruniverse.pixelmotd.global.enums.MotdPlayersMode;
import dev.mruniverse.pixelmotd.global.enums.MotdPlayersType;
import dev.mruniverse.pixelmotd.global.enums.MotdType;
import dev.mruniverse.pixelmotd.spigot.PixelMOTD;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;
import java.util.Random;

public class MotdPlayers {

    private final PixelMOTD plugin;

    private final String type;

    private final Random random = new Random();

    private String modeText;

    private MotdPlayersMode mode;

    private boolean enabled;

    private List<String> values;

    public MotdPlayers(PixelMOTD plugin, MotdType motdType, MotdPlayersType motdPlayersType) {
        this.type = motdPlayersType.getPath(motdType);
        this.plugin = plugin;
        FileConfiguration configuration = plugin.getStorage().getControl(GuardianFiles.MOTDS);
        this.modeText = configuration.getString(type + "mode","EQUALS");
        this.mode = MotdPlayersMode.getModeFromText(modeText);
        this.modeText = modeText.replace(mode.getReplace(),"");
        this.enabled = configuration.getBoolean(type + "enable");
        this.values = configuration.getStringList(type + "values");

    }

    public void update() {
        FileConfiguration configuration = plugin.getStorage().getControl(GuardianFiles.MOTDS);
        this.mode = MotdPlayersMode.getModeFromText(configuration.getString(type + "mode","EQUALS"));
        this.modeText = configuration.getString(type + "mode","EQUALS");
        this.mode = MotdPlayersMode.getModeFromText(modeText);
        this.enabled = configuration.getBoolean(type + "enable");
        this.values = configuration.getStringList(type + "values");
    }

    public MotdPlayersMode getMode() { return mode; }

    public int getResult(int value) {
        switch (mode) {
            case REMOVE:
                return value - Integer.parseInt(modeText);
            case EQUALS:
                return value;
            case ADD_MIDDLE:
                if(value > 0 ) {
                    value = value + (value/2);
                }
                return value;
            case REMOVE_MIDDLE:
                if(value > 0) {
                    value = value - (value/2);
                }
            case ADD:
                return value + Integer.parseInt(modeText);
            default:
            case VALUES:
                if(values.size() >= 1) return Integer.parseInt(values.get(random.nextInt(values.size())));
                return value;

        }
    }

    public boolean isEnabled() { return enabled; }




}

