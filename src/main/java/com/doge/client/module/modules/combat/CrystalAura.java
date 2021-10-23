package com.doge.client.module.modules.combat;

import com.doge.api.event.Event;
import com.doge.api.event.events.PacketEvent;
import com.doge.api.event.events.UpdateWalkingPlayerEvent;
import com.doge.api.setting.settings.BooleanSetting;
import com.doge.api.setting.settings.ColorSetting;
import com.doge.api.setting.settings.ModeSetting;
import com.doge.api.setting.settings.NumberSetting;
import com.doge.api.util.*;
import com.doge.api.util.color.DColor;
import com.doge.client.Main;
import com.doge.client.friend.FriendManager;
import com.doge.client.module.Category;
import com.doge.client.module.Module;
import me.zero.alpine.listener.Listener;
import net.minecraft.block.Block;
import net.minecraft.block.BlockObsidian;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemEndCrystal;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/*
TODO:
    1. Add Predict
    2. Add chain mode (whatever that is)
    3. Add Anti Weakness
 */

public class CrystalAura extends Module {

    // Settings start ------------------------------------------------------------------
    // Basic
    private final BooleanSetting place = new BooleanSetting("Place", this, true); //
    private final BooleanSetting Break = new BooleanSetting("Break", this, true); //
    private final NumberSetting targetRange = new NumberSetting("Target Range", this, 15.0, 0.0, 20.0, 0.1); //
    private final BooleanSetting targetFriends = new BooleanSetting("Friends", this, false); //
    private final ModeSetting attackPriority = new ModeSetting("Attack Priority", this, "Health", "Health", "Closest"); //

    // Range
    private final NumberSetting placeRange = new NumberSetting("Place Range", this, 5.0, 0.0, 6.0, 0.1); //
    private final NumberSetting breakRange = new NumberSetting("Break Range", this, 5.0, 0.0, 6.0, 0.1); //
    private final NumberSetting wallPlaceRange = new NumberSetting("Wall Place Range", this, 3.5, 0.0, 6.0, 0.1);
    private final NumberSetting wallBreakRange = new NumberSetting("Wall Break Range", this, 3.5, 0.0, 6.0, 0.1);

    // Delay
    private final NumberSetting placeDelay = new NumberSetting("Place Delay", this, 0, 0, 10, 1);
    private final NumberSetting breakDelay = new NumberSetting("Break Delay", this, 0, 0, 10, 1);

    // Damage
    private final NumberSetting minDamagePlace = new NumberSetting("Min Damage Place", this, 9, 0, 36, 1); //
    private final NumberSetting minDamageBreak = new NumberSetting("Min Damage Break", this, 8, 0, 36, 1); //
    private final NumberSetting maxSelfDamage = new NumberSetting("Max Self Damage", this, 5, 0, 36, 1); //

    // Misc
    private final BooleanSetting raytrace = new BooleanSetting("Raytrace", this, false); //
    private final ModeSetting rotate = new ModeSetting("Rotate", this, "None", "None", "Packet", "Full"); //
    private final ModeSetting switchMode = new ModeSetting("Switch", this, "None", "None", "Normal", "Silent"); //
    private final ModeSetting swing = new ModeSetting("Swing", this, "Mainhand", "Mainhand", "Offhand"); //
    private final BooleanSetting ignoreTerrain = new BooleanSetting("Terrain Trace", this, true);

    // Faceplace
    private final BooleanSetting faceplace = new BooleanSetting("Face Place", this, true);
    private final NumberSetting facePlaceHealth = new NumberSetting("Face Place HP", this, 8, 0, 36, 1);

    // Armour breaker
    private final BooleanSetting armourBreaker = new BooleanSetting("Armour Breaker", this, true);
    private final NumberSetting armourDurability = new NumberSetting("Armour Durability", this, 20, 0, 100, 1);

