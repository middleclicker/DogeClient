package com.doge.mixin.mixins;

import com.doge.client.manager.ModuleManager;
import com.doge.client.module.modules.exploit.Reach;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin({PlayerControllerMP.class})
public class MixinPlayerControllerMP {

    /**
     * @return Player Block Reach
     * @author Middleclicker
     * @reason For reach module
     */
    @Overwrite
    public float getBlockReachDistance() {
        Reach reachModule = (Reach) ModuleManager.getModuleByName("Reach");
        assert reachModule != null;
        return (float) reachModule.distance.getNumber();
    }

}
