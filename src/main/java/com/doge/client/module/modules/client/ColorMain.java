package com.doge.client.module.modules.client;

import com.doge.api.setting.settings.BooleanSetting;
import com.doge.api.setting.settings.ModeSetting;
import com.doge.api.util.color.ColorUtil;
import com.doge.api.util.color.DColor;
import com.doge.client.module.Module;
import net.minecraft.util.text.TextFormatting;

import static com.doge.client.module.Category.CLIENT;

public class ColorMain extends Module {
    public BooleanSetting customFont = new BooleanSetting("Custom Font", this, true);
    public ModeSetting colorModel = new ModeSetting("Color Model", this, "RGB", "HSB", "RGB");

    public static ColorMain INSTANCE = new ColorMain();

    public ColorMain() {
        super("Colors", "World of colors.", CLIENT);
        this.addSetting(customFont, colorModel);
    }

    @Override
    public void onEnable() {
        this.toggle();
    }
}
