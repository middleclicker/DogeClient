package com.doge.client.module.modules.misc;

import com.doge.api.setting.settings.BooleanSetting;
import com.doge.api.setting.settings.ModeSetting;
import com.doge.api.setting.settings.NumberSetting;
import com.doge.client.module.Category;
import com.doge.client.module.Module;

public class YawLock extends Module {

    public BooleanSetting lockYaw = new BooleanSetting("Lock Yaw", this, true);
    public NumberSetting directionYaw = new NumberSetting("Direction Yaw", this, 0, -180, 180, 1);
    public BooleanSetting lockPitch = new BooleanSetting("Lock Pitch", this, false);
    public NumberSetting directionPitch = new NumberSetting("Direction Pitch", this, 0, -180, 180, 1);

    public YawLock() {
        super("Yaw Lock", "Locks your yaw.", Category.MISC);
        this.addSetting(lockYaw, directionYaw, lockPitch, directionPitch);
    }

    @Override
    public void onUpdate() {
        if (lockYaw.isOn()) {
            mc.player.rotationYaw = (float) directionYaw.getNumber();
            mc.player.rotationYawHead = (float) directionYaw.getNumber();
        }
        if (lockPitch.isOn()) {
            mc.player.rotationPitch = (float) directionPitch.getNumber();
        }
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }
}
