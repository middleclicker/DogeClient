package com.doge.api.setting.settings;

import com.doge.api.setting.Setting;
import com.doge.api.setting.Type;
import com.doge.client.Main;
import com.doge.client.module.Module;
import com.lukflug.panelstudio.settings.Toggleable;

public class BooleanSetting extends Setting implements Toggleable {
    public boolean enabled;

    public BooleanSetting(String name, Module parent, boolean enabled) {
        this.name = name;
        this.parent = parent;
        this.enabled = enabled;
        this.type = Type.BOOLEAN;
    }

    @Override
    public void toggle() {
        enabled = !enabled;
        if(Main.SAVELOAD_CONFIG != null) {
            Main.SAVELOAD_CONFIG.save();
        }
    }

    @Override
    public boolean isOn() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        if(Main.SAVELOAD_CONFIG != null) {
            Main.SAVELOAD_CONFIG.save();
        }
    }
}
