package com.doge.client.module.modules.movement;

import com.doge.api.setting.settings.NumberSetting;
import com.doge.client.module.Category;
import com.doge.client.module.Module;

public class ReverseStep extends Module {
    public NumberSetting height = new NumberSetting("Height", this, 2, 0.5, 2.5, 0.5);

    public ReverseStep() {
        super("Reverse Step", "Step down from blocks.", Category.MOVEMENT);
        this.addSetting(height);
    }

    @Override
    public void onUpdate() {
        if (mc.world == null || mc.player == null || mc.player.isInWater() || mc.player.isInLava() || mc.player.isOnLadder() || mc.gameSettings.keyBindJump.isKeyDown() || !mc.player.onGround) {
            return;
        }

        for (double y = 0.0; y < this.height.getNumber() + 0.5; y += 0.01) {
            if (!mc.world.getCollisionBoxes(mc.player, mc.player.getEntityBoundingBox().offset(0.0, -y, 0.0)).isEmpty()) {
                mc.player.motionY = -10.0;
                break;
            }
        }
    }
}
