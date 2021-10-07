package com.doge.client.module.modules.combat;

import com.doge.api.setting.settings.BooleanSetting;
import com.doge.api.setting.settings.NumberSetting;
import com.doge.api.util.Timer;
import com.doge.client.module.Category;
import com.doge.client.module.Module;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

public class AutoArmour extends Module {

    // TODO: Rewrite cuz its skidded from w+2

    public NumberSetting delay = new NumberSetting("Delay", this, 250, 0, 1000, 1);
    public BooleanSetting cursed = new BooleanSetting("Cursed", this, false);

    private final Timer delayTimer = new Timer();

    public AutoArmour() {
        super("Auto Armour", "Automatically equips armour pieces.", Category.COMBAT);
        this.addSetting(delay, cursed);
    }

    @Override
    public void onUpdate() {
        int[] bestArmorSlots = new int[4];
        int[] bestArmorValues = new int[4];

        for (int armourType = 0; armourType < 4; armourType++) {
            ItemStack oldArmour = mc.player.inventory.armorItemInSlot(armourType);
            if (oldArmour.getItem() instanceof ItemArmor) {
                bestArmorValues[armourType] = (((ItemArmor) oldArmour.getItem()).damageReduceAmount);
            }
            bestArmorSlots[armourType] = -1;
        }

        for (int slot = 0; slot < 36; slot++) {
            ItemStack stack = mc.player.inventory.getStackInSlot(slot);

            if (stack.getCount() > 1)
                continue;

            if(!(stack.getItem() instanceof ItemArmor))
                continue;

            ItemArmor armor = (ItemArmor)stack.getItem();
            int armorType = armor.armorType.ordinal() - 2;

            if (armorType == 2 && mc.player.inventory.armorItemInSlot(armorType).getItem().equals(Items.ELYTRA)) continue;

            int armorValue = armor.damageReduceAmount;

            if(armorValue > bestArmorValues[armorType])
            {
                bestArmorSlots[armorType] = slot;
                bestArmorValues[armorType] = armorValue;
            }
        }

        for (int armorType = 0; armorType < 4; armorType++) {
            // check if better armor was found
            int slot = bestArmorSlots[armorType];
            if(slot == -1)
                continue;

            // check if armor can be swapped
            // needs 1 free slot where it can put the old armor
            ItemStack oldArmor = mc.player.inventory.armorItemInSlot(armorType);
            if(oldArmor != ItemStack.EMPTY || mc.player.inventory.getFirstEmptyStack() != -1)
            {
                // hotbar fix
                if(slot < 9)
                    slot += 36;

                // swap armor
                mc.playerController.windowClick(0, 8 - armorType, 0,
                        ClickType.QUICK_MOVE, mc.player);
                mc.playerController.windowClick(0, slot, 0,
                        ClickType.QUICK_MOVE, mc.player);

                break;
            }
        }
    }
}
