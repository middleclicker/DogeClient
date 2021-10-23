package com.doge.client.module.modules.hud;

import com.doge.client.Main;
import com.doge.client.module.Category;
import com.doge.client.module.Module;
import org.lwjgl.input.Keyboard;

public class HUDEditor extends Module {
    public HUDEditor() {
        super("HUD Editor", "Edit HUD modules.", Category.HUD);
    }

    @Override
    public void onEnable() {
        Main.INSTANCE.dogeGUI.enterHUDEditor();
    }

    @Override
    public void onUpdate() {
        if (mc.player == null || mc.world == null) { return; }
        if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
            this.setToggled(false);
            Main.INSTANCE.dogeGUI.enterGUI();
        }
    }

    @Override
    public void onDisable() {

    }
}
