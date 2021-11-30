package com.doge.mixin.mixins;

import com.doge.api.event.events.DamageBlockEvent;
import com.doge.client.Main;
import com.doge.client.manager.ModuleManager;
import com.doge.client.module.modules.exploit.Reach;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

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

    @Inject(method = "onPlayerDamageBlock(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/EnumFacing;)Z", at = @At("HEAD"), cancellable = true)
    private void onPlayerDamageBlock(BlockPos posBlock, EnumFacing directionFacing, CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        DamageBlockEvent event = new DamageBlockEvent(posBlock, directionFacing);
        Main.EVENT_BUS.post(event);
        if (event.isCancelled()) {
            callbackInfoReturnable.setReturnValue(false);
        }
    }

}
