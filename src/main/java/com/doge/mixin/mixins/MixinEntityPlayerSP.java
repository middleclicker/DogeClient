package com.doge.mixin.mixins;

import com.doge.api.event.events.PlayerUpdateEvent;
import com.doge.client.Main;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityPlayerSP.class)
public abstract class MixinEntityPlayerSP extends AbstractClientPlayer {
    public MixinEntityPlayerSP() {
        super(null, null);
    }

    @Inject(method = "onUpdate", at = @At("HEAD"), cancellable = true)
    public void onUpdate(CallbackInfo info) {
        PlayerUpdateEvent event = new PlayerUpdateEvent();
        Main.EVENT_BUS.post(event);
        if (event.isCancelled()) {
            info.cancel();
        }
    }
}