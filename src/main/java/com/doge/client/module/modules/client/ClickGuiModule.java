package com.doge.client.module.modules.client;

import com.doge.api.setting.settings.BooleanSetting;
import com.doge.api.setting.settings.ColorSetting;
import com.doge.api.setting.settings.ModeSetting;
import com.doge.api.setting.settings.NumberSetting;
import com.doge.api.util.color.DColor;
import com.doge.client.Main;
import com.doge.client.manager.ModuleManager;
import com.doge.client.module.Category;
import com.doge.client.module.Module;
import org.lwjgl.input.Keyboard;

public class ClickGuiModule extends Module {
    public NumberSetting opacity = new NumberSetting("Opacity", this, 150, 50, 255, 1);
    public NumberSetting scrollSpeed = new NumberSetting("ScrollSpeed", this, 10, 1, 20, 1);
    public ColorSetting outlineColor = new ColorSetting("Outline Color", this, new DColor(48, 87, 126, 255));
    public ColorSetting enabledColor = new ColorSetting("Enabled Color", this, new DColor(61, 103, 149, 255));
    public ColorSetting backgroundColor = new ColorSetting("Background Color", this, new DColor(23, 23, 51, 255));
    public ColorSetting settingBackgroundColor = new ColorSetting("Setting Color", this, new DColor(23, 23, 51, 255));
    public ColorSetting fontColor = new ColorSetting("Font Color", this, new DColor(255, 255, 255, 255));
    public NumberSetting animationSpeed = new NumberSetting("Animation Speed", this, 200, 0, 1000, 1);
    public ModeSetting scrolling = new ModeSetting("Scrolling", this, "Screen", "Screen", "Container");
    public BooleanSetting showHUD = new BooleanSetting("Show HUD Panels", this, false);
    public ModeSetting theme = new ModeSetting("Skin", this, "Gamesense Theme", "Gamesense Theme", "Clear Gradient Theme", "Clear Theme");

    public static ClickGuiModule INSTANCE = new ClickGuiModule();

    public ClickGuiModule() {
        super("Click GUI", "HUD to show all the modules.", Category.CLIENT);
        this.key = Keyboard.KEY_RSHIFT;
        this.addSetting(opacity, scrollSpeed, outlineColor, enabledColor, backgroundColor, settingBackgroundColor, fontColor, animationSpeed, scrolling, showHUD, theme);
    }

    @Override
    public void onEnable() {
        Main.INSTANCE.dogeGUI.enterGUI();
    }

    @Override
    public void onDisable() {

    }

    @Override
    public void onUpdate() {
        if (mc.player == null || mc.world == null) { return; }
        if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
            this.setToggled(false);
        }

        if (ModuleManager.getModuleByName("HUD Editor").isOn()) {
            this.setToggled(false);
        }
    }
}