    // Render
    private final ModeSetting renderMode = new ModeSetting("Render", this, "Pretty", "Pretty", "Solid", "Outline", "Circle", "Column");
    private final ColorSetting renderFillColour = new ColorSetting("Fill Colour", this, new DColor(0, 0, 0, 255));
    private final ColorSetting renderBoxColour = new ColorSetting("Box Colour", this, new DColor(255, 255, 255, 255));
    // Settings end ------------------------------------------------------------------

    public CrystalAura() {
        super("Crystal Aura", "Automatically place and detonate crystals.", Category.COMBAT);
        this.addSetting(targetRange, targetFriends, placeRange, breakRange, wallPlaceRange, wallBreakRange, placeDelay, breakDelay, minDamagePlace, minDamageBreak, maxSelfDamage, raytrace, rotate, switchMode, swing, ignoreTerrain, faceplace, facePlaceHealth, armourBreaker, armourDurability, renderMode, renderFillColour, renderBoxColour);
    }

    private EntityPlayer target;
    private EntityEnderCrystal targetingCrystal;
    private List<EntityEnderCrystal> crystals = new ArrayList<>();
    private List<EntityPlayer> players = new ArrayList<>();
    private boolean rotating = false;
    private boolean breaking = false;
    private float yaw, pitch;
    private double PlaceRange = placeRange.getNumber();

    @Override
    public void onUpdate() {
        if (mc.player == null || mc.world == null) { return; }
        List<BlockPos> placeBlocks = new ArrayList<>();
        crystals = mc.world.getEntitiesWithinAABB(EntityEnderCrystal.class, new AxisAlignedBB(mc.player.posX-breakRange.getNumber(), mc.player.posY-breakRange.getNumber(), mc.player.posZ-breakRange.getNumber(), mc.player.posX+breakRange.getNumber(), mc.player.posY+breakRange.getNumber(), mc.player.posZ+breakRange.getNumber())); // Get end crystals within break radius.
        players = mc.world.getEntitiesWithinAABB(EntityPlayer.class, new AxisAlignedBB(mc.player.posX-targetRange.getNumber(), mc.player.posY-targetRange.getNumber(), mc.player.posZ-targetRange.getNumber(), mc.player.posX+targetRange.getNumber(), mc.player.posY+targetRange.getNumber(), mc.player.posZ+targetRange.getNumber())); // Get possible enemies within target radius.
        if (crystals.size() > 0) {
            crystals.removeIf(crystal -> !mc.player.canEntityBeSeen(crystal) && raytrace.isOn()); // Removes ender crystal if it cant be seen and raytrace is on.
        }
        if (players.size() > 0) {
            players.removeIf(player -> FriendManager.isFriend(player.getName()) && !targetFriends.isOn()); // Removes friends if crystal aura shouldn't target friends.
            players.removeIf(player -> !player.isEntityAlive()); // Removes target if target it dead.
            players.removeIf(player -> player == mc.player);
        }

        if (place.isOn()) {
            for (double x = PlaceRange; x < mc.player.posX+PlaceRange; x++) {
                for (double y = mc.player.posY-PlaceRange; y < mc.player.posY+PlaceRange; y++) {
                    for (double z = mc.player.posZ-PlaceRange; z < mc.player.posZ+PlaceRange; z++) {
                        BlockPos checkPos = new BlockPos(x, y, z);
                        if (mc.world.getBlockState(checkPos).getBlock() == Blocks.OBSIDIAN || mc.world.getBlockState(checkPos).getBlock() == Blocks.BEDROCK) {
                            placeBlocks.add(checkPos);
                            // ClientMessage.sendMessage("Added " + mc.world.getBlockState(checkPos).getBlock());
                        }
                    }
                }
            }
        }

        if (attackPriority.is("Health") && players.size() > 0) { // If the priority is health.
            double min = 36.0;
            for (EntityPlayer player : players) {
                double totalHealth = getTotalHealth(player);
                if (totalHealth < min) {
                    min = totalHealth;
                    target = player;
                }
            }
        } else if (attackPriority.is("Closest") && players.size() > 0) { // If the priority is distance.
            double min = targetRange.getNumber();
            for (EntityPlayer player : players) {
                double distance = mc.player.getDistanceSq(player);
                if (distance < min) {
                    min = distance;
                    target = player;
                }
            }
        }
        if (crystals.size() > 0 && target != null) {
            crystals.removeIf(crystal -> CrystalUtil.calculateDamage(crystal, target, false) < minDamageBreak.getNumber());
        }

        if (place.isOn() && target != null) {
            if (placeBlocks.size() > 0) {
                for (BlockPos block : placeBlocks) {
                    EntityEnderCrystal crystal = new EntityEnderCrystal(mc.world, block.x, block.y+1, block.z);
                    if (CrystalUtil.calculateDamage(crystal, target, false) > minDamagePlace.getNumber() ||
                            (getTotalHealth(target) <= facePlaceHealth.getNumber() && faceplace.isOn()) ||
                            (CrystalUtil.shouldArmourBreaker(target, (float) armourDurability.getNumber()) && armourBreaker.isOn())) {
                        int slot = InventoryUtil.findHotbarBlock(ItemEndCrystal.class);
                        if (swing.is("Mainhand")) {
                            if (slot > -1) {
                                if (switchMode.is("Silent")) {
                                    InventoryUtil.switchToHotbarSlot(slot, true);
                                } else {
                                    InventoryUtil.switchToHotbarSlot(slot, false);
                                }
                                BlockUtil.placeCrystalOnBlock(block, EnumHand.MAIN_HAND, true);
                            }
                        } else if (swing.is("Offhand")) {
                            if (switchMode.is("Silent")) {
                                InventoryUtil.switchToHotbarSlot(slot, true);
                            } else {
                                InventoryUtil.switchToHotbarSlot(slot, false);
                            }
                            BlockUtil.placeCrystalOnBlock(block, EnumHand.OFF_HAND, true);
                        }
                    }
                }
            }
        }

        if (Break.isOn()) {
            for (EntityEnderCrystal crystal : crystals) {
                targetingCrystal = crystal;
                if (rotate.is("Packet")) {
                    setYawPitch(targetingCrystal);
                    rotating = false;
                } else if (rotate.is("Full")) {
                    rotating = true;
                }
                if (CrystalUtil.calculateDamage(crystal, mc.player, false) < maxSelfDamage.getNumber()) {
                    breaking = true;
                    breakCrystal(crystal);
                }
            }
        }

    }

