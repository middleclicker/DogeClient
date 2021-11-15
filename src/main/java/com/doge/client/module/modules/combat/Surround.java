package com.doge.client.module.modules.combat;

import com.doge.api.setting.settings.BooleanSetting;
import com.doge.api.setting.settings.NumberSetting;
import com.doge.api.util.ClientMessage;
import com.doge.client.module.Category;
import com.doge.client.module.Module;
import net.minecraft.block.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.List;

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

    // Module Functions
    @Override
    public void onEnable() {
        if (mc.player == null || mc.world == null) { return; }
        yLevel = (int) Math.round(mc.player.posY);
        if (center.isOn()) {
            mc.player.connection.sendPacket(new CPacketPlayer.Position(Math.floor(mc.player.posX)+0.5, yLevel, Math.floor(mc.player.posZ)+0.5, true));
            mc.player.setPosition(Math.floor(mc.player.posX)+0.5, yLevel, Math.floor(mc.player.posZ)+0.5);
        }
    }

    // Core Functions
    @Override
    public void onUpdate() {
        if (mc.player == null || mc.world == null) { return; }
        int obbyslot;
        if (findHotbarBlock(BlockObsidian.class) == -1) {
            if (isBlock(mc.player.getHeldItemOffhand().getItem(), BlockObsidian.class)) {
                obbyslot = -2;
            } else {
                obbyslot = -1;
            }
        } else {
            obbyslot = findHotbarBlock(BlockObsidian.class);
        }
        if (obbyslot == -1) {
            this.toggle();
            ClientMessage.sendMessage("Unable to find obsidian in hotbar, disabling.");
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
                placeBlock(frontDown, EnumHand.OFF_HAND, rotate.enabled, packet.enabled, false);
            } else {
                switchToHotbarSlot(obbyslot, silent.enabled);
                placeBlock(frontDown, EnumHand.MAIN_HAND, rotate.enabled, packet.enabled, false);
            }
        }
        if (!isBlock(leftDown)) {
            if (obbyslot == -2) {
                placeBlock(leftDown, EnumHand.OFF_HAND, rotate.enabled, packet.enabled, false);
            } else {
                switchToHotbarSlot(obbyslot, silent.enabled);
                placeBlock(leftDown, EnumHand.MAIN_HAND, rotate.enabled, packet.enabled, false);
            }
        }
        if (!isBlock(backDown)) {
            if (obbyslot == -2) {
                placeBlock(backDown, EnumHand.OFF_HAND, rotate.enabled, packet.enabled, false);
            } else {
                switchToHotbarSlot(obbyslot, silent.enabled);
                placeBlock(backDown, EnumHand.MAIN_HAND, rotate.enabled, packet.enabled, false);
            }
        }
        if (!isBlock(rightDown)) {
            if (obbyslot == -2) {
                placeBlock(rightDown, EnumHand.OFF_HAND, rotate.enabled, packet.enabled, false);
            } else {
                switchToHotbarSlot(obbyslot, silent.enabled);
                placeBlock(rightDown, EnumHand.MAIN_HAND, rotate.enabled, packet.enabled, false);
            }
        }

        if (!isBlock(front)) {
            if (obbyslot == -2) {
                placeBlock(front, EnumHand.OFF_HAND, rotate.enabled, packet.enabled, false);
            } else {
                switchToHotbarSlot(obbyslot, silent.enabled);
                placeBlock(front, EnumHand.MAIN_HAND, rotate.enabled, packet.enabled, false);
            }
        }
        if (!isBlock(left)) {
            if (obbyslot == -2) {
                placeBlock(left, EnumHand.OFF_HAND, rotate.enabled, packet.enabled, false);
            } else {
                switchToHotbarSlot(obbyslot, silent.enabled);
                placeBlock(left, EnumHand.MAIN_HAND, rotate.enabled, packet.enabled, false);
            }
        }
        if (!isBlock(back)) {
            if (obbyslot == -2) {
                placeBlock(back, EnumHand.OFF_HAND, rotate.enabled, packet.enabled, false);
            } else {
                switchToHotbarSlot(obbyslot, silent.enabled);
                placeBlock(back, EnumHand.MAIN_HAND, rotate.enabled, packet.enabled, false);
            }
        }
        if (!isBlock(right)) {
            if (obbyslot == -2) {
                placeBlock(right, EnumHand.OFF_HAND, rotate.enabled, packet.enabled, false);
            } else {
                switchToHotbarSlot(obbyslot, silent.enabled);
                placeBlock(right, EnumHand.MAIN_HAND, rotate.enabled, packet.enabled, false);
            }
        }

        if (autoDisable.isOn()) {
            if (!mc.player.onGround) {
                this.toggle();
            }
        }
    }

    // Util Functions -----------------------------------------------------------------------------------
    public static boolean isBlock(BlockPos blockpos) {
        Block block = mc.world.getBlockState(blockpos).getBlock();
        if (block instanceof BlockAir || block instanceof BlockLiquid || block instanceof BlockTallGrass || block instanceof BlockFire || block instanceof BlockDeadBush || block instanceof BlockSnow) {
            return false;
        }
        return true;
    }
    public static void switchToHotbarSlot(Class c, boolean silent) {
        int slot = findHotbarBlock(c);
        if (slot > -1) {
            switchToHotbarSlot(slot, silent);
        }
    }
    public static void switchToHotbarSlot(int slot, boolean silent) {
        if (mc.player.inventory.currentItem == slot || slot < 0) {
            return;
        }
        if (silent) {
            mc.player.connection.sendPacket(new CPacketHeldItemChange(slot));
            mc.playerController.updateController();
        } else {
            mc.player.connection.sendPacket(new CPacketHeldItemChange(slot));
            mc.player.inventory.currentItem = slot;
            mc.playerController.updateController();
        }
    }
    public static int findHotbarBlock(Class c) {
        for (int i = 0; i < 9; ++i) {
            ItemStack stack = mc.player.inventory.getStackInSlot(i);
            if (stack == ItemStack.EMPTY) continue;
            if (c.isInstance(stack.getItem())) {
                return i;
            }
            if (!(stack.getItem() instanceof ItemBlock) || !c.isInstance(((ItemBlock) stack.getItem()).getBlock()))
                continue;
            return i;
        }
        return -1;
    }
    public static boolean isBlock(Item item, Class c) {
        if (item instanceof ItemBlock) {
            Block block = ((ItemBlock) item).getBlock();
            return c.isInstance(block);
        }
        return false;
    }
    public static boolean placeBlock(BlockPos pos, EnumHand hand, boolean rotate, boolean packet, boolean isSneaking) {
        EnumFacing side = getFirstFacing(pos);
        if (side == null) {
            return isSneaking;
        }

        BlockPos neighbour = pos.offset(side);
        EnumFacing opposite = side.getOpposite();

        Vec3d hitVec = new Vec3d(neighbour).add(0.5, 0.5, 0.5).add(new Vec3d(opposite.getDirectionVec()).scale(0.5));
        if (!mc.player.isSneaking()) {
            mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_SNEAKING));
            mc.player.setSneaking(true);
        }

        if (rotate) {
            faceVector(hitVec, true);
        }

        rightClickBlock(neighbour, hitVec, hand, opposite, packet);
        mc.player.swingArm(EnumHand.MAIN_HAND);
        return true;
    }
    public static EnumFacing getFirstFacing(BlockPos pos) {
        for (EnumFacing facing : getPossibleSides(pos)) {
            return facing;
        }
        return null;
    }
    public static List<EnumFacing> getPossibleSides(BlockPos pos) {
        List<EnumFacing> facings = new ArrayList<>();
        for (EnumFacing side : EnumFacing.values()) {
            BlockPos neighbour = pos.offset(side);
            if(mc.world.getBlockState(neighbour) == null) return facings;
            if(mc.world.getBlockState(neighbour).getBlock() == null) return facings;
            if (mc.world.getBlockState(neighbour).getBlock().canCollideCheck(mc.world.getBlockState(neighbour), false)) {
                IBlockState blockState = mc.world.getBlockState(neighbour);
                if (!blockState.getMaterial().isReplaceable()) {
                    facings.add(side);
                }
            }
        }
        return facings;
    }
    public static void faceVector(Vec3d vec, boolean normalizeAngle) {
        float[] rotations = getLegitRotations(vec);
        mc.player.connection.sendPacket(new CPacketPlayer.Rotation(rotations[0], normalizeAngle ? MathHelper.normalizeAngle((int) rotations[1], 360) : rotations[1], mc.player.onGround));
    }
    public static float[] getLegitRotations(Vec3d vec) {
        Vec3d eyesPos = new Vec3d(mc.player.posX, mc.player.posY + mc.player.getEyeHeight(), mc.player.posZ);
        double diffX = vec.x - eyesPos.x;
        double diffY = vec.y - eyesPos.y;
        double diffZ = vec.z - eyesPos.z;
        double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);

        float yaw = (float) Math.toDegrees(Math.atan2(diffZ, diffX)) - 90F;
        float pitch = (float) -Math.toDegrees(Math.atan2(diffY, diffXZ));

        return new float[]{
                mc.player.rotationYaw + MathHelper.wrapDegrees(yaw - mc.player.rotationYaw),
                mc.player.rotationPitch + MathHelper.wrapDegrees(pitch - mc.player.rotationPitch)
        };
    }
    public static void rightClickBlock(BlockPos pos, Vec3d vec, EnumHand hand, EnumFacing direction, boolean packet) {
        if (packet) {
            float f = (float) (vec.x - (double) pos.getX());
            float f1 = (float) (vec.y - (double) pos.getY());
            float f2 = (float) (vec.z - (double) pos.getZ());
            mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(pos, direction, hand, f, f1, f2));
        } else {
            mc.playerController.processRightClickBlock(mc.player, mc.world, pos, direction, vec, hand);
        }
    }
}