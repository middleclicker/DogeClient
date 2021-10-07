package com.doge.client.module.modules.movement;

import com.doge.api.event.events.PacketEvent;
import com.doge.api.setting.settings.BooleanSetting;
import com.doge.api.setting.settings.NumberSetting;
import com.doge.client.module.Category;
import com.doge.client.module.Module;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.network.play.server.SPacketExplosion;

public class Velocity extends Module {

    public NumberSetting horizontalVelocity = new NumberSetting("Horizontal", this, 0, 0, 100, 1);
    public NumberSetting verticalVelocity = new NumberSetting("Vertical", this, 0, 0, 100, 1);
    public BooleanSetting explosions = new BooleanSetting("Explosions", this, true);

    public Velocity() {
        super("Velocity", "Cancels your knockback.", Category.MOVEMENT);
        this.addSetting(horizontalVelocity, verticalVelocity, explosions);
    }

    @EventHandler
    private final Listener<PacketEvent.Receive> recieveListener = new Listener<>(event -> {
        if (mc.player == null || mc.world == null) { return; }

        if (event.getPacket() instanceof SPacketExplosion && this.explosions.isOn()) {
            final SPacketExplosion packet = (SPacketExplosion) event.getPacket();
            if (this.horizontalVelocity.getNumber() == 0 && this.verticalVelocity.getNumber() == 0) {
                event.cancel();
                return;
            }
            if (this.horizontalVelocity.getNumber() != 100) {
                packet.motionX = (float) (packet.motionX / 100 * this.horizontalVelocity.getNumber());
                packet.motionZ = (float) (packet.motionZ / 100 * this.horizontalVelocity.getNumber());
            }
            if (this.verticalVelocity.getNumber() != 100) {
                packet.motionY = (float) (packet.motionY / 100 * this.verticalVelocity.getNumber());
            }
        }

        if (event.getPacket() instanceof SPacketEntityVelocity) {
            final SPacketEntityVelocity packet = (SPacketEntityVelocity) event.getPacket();
            if (packet.getEntityID() == mc.player.getEntityId() && this.horizontalVelocity.getNumber() == 0 && this.verticalVelocity.getNumber() == 0) {
                event.cancel();
                return;
            }
            if (this.horizontalVelocity.getNumber() != 100) {
                packet.motionX = (int) (packet.motionX / 100 * this.horizontalVelocity.getNumber());
                packet.motionZ = (int) (packet.motionZ / 100 * this.horizontalVelocity.getNumber());
            }
            if (this.verticalVelocity.getNumber() != 100) {
                packet.motionY = (int) (packet.motionY / 100 * this.verticalVelocity.getNumber());
            }
        }
    });
}
