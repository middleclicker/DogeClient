package com.doge.api.setting.settings;

import com.doge.api.setting.Setting;
import com.doge.api.setting.Type;
import com.doge.client.module.Module;
import com.lukflug.panelstudio.settings.EnumSetting;

import java.util.Arrays;
import java.util.List;

public class ModeSetting extends Setting implements EnumSetting {
    public int index;

    public List<String> modes;

    public ModeSetting(String name, Module parent, String defaultMode, String... modes) {
        this.name = name;
        this.parent = parent;
        this.modes = Arrays.asList(modes);
        this.index = this.modes.indexOf(defaultMode);
        this.type = Type.MODE;
    }

    public boolean is(String mode) {
        return (this.index == this.modes.indexOf(mode));
    }

    public void setMode(String mode) {
        this.index = this.modes.indexOf(mode);
    }

    @Override
    public void increment() {
        if (this.index < this.modes.size() - 1) {
            this.index++;
        } else {
            this.index = 0;
        }
    }

    @Override
    public String getValueName() {
        return this.modes.get(this.index);
    }
}
