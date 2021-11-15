package com.doge.client.module.modules.combat;

import com.doge.api.setting.settings.BooleanSetting;
import com.doge.api.setting.settings.ModeSetting;
import com.doge.api.setting.settings.NumberSetting;
import com.doge.client.module.Category;
import com.doge.client.module.Module;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.Item;
import org.lwjgl.input.Mouse;

public class Offhand extends Module {

    ModeSetting mode = new ModeSetting("Mode", this, "Totem", "Totem", "Crystal", "Gapple");
    NumberSetting totemHP = new NumberSetting("Totem Health", this, 16, 0, 36, 1);
    BooleanSetting rightClickGap = new BooleanSetting("RClick Sword Gap", this, true);

    public Offhand() {
        super("Offhand", "Puts items in your offhand.", Category.COMBAT);
        this.addSetting(mode, totemHP, rightClickGap);
    }

    private boolean isRightClicking = false;

    @Override
    public void onUpdate() {
        if (mc.player == null || mc.world == null) { return; }

        float playerHealth = mc.player.getHealth();
        if (playerHealth > totemHP.getNumber()) {
            if (mode.getValueName().equals("Totem") && !isRightClicking) {
                swapItems(getItemSlot(Items.TOTEM_OF_UNDYING));
            }
            if (mode.getValueName().equals("Crystal") && !isRightClicking) {
                swapItems(getItemSlot(Items.END_CRYSTAL));
            }
            if (mode.getValueName().equals("Gapple") && !isRightClicking) {
                swapItems(getItemSlot(Items.GOLDEN_APPLE));
            }
            if (rightClickGap.isOn()) {
                if (Mouse.isButtonDown(1)) {
                    isRightClicking = true;
                    if (mc.player.getHeldItemMainhand().getItem() == Items.DIAMOND_SWORD || mc.player.getHeldItemMainhand().getItem() == Items.IRON_SWORD || mc.player.getHeldItemMainhand().getItem() == Items.STONE_SWORD || mc.player.getHeldItemMainhand().getItem() == Items.WOODEN_SWORD || mc.player.getHeldItemMainhand().getItem() == Items.GOLDEN_SWORD) {
                        swapItems(getItemSlot(Items.GOLDEN_APPLE));
                    }
                } else {
                    isRightClicking = false;
                }
            }
        } else {
            swapItems(getItemSlot(Items.TOTEM_OF_UNDYING));
        }
    }

    public void swapItems(int slot) {
        if (slot == -1) {
            return;
        }
        mc.playerController.windowClick(0, slot, 0, ClickType.PICKUP, mc.player);
        mc.playerController.windowClick(0, 45, 0, ClickType.PICKUP, mc.player);
        mc.playerController.windowClick(0, slot, 0, ClickType.PICKUP, mc.player);
        mc.playerController.updateController();
    }

    public static int getItemSlot(Item input) {
        if (input == mc.player.getHeldItemOffhand().getItem()) return -1;
        for (int i = 36; i >= 0; i--) {
            final Item item = mc.player.inventory.getStackInSlot(i).getItem();
            if (item == input) {
                if (i < 9) {
                    i += 36;
                }
                return i;
            }
        }
        return -1;
    }
}
