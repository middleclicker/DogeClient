package com.doge.client.module.modules.combat;

import com.doge.api.setting.settings.BooleanSetting;
import com.doge.api.setting.settings.NumberSetting;
import com.doge.api.util.BlockUtil;
import com.doge.api.util.ClientMessage;
import com.doge.api.util.InventoryUtil;
import com.doge.client.module.Category;
import com.doge.client.module.Module;
import net.minecraft.block.*;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;

public class Surround extends Module {

    public BooleanSetting rotate = new BooleanSetting("Rotate", this, false);
    public BooleanSetting packet = new BooleanSetting("Packet", this, false);
    public BooleanSetting center = new BooleanSetting("Center", this, true);
    public BooleanSetting silent = new BooleanSetting("Silent Switch", this, false);
    public BooleanSetting autoDisable = new BooleanSetting("Auto Disable", this, true);
    public NumberSetting delay = new NumberSetting("Delay In Ticks", this, 0, 0, 10, 1);

    public Surround() {
        super("Surround", "Surrounds you in obsidian to create a safe hole.", Category.COMBAT);
        this.addSetting(rotate, packet, center, silent, autoDisable, delay);
    }

    private int yLevel = 0;

    @Override
    public void onEnable() {
        if (mc.player == null || mc.world == null) { return; }
        yLevel = (int) Math.round(mc.player.posY);
        if (center.isOn()) {
            mc.player.connection.sendPacket(new CPacketPlayer.Position(Math.floor(mc.player.posX)+0.5, yLevel, Math.floor(mc.player.posZ)+0.5, true));
            mc.player.setPosition(Math.floor(mc.player.posX)+0.5, yLevel, Math.floor(mc.player.posZ)+0.5);
        }
    }

    @Override
    public void onUpdate() {
        if (mc.player == null || mc.world == null) { return; }
        int obbyslot;
        if (InventoryUtil.findHotbarBlock(BlockObsidian.class) == -1) {
            if (InventoryUtil.isBlock(mc.player.getHeldItemOffhand().getItem(), BlockObsidian.class)) {
                obbyslot = -2;
            } else {
                obbyslot = -1;
            }
        } else {
            obbyslot = InventoryUtil.findHotbarBlock(BlockObsidian.class);
        }
        if (obbyslot == -1) {
            this.toggle();
            ClientMessage.sendMessage("Unable to find obsidian in hotbar!");
            return;
        }

        BlockPos front = new BlockPos(Math.floor(mc.player.posX), yLevel, Math.floor(mc.player.posZ+1));
        BlockPos back = new BlockPos(Math.floor(mc.player.posX), yLevel, Math.floor(mc.player.posZ-1));
        BlockPos left = new BlockPos(Math.floor(mc.player.posX-1), yLevel, Math.floor(mc.player.posZ));
        BlockPos right = new BlockPos(Math.floor(mc.player.posX+1), yLevel, Math.floor(mc.player.posZ));
        BlockPos frontDown = new BlockPos(Math.floor(mc.player.posX), yLevel-1, Math.floor(mc.player.posZ+1));
        BlockPos backDown = new BlockPos(Math.floor(mc.player.posX), yLevel-1, Math.floor(mc.player.posZ-1));
        BlockPos leftDown = new BlockPos(Math.floor(mc.player.posX-1), yLevel-1, Math.floor(mc.player.posZ));
        BlockPos rightDown = new BlockPos(Math.floor(mc.player.posX+1), yLevel-1, Math.floor(mc.player.posZ));

        // Stupid way of doing it but it works
        if (!isBlock(frontDown)) {
            if (obbyslot == -2) {
                BlockUtil.placeBlock(frontDown, EnumHand.OFF_HAND, rotate.enabled, packet.enabled, false);
            } else {
                InventoryUtil.switchToHotbarSlot(obbyslot, silent.enabled);
                BlockUtil.placeBlock(frontDown, EnumHand.MAIN_HAND, rotate.enabled, packet.enabled, false);
            }
        }
        if (!isBlock(leftDown)) {
            if (obbyslot == -2) {
                BlockUtil.placeBlock(leftDown, EnumHand.OFF_HAND, rotate.enabled, packet.enabled, false);
            } else {
                InventoryUtil.switchToHotbarSlot(obbyslot, silent.enabled);
                BlockUtil.placeBlock(leftDown, EnumHand.MAIN_HAND, rotate.enabled, packet.enabled, false);
            }
        }
        if (!isBlock(backDown)) {
            if (obbyslot == -2) {
                BlockUtil.placeBlock(backDown, EnumHand.OFF_HAND, rotate.enabled, packet.enabled, false);
            } else {
                InventoryUtil.switchToHotbarSlot(obbyslot, silent.enabled);
                BlockUtil.placeBlock(backDown, EnumHand.MAIN_HAND, rotate.enabled, packet.enabled, false);
            }
        }
        if (!isBlock(rightDown)) {
            if (obbyslot == -2) {
                BlockUtil.placeBlock(rightDown, EnumHand.OFF_HAND, rotate.enabled, packet.enabled, false);
            } else {
                InventoryUtil.switchToHotbarSlot(obbyslot, silent.enabled);
                BlockUtil.placeBlock(rightDown, EnumHand.MAIN_HAND, rotate.enabled, packet.enabled, false);
            }
        }

        if (!isBlock(front)) {
            if (obbyslot == -2) {
                BlockUtil.placeBlock(front, EnumHand.OFF_HAND, rotate.enabled, packet.enabled, false);
            } else {
                InventoryUtil.switchToHotbarSlot(obbyslot, silent.enabled);
                BlockUtil.placeBlock(front, EnumHand.MAIN_HAND, rotate.enabled, packet.enabled, false);
            }
        }
        if (!isBlock(left)) {
            if (obbyslot == -2) {
                BlockUtil.placeBlock(left, EnumHand.OFF_HAND, rotate.enabled, packet.enabled, false);
            } else {
                InventoryUtil.switchToHotbarSlot(obbyslot, silent.enabled);
                BlockUtil.placeBlock(left, EnumHand.MAIN_HAND, rotate.enabled, packet.enabled, false);
            }
        }
        if (!isBlock(back)) {
            if (obbyslot == -2) {
                BlockUtil.placeBlock(back, EnumHand.OFF_HAND, rotate.enabled, packet.enabled, false);
            } else {
                InventoryUtil.switchToHotbarSlot(obbyslot, silent.enabled);
                BlockUtil.placeBlock(back, EnumHand.MAIN_HAND, rotate.enabled, packet.enabled, false);
            }
        }
        if (!isBlock(right)) {
            if (obbyslot == -2) {
                BlockUtil.placeBlock(right, EnumHand.OFF_HAND, rotate.enabled, packet.enabled, false);
            } else {
                InventoryUtil.switchToHotbarSlot(obbyslot, silent.enabled);
                BlockUtil.placeBlock(right, EnumHand.MAIN_HAND, rotate.enabled, packet.enabled, false);
            }
        }

        if (autoDisable.isOn()) {
            if (!mc.player.onGround) {
                this.toggle();
            }
        }
    }

    public static boolean isBlock(BlockPos blockpos) {
        Block block = mc.world.getBlockState(blockpos).getBlock();
        if (block instanceof BlockAir || block instanceof BlockLiquid || block instanceof BlockTallGrass || block instanceof BlockFire || block instanceof BlockDeadBush || block instanceof BlockSnow) {
            return false;
        }
        return true;
    }
}
