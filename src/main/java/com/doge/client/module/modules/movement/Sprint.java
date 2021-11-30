package com.doge.client.module.modules.movement;

import com.doge.api.setting.settings.ModeSetting;
import com.doge.client.module.Category;
import com.doge.client.module.Module;
import net.minecraft.client.settings.KeyBinding;

public class Sprint extends Module {

    public ModeSetting mode = new ModeSetting("Mode", this, "Vanilla", "Vanilla", "Keep", "Multi Directional");

    public Sprint() {
        super("Sprint", "Makes your sprint.", Category.MOVEMENT);
        this.addSetting(mode);
    }

    @Override
    public void onUpdate() {
        if (mc.player == null || mc.world == null) {
            return;
        }
        if (mode.getValueName().equals("Vanilla")) {
            KeyBinding.setKeyBindState(mc.gameSettings.keyBindSprint.getKeyCode(), true);
        } else if (mode.getValueName().equals("Keep")) {
            mc.player.setSprinting(true);
        }
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {
        if (mode.getValueName().equals("Vanilla")) {
            KeyBinding.setKeyBindState(mc.gameSettings.keyBindSprint.getKeyCode(), false);
        } else {
            mc.player.setSprinting(false);
        }
    }
}
