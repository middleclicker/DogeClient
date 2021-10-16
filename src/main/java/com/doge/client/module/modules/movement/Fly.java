package com.doge.client.module.modules.movement;

import com.doge.api.setting.settings.ModeSetting;
import com.doge.api.setting.settings.NumberSetting;
import com.doge.client.module.Category;
import com.doge.client.module.Module;

public class Fly extends Module {
    // Very very basic fly hack, no bypasses whatsoever.
    // TODO: Add more modes.

    public ModeSetting mode = new ModeSetting("Mode", this, "Creative", "Creative");
    public NumberSetting speed = new NumberSetting("Speed", this, 10, 1, 20, 1);

    public Fly() {
        super("Fly", "Weeeeeeeeeeeeee!", Category.MOVEMENT);
        this.addSetting(mode, speed);
    }

    @Override
    public void onEnable() {
        if (mc.player == null || mc.world == null || mc.player.capabilities.isCreativeMode) {
            return;
        }
        if (mode.getValueName().equals("Creative")) {
            mc.player.capabilities.setFlySpeed((float) (speed.getNumber() / 100f));
            mc.player.capabilities.isFlying = true;
            mc.player.capabilities.allowFlying = true;
        }
    }

    @Override
    public void onDisable() {
        if (mc.player.capabilities.isCreativeMode) { return; }
        if (mode.getValueName().equals("Creative")) {
            mc.player.capabilities.setFlySpeed(0.05f);
            mc.player.capabilities.isFlying = false;
            mc.player.capabilities.allowFlying = false;
        }
    }
}
