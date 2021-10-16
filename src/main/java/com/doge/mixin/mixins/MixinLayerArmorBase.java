package com.doge.mixin.mixins;

import com.doge.client.manager.ModuleManager;
import com.doge.client.module.modules.render.NoRender;
import net.minecraft.client.renderer.entity.layers.LayerArmorBase;
import net.minecraft.entity.EntityLivingBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LayerArmorBase.class)
public class MixinLayerArmorBase {
    @Inject(method = "doRenderLayer", at = @At("HEAD"), cancellable = true)
    public void doRenderLayer(EntityLivingBase entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale, CallbackInfo info) {
        try {
            NoRender noRender = (NoRender) ModuleManager.getModuleByName("No Render");
            if (noRender.isOn() && noRender.armour.isOn()) {
                info.cancel();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
