package com.doge.client.module.modules.combat;

import com.doge.api.setting.settings.BooleanSetting;
import com.doge.api.setting.settings.ModeSetting;
import com.doge.api.setting.settings.NumberSetting;
import com.doge.client.Main;
import com.doge.client.friend.FriendManager;
import com.doge.client.module.Category;
import com.doge.client.module.Module;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.util.EnumHand;

import java.util.ArrayList;
import java.util.List;

// I think I wrote this while I was on cocaine
/*
TODO:
 1. Fix some mobs not registering
 2. Fix range not working
 3. Implement rotation setting for packet.
 */


public class Killaura extends Module {

    public ModeSetting mode = new ModeSetting("Mode", this, "Single", "Single", "Multi");
    public ModeSetting priority = new ModeSetting("Priority", this, "Closest", "Closest", "Health");
    public ModeSetting rotate = new ModeSetting("Rotate", this, "None", "None", "Packet", "Legit");
    public BooleanSetting swing = new BooleanSetting("Swing", this, false);
    public BooleanSetting raytrace = new BooleanSetting("Raytrace", this, true);
    public ModeSetting weapon = new ModeSetting("Weapon", this, "Sword", "Sword", "Axe", "All");
    public NumberSetting range = new NumberSetting("Range", this, 5.0, 0.0, 10.0, 0.1);
    public BooleanSetting friends = new BooleanSetting("Friends", this, false);
    public BooleanSetting players = new BooleanSetting("Players", this, true);
    public BooleanSetting monsters = new BooleanSetting("Monsters", this, true);
    public BooleanSetting passives = new BooleanSetting("Passives", this, false);

    public Killaura() {
        super("Kill Aura", "Swings at enemies in a range.", Category.COMBAT);
        this.addSetting(mode, priority, rotate, swing, raytrace, weapon, range, friends, players, monsters, passives);
    }

    private Entity target;
    private ArrayList<Entity> targets = new ArrayList<>();

    @Override
    public void onEnable() {
        this.target = null;
    }

    @Override
    public void onUpdate() {
        if (mc.player == null || mc.world == null) {
            return;
        }
        if (mode.is("Single") && findTargetEntities().size() > 0) {
            target = findTargetEntities().get(0);
            if (raytrace.isOn()) {
                if (mc.player.canEntityBeSeen(target) && !shouldWait()) {
                    if (weapon.is("Sword")) {
                        if (mc.player.getHeldItemMainhand().getItem() == Items.DIAMOND_SWORD ||
                                mc.player.getHeldItemMainhand().getItem() == Items.IRON_SWORD ||
                                mc.player.getHeldItemMainhand().getItem() == Items.GOLDEN_SWORD ||
                                mc.player.getHeldItemMainhand().getItem() == Items.WOODEN_SWORD ||
                                mc.player.getHeldItemMainhand().getItem() == Items.STONE_SWORD) {
                            mc.playerController.attackEntity(mc.player, target);
                            if (this.swing.isOn()) {
                                mc.player.swingArm(EnumHand.MAIN_HAND);
                            }
                            if (this.rotate.is("Camera")) {
                                Main.ROTATION_MANAGER.lookAtEntity(this.target);
                            }
                        }
                    } else if (weapon.is("Axe")) {
                        if (mc.player.getHeldItemMainhand().getItem() == Items.DIAMOND_AXE ||
                                mc.player.getHeldItemMainhand().getItem() == Items.IRON_AXE ||
                                mc.player.getHeldItemMainhand().getItem() == Items.GOLDEN_AXE ||
                                mc.player.getHeldItemMainhand().getItem() == Items.WOODEN_AXE ||
                                mc.player.getHeldItemMainhand().getItem() == Items.STONE_AXE) {
                            mc.playerController.attackEntity(mc.player, target);
                            if (this.swing.isOn()) {
                                mc.player.swingArm(EnumHand.MAIN_HAND);
                            }
                            if (this.rotate.is("Camera")) {
                                Main.ROTATION_MANAGER.lookAtEntity(this.target);
                            }
                        }
                    } else {
                        mc.playerController.attackEntity(mc.player, target);
                        if (this.swing.isOn()) {
                            mc.player.swingArm(EnumHand.MAIN_HAND);
                        }
                        if (this.rotate.is("Camera")) {
                            Main.ROTATION_MANAGER.lookAtEntity(this.target);
                        }
                    }
                }
            } else {
                if (!shouldWait()) {
                    mc.playerController.attackEntity(mc.player, target);
                    if (this.swing.isOn()) {
                        mc.player.swingArm(EnumHand.MAIN_HAND);
                    }
                    if (this.rotate.is("Camera")) {
                        Main.ROTATION_MANAGER.lookAtEntity(this.target);
                    } else if (this.rotate.is("Packet")) {

                    }
                }
            }
        } else if (mode.is("Multi") && findTargetEntities().size() > 0) {
            targets = findTargetEntities();
            for (Entity entity : targets) {
                if (raytrace.isOn()) {
                    if (mc.player.canEntityBeSeen(entity) && !shouldWait()) {
                        if (weapon.is("Sword")) {
                            if (mc.player.getHeldItemMainhand().getItem() == Items.DIAMOND_SWORD ||
                                    mc.player.getHeldItemMainhand().getItem() == Items.IRON_SWORD ||
                                    mc.player.getHeldItemMainhand().getItem() == Items.GOLDEN_SWORD ||
                                    mc.player.getHeldItemMainhand().getItem() == Items.WOODEN_SWORD ||
                                    mc.player.getHeldItemMainhand().getItem() == Items.STONE_SWORD) {
                                mc.playerController.attackEntity(mc.player, entity);
                                if (this.swing.isOn()) {
                                    mc.player.swingArm(EnumHand.MAIN_HAND);
                                }
                                if (this.rotate.is("Camera")) {
                                    Main.ROTATION_MANAGER.lookAtEntity(entity);
                                }
                            }
                        } else if (weapon.is("Axe")) {
                            if (mc.player.getHeldItemMainhand().getItem() == Items.DIAMOND_AXE ||
                                    mc.player.getHeldItemMainhand().getItem() == Items.IRON_AXE ||
                                    mc.player.getHeldItemMainhand().getItem() == Items.GOLDEN_AXE ||
                                    mc.player.getHeldItemMainhand().getItem() == Items.WOODEN_AXE ||
                                    mc.player.getHeldItemMainhand().getItem() == Items.STONE_AXE) {
                                mc.playerController.attackEntity(mc.player, entity);
                                if (this.swing.isOn()) {
                                    mc.player.swingArm(EnumHand.MAIN_HAND);
                                }
                                if (this.rotate.is("Camera")) {
                                    Main.ROTATION_MANAGER.lookAtEntity(entity);
                                }
                            }
                        } else {
                            mc.playerController.attackEntity(mc.player, entity);
                            if (this.swing.isOn()) {
                                mc.player.swingArm(EnumHand.MAIN_HAND);
                            }
                            if (this.rotate.is("Camera")) {
                                Main.ROTATION_MANAGER.lookAtEntity(entity);
                            }
                        }
                    }
                } else {
                    if (!shouldWait()) {
                        mc.playerController.attackEntity(mc.player, entity);
                        if (this.swing.isOn()) {
                            mc.player.swingArm(EnumHand.MAIN_HAND);
                        }
                        if (this.rotate.is("Camera")) {
                            Main.ROTATION_MANAGER.lookAtEntity(entity);
                        } else if (this.rotate.is("Packet")) {

                        }
                    }
                }
            }
        }
    }

