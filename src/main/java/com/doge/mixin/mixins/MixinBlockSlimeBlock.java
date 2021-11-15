package com.doge.mixin.mixins;

import com.doge.client.manager.ModuleManager;
import com.doge.client.module.modules.movement.NoSlow;
import net.minecraft.block.BlockSlime;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockSlime.class)
public class MixinBlockSlimeBlock {
    @Inject(method = "onEntityWalk", at = @At("HEAD"), cancellable = true)
    private void onSteppedOn(World world, BlockPos pos, Entity entity, CallbackInfo info) {
        if (ModuleManager.getModuleByName("No Slow").isOn() && NoSlow.INSTANCE.slime.isOn()) {
            info.cancel();
        }
    }
}
