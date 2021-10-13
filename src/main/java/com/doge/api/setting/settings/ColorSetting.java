package com.doge.api.setting.settings;

import com.doge.api.setting.Setting;
import com.doge.api.setting.Type;
import com.doge.api.util.color.DColor;
import com.doge.client.module.Module;

import java.awt.*;

public class ColorSetting extends Setting implements com.lukflug.panelstudio.settings.ColorSetting {
    private boolean rainbow;
    private DColor value;

    public ColorSetting (String name, Module parent, final DColor value) {
        this.name = name;
        this.parent = parent;
        this.value = value;
        this.type = Type.COLOR;
    }

    public DColor getValue() {
        if (rainbow) {
            float[] tickColor = {
                    (System.currentTimeMillis() % (360 * 32)) / (360f * 32)
            };
            int colorRgbO = DColor.HSBtoRGB(tickColor[0], 1, 1);
            return new DColor((colorRgbO >> 16) & 0xFF, (colorRgbO >> 8) & 0xFF, colorRgbO & 0xFF);
        }
        return this.value;
    }

    public boolean getRainb() {
        return this.rainbow;
    }

    public void setRainb(boolean rainb) {
        this.rainbow = rainb;
    }

    public static int rainbow(int delay) {
        float[] tickColor = {
                (System.currentTimeMillis() % (360 * 32)) / (360f * 32)
        };
        int colorRgbO = Color.HSBtoRGB(tickColor[0], 1, 1);
        return new Color((colorRgbO >> 16) & 0xFF, (colorRgbO >> 8) & 0xFF, colorRgbO & 0xFF).getRGB();
    }

    public void setValue(boolean rainbow, final DColor value) {
        this.rainbow = rainbow;
        this.value = value;
    }

    public long toInteger() {
        return this.value.getRGB() & (0xFFFFFFFF);
    }

    public void fromInteger (long number) {
        this.value = new DColor(Math.toIntExact(number & 0xFFFFFFFF),true);
    }

    public DColor getColor() {
        return this.value;
    }

    @Override
    public boolean getRainbow() {
        return this.rainbow;
    }

    @Override
    public void setValue(Color value) {
        setValue(getRainbow(),new DColor(value));
    }

    @Override
    public void setRainbow(boolean rainbow) {
        this.rainbow = rainbow;
    }
}
