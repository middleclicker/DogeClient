package com.doge.mixin.mixins;

import com.doge.client.Main;
import com.doge.client.manager.ModuleManager;
import net.minecraft.client.Minecraft;
import net.minecraft.crash.CrashReport;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MixinMinecraft {
    @Inject(method = "crashed", at = @At("HEAD"))
    public void crashed(CrashReport report, CallbackInfo info) {
        Main.SAVELOAD_CONFIG.save();
        ModuleManager.getModuleByName("Discord RPC").setToggled(false);
    }

    @Inject(method = "shutdown", at = @At("HEAD"))
    public void shutdown(CallbackInfo callbackInfo) {
        Main.SAVELOAD_CONFIG.save();
        ModuleManager.getModuleByName("Discord RPC").setToggled(false);
    }
}
