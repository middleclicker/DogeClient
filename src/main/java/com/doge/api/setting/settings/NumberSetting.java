package com.doge.api.setting.settings;

import com.doge.api.setting.Setting;
import com.doge.api.setting.Type;
import com.doge.client.Main;
import com.doge.client.module.Module;

public class NumberSetting extends Setting implements com.lukflug.panelstudio.settings.NumberSetting {
    public double value;
    public double minimum;
    public double maximum;
    public double increment;

    public NumberSetting(String name, Module parent, double value, double minimum, double maximum, double increment) {
        this.name = name;
        this.parent = parent;
        this.value = value;
        this.minimum = minimum;
        this.maximum = maximum;
        this.increment = increment;
        this.type = Type.NUMBER;
    }

    @Override
    public double getNumber() {
        return value;
    }

    @Override
    public void setNumber(double value) {
        this.value = value;
        if(Main.SAVELOAD_CONFIG != null) {
            Main.SAVELOAD_CONFIG.save();
        }
    }

    public double getValue() {
        return value;
    }

    @Override
    public double getMaximumValue() {
        return maximum;
    }

    @Override
    public double getMinimumValue() {
        return minimum;
    }

    @Override
    public int getPrecision() {
        return 1;
    }
}
