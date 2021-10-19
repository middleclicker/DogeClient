package com.doge.client.module.modules.misc;

import com.doge.client.module.Category;
import com.doge.client.module.Module;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.entity.EntityOtherPlayerMP;

import java.util.UUID;

public class FakePlayer extends Module {
    public FakePlayer() {
        super("Fake Player", "Spawns a fake player.", Category.MISC);
    }

    private EntityOtherPlayerMP fake_player;

    @Override
    public void onEnable() {
        fake_player = new EntityOtherPlayerMP(mc.world, new GameProfile(UUID.fromString("90676414-9e9d-49b0-b109-dc7cbd53f0ab"), "Middleclicker"));
        fake_player.copyLocationAndAnglesFrom(mc.player);
        fake_player.rotationYawHead = mc.player.rotationYawHead;
        mc.world.addEntityToWorld(-100, fake_player);
        fake_player.onLivingUpdate();
    }

    @Override
    public void onDisable() {
        try {
            mc.world.removeEntityFromWorld(-100);
        } catch (Exception ignored) {}
    }
}
