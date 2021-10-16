package com.doge.client.module.modules.misc;

import com.doge.api.setting.settings.NumberSetting;
import com.doge.client.module.Category;
import com.doge.client.module.Module;
import net.minecraft.client.gui.GuiMainMenu;

public class AutoDisconnect extends Module {

    public NumberSetting health = new NumberSetting("Health", this, 10, 1, 30, 1);

    public AutoDisconnect() {
        super("Auto Disconnect", "Automatically disconnects for you at a set health.", Category.MISC);
        this.addSetting(health);
    }

    @Override
    public void onUpdate() {
        if (mc.player == null || mc.world == null) { return; }
        if (mc.player.getHealth() <= health.getNumber()) {
            mc.world.sendQuittingDisconnectingPacket();
            mc.loadWorld(null);
            mc.displayGuiScreen(new GuiMainMenu());
            this.toggle();
        }
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }
}
