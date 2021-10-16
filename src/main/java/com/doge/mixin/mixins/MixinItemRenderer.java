package com.doge.mixin.mixins;

import com.doge.api.event.events.RenderItemEvent;
import com.doge.client.Main;
import com.doge.client.manager.ModuleManager;
import com.doge.client.module.modules.render.NoRender;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.util.EnumHandSide;
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

    @Inject(method = "transformFirstPerson", at = @At("HEAD"))
    public void transformFirstPerson(EnumHandSide hand, float p_187453_2_, CallbackInfo callbackInfo) {
        RenderItemEvent event = new RenderItemEvent(hand);
        Main.EVENT_BUS.post(event);
    }
}
