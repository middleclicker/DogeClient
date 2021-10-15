package com.doge.mixin.mixins;

import com.doge.client.manager.ModuleManager;
import com.doge.client.module.modules.render.NoRender;
import net.minecraft.client.renderer.ItemRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemRenderer.class)
public class MixinItemRenderer {
    @Inject(method = "renderFireInFirstPerson", at = @At("HEAD"), cancellable = true)
    private void dontRenderFireInFirstPerson(CallbackInfo info) {
        NoRender noRender = (NoRender) ModuleManager.getModuleByName("No Render");
        try {
            if (noRender.isOn() && noRender.fire.isOn()) {
                info.cancel();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