    @Override
    public void onDisable() {
        crystals = null;
        players = null;
    }

    private void breakCrystal(EntityEnderCrystal crystal) {
        mc.player.connection.sendPacket(new CPacketUseEntity(crystal));
        // ClientMessage.sendMessage("Breaking");
        breaking = false;
    }

    private final Listener<UpdateWalkingPlayerEvent> walkingListener = new Listener<>(event -> {
        if (event.getEra() == Event.Era.PRE && rotating && rotate.is("Full")) {
            Main.ROTATION_MANAGER.lookAtEntity(targetingCrystal);
        }
    });


    private final Listener<PacketEvent.Send> sendListener = new Listener<>(event -> {
        if (event.getPacket() instanceof CPacketPlayer && rotating && rotate.is("Packet")) {
            final CPacketPlayer player = (CPacketPlayer) event.getPacket();
            player.yaw = yaw;
            player.pitch = pitch;
        }
    });

    private final Listener<PacketEvent.Receive> receiveListener = new Listener<>(event -> {

    });

    private double getTotalHealth(EntityPlayer player) {
        return player.getHealth() + player.getAbsorptionAmount();
    }

    private void setYawPitch(EntityEnderCrystal crystal) {
        float[] angle = MathsUtil.calcAngle(mc.player.getPositionEyes(mc.getRenderPartialTicks()), crystal.getPositionEyes(mc.getRenderPartialTicks()));
        this.yaw = angle[0];
        this.pitch = angle[1];
        this.rotating = true;
    }
}
