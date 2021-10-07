package com.doge.client.module.modules.client;

import com.doge.api.setting.settings.BooleanSetting;
import com.doge.api.setting.settings.ColorSetting;
import com.doge.api.setting.settings.ModeSetting;
import com.doge.api.setting.settings.NumberSetting;
import com.doge.api.util.color.DColor;
import com.doge.client.Main;
import com.doge.client.module.Category;
import com.doge.client.module.Module;
import org.lwjgl.input.Keyboard;

public class ClickGuiModule extends Module {
    public NumberSetting opacity = new NumberSetting("Opacity", this, 150, 50, 255, 1);
    public NumberSetting scrollSpeed = new NumberSetting("ScrollSpeed", this, 10, 1, 20, 1);
    public ColorSetting outlineColor = new ColorSetting("Outline", this, new DColor(255, 0, 0, 255));
    public ColorSetting enabledColor = new ColorSetting("Enabled", this, new DColor(255, 0, 0, 255));
    public ColorSetting backgroundColor = new ColorSetting("Background", this, new DColor(0, 0, 0, 255));
    public ColorSetting settingBackgroundColor = new ColorSetting("Setting", this, new DColor(30, 30, 30, 255));
    public ColorSetting fontColor = new ColorSetting("Font", this, new DColor(255, 255, 255, 255));
    public NumberSetting animationSpeed = new NumberSetting("AnimationSpeed", this, 200, 0, 1000, 1);
    public ModeSetting scrolling = new ModeSetting("Scrolling", this, "Screen", "Screen", "Container");
    public BooleanSetting showHUD = new BooleanSetting("Show HUD Panels", this, false);
    public ModeSetting theme = new ModeSetting("Skin", this, "Gamesense Theme", "Gamesense Theme", "Clear Gradient Theme", "Clear Theme");

    public ClickGuiModule() {
        super("Click GUI", "HUD to show all the modules.", Category.CLIENT);
        this.key = Keyboard.KEY_RSHIFT;
        this.addSetting(opacity, scrollSpeed, outlineColor, enabledColor, backgroundColor, settingBackgroundColor, fontColor, animationSpeed, scrolling, showHUD, theme);
    }

    @Override
    public void onEnable() {
        Main.INSTANCE.dogeGUI.enterGUI();
        this.toggle();
    }
}
