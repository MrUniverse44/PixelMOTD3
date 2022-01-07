package dev.mruniverse.pixelmotd.global.shared;

import dev.mruniverse.pixelmotd.global.Control;
import dev.mruniverse.pixelmotd.global.enums.PluginVersion;

public class ConfigVersion {

    public static int CURRENT_CODE = 3;

    private PluginVersion currentVersion;

    private boolean work = true;

    private Control control;

    public ConfigVersion(Control control) {
        this.control = control;
        load();
    }

    public void setControl(Control control) {
        this.control = control;
        load();
    }

    public boolean isUpdated() {
        return currentVersion.isNewest(CURRENT_CODE);
    }

    private void load() {
        currentVersion = PluginVersion.getFromCode(control.getInt("settings.config-version",0));
        if(isUpdated() && !isWork()) {
            work = true;
        }
        if(!isUpdated() && isWork()) {
            work = false;
        }
    }

    public void setWork(boolean work) {
        this.work = work;
    }

    public boolean isWork() {
        return work;
    }

}