    private boolean shouldWait() {
        if (mc.player.getCooledAttackStrength(0) < 1) {
            return true;
        } else {
            return mc.player.ticksExisted % 2 == 1;
        }
    }

    private ArrayList<Entity> findTargetEntities() {
        ArrayList<Entity> entities = new ArrayList<>();
        double minHealth = getMinHealth(mc.world.getLoadedEntityList());
        double minDistance = getMinDistance(mc.world.getLoadedEntityList());
        for (Entity entity : mc.world.getLoadedEntityList()) {
            if (entity.isEntityAlive() && entity != mc.player && mc.player.getDistanceSq(entity) < range.getNumber()) {  // Basic Checks
                if (mode.is("Single")) { // If killaura should only target one entity.
                    if (entity instanceof EntityPlayer && players.isOn()) { // If the entity is a player and killaura should target players.
                        if (priority.is("Health")) { // If priority is health.
                            EntityPlayer e = (EntityPlayer) entity;
                            if (getTotalHealth(e) <= minHealth) { // If the entity's health is lower than or equal to min health (should be equals).
                                entities.add(entity);
                                return entities;
                            }
                        } else { // If priority is closest.
                            // Similar to if priority is health.
                            EntityPlayer e = (EntityPlayer) entity;
                            if (getDistance(e) <= minDistance) {
                                entities.add(entity);
                                return entities;
                            }
                        }
                    } else if (entity instanceof EntityMob && monsters.isOn()) { // If the entity is a monster and killaura should target monsters.
                        // Need to figure something out later... but this will stay for now
                        entities.add(entity);
                        return entities;
                    } else if (entity instanceof EntityAnimal && passives.isOn()) { // If the entity is passive and killaura should target passives.
                        entities.add(entity);
                        return entities;
                    }
                } else { // If killaura should target multiple entities.
                    entities.add(entity); // Lol
                }
            }

        }
        return entities;
    }

    private double getTotalHealth(EntityPlayer player) {
        return player.getHealth() + player.getAbsorptionAmount();
    }

    private double getMinHealth(List<Entity> loadedEntityList) {
        double min = 36.0; // Set min to highest health a player can have.
        if (loadedEntityList.size() == 0) {
            return 37.0;
        }
        for (int i = 1; i < loadedEntityList.size(); i++) { // Loop through the loaded entity list.
            if (loadedEntityList.get(i) instanceof EntityPlayer) {
                EntityPlayer e = (EntityPlayer) loadedEntityList.get(i);
                if (e == mc.player) continue; // Target cant be yourself.
                if (FriendManager.isFriend(e.getName()) && !friends.isOn()) continue; // Do u want to attack friends?
                if (getTotalHealth(e) < min) { // If the player has lower health than lowest health.
                    min = getTotalHealth(e); // Set min health to player health.
                }
            }

        }
        return min;
    }

    private double getMinDistance(List<Entity> loadedEntityList) {
        // Similar to getMinHealth function
        double min = range.getNumber();
        if (loadedEntityList.size() == 0) {
            return range.getNumber() + 1;
        }
        for (int i = 1; i < loadedEntityList.size(); i++) {
            if (loadedEntityList.get(i) instanceof EntityPlayer) {
                EntityPlayer e = (EntityPlayer) loadedEntityList.get(i);
                if (e == mc.player) continue;
                if (FriendManager.isFriend(e.getName()) && !friends.isOn()) continue;
                if (getDistance(e) < min) {
                    min = getDistance(e);
                }
            }
        }
        return min;
    }

    private double getDistance(Entity entity) {
        return mc.player.getDistanceSq(entity.posX, entity.posY, entity.posZ);
    }
}
