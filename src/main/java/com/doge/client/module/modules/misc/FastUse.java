package com.doge.client.module.modules.misc;

import com.doge.api.setting.settings.BooleanSetting;
import com.doge.api.setting.settings.NumberSetting;
import com.doge.client.module.Category;
import com.doge.client.module.Module;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemEndCrystal;
import net.minecraft.item.ItemExpBottle;

public class FastUse extends Module {

    public NumberSetting delay = new NumberSetting("Delay", this, 0, 0, 4, 1);
    public BooleanSetting xp = new BooleanSetting("XP", this, true);
    public BooleanSetting blocks = new BooleanSetting("Blocks", this, false);
    public BooleanSetting crystal = new BooleanSetting("Crystal", this, false);

    public FastUse() {
        super("Fast Use", "Use items faster.", Category.MISC);
        this.addSetting(delay, xp, blocks, crystal);
    }

    @Override
    public void onUpdate() {
        if (mc.player == null || mc.world == null) { return; }

        if (xp.isOn() && mc.player.getHeldItemMainhand().getItem() instanceof ItemExpBottle || xp.isOn() && mc.player.getHeldItemOffhand().getItem() instanceof ItemExpBottle ||
                blocks.isOn() && mc.player.getHeldItemMainhand().getItem() instanceof ItemBlock || blocks.isOn() && mc.player.getHeldItemOffhand().getItem() instanceof ItemBlock ||
                crystal.isOn() && mc.player.getHeldItemMainhand().getItem() instanceof ItemEndCrystal || crystal.isOn() && mc.player.getHeldItemOffhand().getItem() instanceof ItemEndCrystal) {
            mc.rightClickDelayTimer = (int) delay.getNumber();
        } else {
            mc.rightClickDelayTimer = 4;
        }
    }

    @Override
    public void onDisable() {
        mc.rightClickDelayTimer = 4;
    }
}
