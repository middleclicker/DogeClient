package com.doge.client.module.modules.render;

import com.doge.api.setting.settings.ModeSetting;
import com.doge.client.module.Category;
import com.doge.client.module.Module;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;

public class Fullbright extends Module {

    public ModeSetting mode = new ModeSetting("Mode", this, "Gamma", "Gamma", "Potion");

    public Fullbright() {
        super("Fullbright", "See in the dark!", Category.RENDER);
        this.addSetting(mode);
    }

    private float lastGamma;

    @Override
    public void onEnable() {
        if (mc.player == null || mc.world == null) {
            return;
        }
        lastGamma = mc.gameSettings.gammaSetting;
        if (mode.getValueName().equals("Gamma")) {
            mc.gameSettings.gammaSetting = 100;
        } else {
            mc.player.addPotionEffect(new PotionEffect(MobEffects.NIGHT_VISION));
        }
    }

    @Override
    public void onDisable() {
        mc.gameSettings.gammaSetting = lastGamma;
        mc.player.removeActivePotionEffect(MobEffects.NIGHT_VISION);
    }
}
