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
    public BooleanSetting textFont = new BooleanSetting("Custom Text", this, false);
    public ModeSetting friendColor = new ModeSetting("Friend Color", this, "Blue", "Black", "Dark Green", "Dark Red", "Gold", "Dark Gray", "Green", "Red", "Yellow", "Dark Blue", "Dark Aqua", "Dark Purple", "Gray", "Blue", "Aqua", "Light Purple", "White");
    public ModeSetting enemyColor = new ModeSetting("Enemy Color", this, "Red","Black", "Dark Green", "Dark Red", "Gold", "Dark Gray", "Green", "Red", "Yellow", "Dark Blue", "Dark Aqua", "Dark Purple", "Gray", "Blue", "Aqua", "Light Purple", "White");
    public ModeSetting chatEnableColor = new ModeSetting("Msg Enbl", this, "Green", "Black", "Dark Green", "Dark Red", "Gold", "Dark Gray", "Green", "Red", "Yellow", "Dark Blue", "Dark Aqua", "Dark Purple", "Gray", "Blue", "Aqua", "Light Purple", "White");
    public ModeSetting chatDisableColor = new ModeSetting("Msg Dsbl", this, "Red", "Black", "Dark Green", "Dark Red", "Gold", "Dark Gray", "Green", "Red", "Yellow", "Dark Blue", "Dark Aqua", "Dark Purple", "Gray", "Blue", "Aqua", "Light Purple", "White");
    public ModeSetting colorModel = new ModeSetting("Color Model", this, "HSB", "HSB", "RGB");

    public ColorMain() {
        super("Colors", "World of colors.", CLIENT);
        this.addSetting(customFont, textFont, friendColor, enemyColor, chatEnableColor, chatDisableColor, colorModel);
    }

    @Override
    public void onEnable() {
        this.toggle();
    }

    public TextFormatting getFriendColor() {
        return ColorUtil.settingToTextFormatting(friendColor);
    }

    public TextFormatting getEnemyColor() {
        return ColorUtil.settingToTextFormatting(enemyColor);
    }

    public TextFormatting getEnabledColor() {
        return ColorUtil.settingToTextFormatting(chatEnableColor);
    }

    public TextFormatting getDisabledColor() {
        return ColorUtil.settingToTextFormatting(chatDisableColor);
    }

    public DColor getFriendGSColor() {
        return new DColor(ColorUtil.settingToColor(friendColor));
    }

    public DColor getEnemyGSColor() {
        return new DColor(ColorUtil.settingToColor(enemyColor));
    }
}
