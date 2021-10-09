package com.doge.client.module.modules.movement;

import com.doge.api.setting.settings.NumberSetting;
import com.doge.client.module.Category;
import com.doge.client.module.Module;
import org.lwjgl.input.Keyboard;

public class Step extends Module {

    public NumberSetting stepHeight = new NumberSetting("Step Height", this, 2, 1, 5, 1);

    public Step() {
        super("Step", "Changes your step height.", Category.MOVEMENT);
        this.addSetting(stepHeight);
    }

    @Override
    public void onUpdate() {
        if (mc.player == null || mc.world == null) {
            return;
        }

        mc.player.stepHeight = (float) stepHeight.getNumber();
    }

    @Override
    public void onDisable() {
        mc.player.stepHeight = 0.5f;
    }

    @Override
    public void onEnable() {

    }
}
