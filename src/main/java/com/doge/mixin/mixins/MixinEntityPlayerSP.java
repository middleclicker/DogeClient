package com.doge.mixin.mixins;

import com.doge.api.event.events.PlayerMoveEvent;
import com.doge.client.Main;
import com.doge.client.manager.ModuleManager;
import com.doge.client.module.modules.movement.Sprint;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.MoverType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(EntityPlayerSP.class)
public abstract class MixinEntityPlayerSP extends AbstractClientPlayer {
    public MixinEntityPlayerSP() {
        super(null, null);
    }

    @Redirect(method = "move", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/entity/AbstractClientPlayer;move(Lnet/minecraft/entity/MoverType;DDD)V"))
    public void move(AbstractClientPlayer player, MoverType type, double x, double y, double z) {
        PlayerMoveEvent moveEvent = new PlayerMoveEvent(type, x, y, z);
        Main.EVENT_BUS.post(moveEvent);
        super.move(type, moveEvent.x, moveEvent.y, moveEvent.z);
    }

    @Redirect(method={"onLivingUpdate"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/entity/EntityPlayerSP;setSprinting(Z)V", ordinal=2))
    public void onLivingUpdate(EntityPlayerSP entityPlayerSP, boolean sprinting) {
        Sprint sprint = (Sprint) ModuleManager.getModuleByName("Sprint");
        if (sprint.isOn() && sprint.mode.is("Multi Directional") && (Minecraft.getMinecraft().player.movementInput.moveForward != 0.0f || Minecraft.getMinecraft().player.movementInput.moveStrafe != 0.0f)) entityPlayerSP.setSprinting(true);
        else entityPlayerSP.setSprinting(sprinting);
    }
}