package com.doge.api.setting.settings;

import com.doge.api.setting.Setting;
import com.doge.api.setting.Type;
import org.lwjgl.input.Keyboard;

public class KeybindSetting extends Setting implements com.lukflug.panelstudio.settings.KeybindSetting {
    public int code;

    public KeybindSetting(String name, int code) {
        this.name = name;
        this.code = code;
        this.type = Type.KEYBIND;
    }

    @Override
    public int getKey() {
        return code;
    }

    @Override
    public void setKey(int key) {
        this.code = key;
    }

    @Override
    public String getKeyName() {
        return Keyboard.getKeyName(code);
    }
}
