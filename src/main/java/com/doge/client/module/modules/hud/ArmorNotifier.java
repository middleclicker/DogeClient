package com.doge.client.module.modules.hud;

import com.doge.api.setting.settings.ColorSetting;
import com.doge.api.util.color.DColor;
import com.doge.client.module.Category;
import com.doge.client.module.HUDModule;
import com.lukflug.panelstudio.hud.HUDList;
import com.lukflug.panelstudio.hud.ListComponent;
import com.lukflug.panelstudio.theme.Theme;
import net.minecraft.item.ItemStack;

import java.awt.*;

public class ArmorNotifier extends HUDModule {

    private ColorSetting color = new ColorSetting("Color", this, new DColor(188, 136, 206, 255));

    public ArmorNotifier() {
        super("Armor Notifier", "Notifies you when your armor's durability is low.", new Point(mc.displayWidth/2, 15), Category.HUD);
        this.addSetting(color);
    }

    @Override
    public void populate(Theme theme) {
        component = new ListComponent(getName(), theme.getPanelRenderer(), position, new ArmorNotifierList());
    }

    private boolean shouldDisplay() {
        for (int i = 5; i <= 8; i++) {
            ItemStack stack = mc.player.inventoryContainer.getInventory().get(i);
            double max_dam = stack.getMaxDamage();
            double dam_left = stack.getMaxDamage() - stack.getItemDamage();
            double percent = (dam_left / max_dam) * 100;
            if (percent < 30) {
                return true;
            }
        }
        return false;
    }

    private class ArmorNotifierList implements HUDList {
        @Override
        public int getSize() {
            return 1;
        }

        @Override
        public String getItem(int i) {
            if (shouldDisplay()) {
                return "Your Armor's Durability is low.";
            }
            return "";
        }

        @Override
        public Color getItemColor(int i) {
            return color.getValue();
        }

        @Override
        public boolean sortUp() {
            return false;
        }

        @Override
        public boolean sortRight() {
            return false;
        }
    }
}
