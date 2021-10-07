package com.doge.client.module;

import com.doge.api.Global;
import com.doge.api.setting.Setting;
import com.doge.api.setting.settings.BooleanSetting;
import com.doge.api.setting.settings.KeybindSetting;
import com.doge.client.Main;
import com.doge.client.manager.SettingManager;
import com.lukflug.panelstudio.settings.Toggleable;
import net.minecraftforge.common.MinecraftForge;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class Module implements Toggleable, Global, com.lukflug.panelstudio.settings.KeybindSetting {
    public static ArrayList<Setting> settings = new ArrayList<>();

    public String name, description;
    public int key;
    public Category category;
    public boolean toggled;

    public KeybindSetting keyCode = new KeybindSetting("Keybind", 0);
    public BooleanSetting hidden = new BooleanSetting("Hidden", this, false);

    public Module(String name, String description, Category category) {
        super();
        this.name = name;
        this.key = 0;
        this.description = description;
        this.category = category;
        this.toggled = false;
        this.addSetting(hidden);
    }

    @Override
    public void toggle() {
        toggled = !toggled;
        if (toggled) {
            onEnable();
        } else {
            onDisable();
        }
    }

    @Override
    public boolean isOn() {
        return toggled;
    }

    public void onEnable() {
        MinecraftForge.EVENT_BUS.register(this);
        Main.EVENT_BUS.subscribe(this);
    }

    public void onDisable() {
        MinecraftForge.EVENT_BUS.unregister(this);
        Main.EVENT_BUS.unsubscribe(this);
    }

    public void setToggled(boolean toggled) {
        this.toggled = toggled;
        if (toggled) {
            Main.EVENT_BUS.subscribe(this);
        } else {
            Main.EVENT_BUS.unsubscribe(this);
        }
    }

    public void onUpdate() {}
    public void onRender() {}

    public void addSetting(Setting... settings) {
        SettingManager.settings.addAll(Arrays.asList(settings));
        SettingManager.settings.sort(Comparator.comparingInt(s -> s == keyCode ? 1 : 0));
    }

    @Override
    public int getKey() {
        return key;
    }

    @Override
    public void setKey(int key) {
        this.key = key;
    }

    @Override
    public String getKeyName() {
        return Keyboard.getKeyName(key);
    }
}
