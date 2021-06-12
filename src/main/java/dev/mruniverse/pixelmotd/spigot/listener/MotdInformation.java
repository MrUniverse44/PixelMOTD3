package dev.mruniverse.pixelmotd.spigot.listener;

import com.google.inject.Inject;
import dev.mruniverse.pixelmotd.spigot.storage.Configuration;

import javax.inject.Named;
import java.util.List;

@SuppressWarnings("unused")
public class MotdInformation {

    private final boolean hoverStatus;

    private final boolean hexStatus;

    private final boolean customIconStatus;

    private final List<String> hover;

    private final String customIconName;

    private final String motdLine1;

    private final String motdLine2;

    private final String HexMotdLine1;

    private final String HexMotdLine2;

    private final String currentMotdName;

    private final MotdType currentMotdType;

    private final int max;

    private final int min;

    @Inject
    @Named("motds")
    private Configuration motds;

    @Inject
    @Named("events")
    private Configuration events;

    @Inject
    @Named("whitelist")
    private Configuration whitelist;

    @SuppressWarnings("ConstantConditions")
    public MotdInformation(MotdType motdType, String motdName,int max,int online){
        this.min = online;
        this.max = max;
        currentMotdType = motdType;
        currentMotdName = motdName;
        HexMotdLine1 = motds.getString(getPath(MotdPaths.HEX_LINE1));
        HexMotdLine2 = motds.getString(getPath(MotdPaths.HEX_LINE2));
        motdLine1 = motds.getString(getPath(MotdPaths.LINE1));
        motdLine2 = motds.getString(getPath(MotdPaths.LINE1));
        hoverStatus = motds.getBoolean(getPath(MotdPaths.HOVER_STATUS));
        hover = motds.getColoredList(getPath(MotdPaths.HOVER));
        customIconStatus = motds.getBoolean(getPath(MotdPaths.CUSTOM_ICON_STATUS));
        hexStatus = motds.getBoolean(getPath(MotdPaths.HEX_STATUS));
        customIconName = motds.getString(getPath(MotdPaths.CUSTOM_ICON_NAME));
    }

    public boolean getCustomIconStatus() { return customIconStatus; }

    public boolean getHexStatus() { return hexStatus; }

    public boolean getHoverStatus() { return hoverStatus; }

    public MotdType getMotdType() { return currentMotdType; }

    public List<String> getHover() { return hover; }

    public String getCustomIconName() { return customIconName; }

    public String getHexMotdLine1() { return HexMotdLine1; }

    public String getHexMotdLine2() { return HexMotdLine2; }

    public String getAllMotd() {
        return motdLine1 + "\n" + motdLine2;
    }

    public String getHexAllMotd() {
        return HexMotdLine1 + "\n" + HexMotdLine2;
    }

    public int getMax() { return max; }

    public int getMin() { return min; }

    public String getMotdLine1() { return motdLine1; }

    public String getMotdLine2() { return motdLine2; }

    public String getMotdName() { return currentMotdName; }

    public String getPath(MotdPaths motdPaths) {
        String complete = currentMotdType.getMotdPath() + currentMotdName + ".";
        switch (motdPaths) {
            case HOVER:
                return complete + "hover.message";
            case HOVER_STATUS:
                return complete + "hover.toggle";
            case HEX_STATUS:
                return complete + "with-hex.enable";
            case HEX_LINE1:
                return complete + "with-hex.line-1";
            case HEX_LINE2:
                return complete + "with-hex.line-2";
            case LINE2:
                return complete + "line-2";
            case CUSTOM_ICON_STATUS:
                return complete + "custom-icon.enable";
            case CUSTOM_ICON_NAME:
                return complete + "custom-icon.name";
            default:
            case LINE1:
                return complete + "line-1";
        }
    }

}
