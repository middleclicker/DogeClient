package com.doge.client.module.modules.render;

import com.doge.api.setting.settings.NumberSetting;
import com.doge.client.module.Category;
import com.doge.client.module.Module;

public class Fov extends Module {

    public NumberSetting fov = new NumberSetting("Fov", this, 100, 1, 179, 1);

    float fovOld;

    public Fov() {
        super("Fov", "Changes your fov", Category.RENDER);
        this.addSetting(fov);
    }

    @Override
    public void onEnable() {
        fovOld = mc.gameSettings.fovSetting;
    }

    @Override
    public void onUpdate() {
        mc.gameSettings.fovSetting = (float) fov.getNumber();
    }

    @Override
    public void onDisable() {
        if (fovOld <= 0) {
            mc.gameSettings.fovSetting = 90f;
        } else {
            mc.gameSettings.fovSetting = fovOld;
        }
    }
}
