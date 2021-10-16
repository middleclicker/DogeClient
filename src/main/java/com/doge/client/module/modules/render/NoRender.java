package com.doge.client.module.modules.render;

import com.doge.api.event.events.PacketEvent;
import com.doge.api.setting.settings.BooleanSetting;
import com.doge.api.setting.settings.NumberSetting;
import com.doge.client.module.Category;
import com.doge.client.module.Module;
import me.zero.alpine.listener.Listener;
import net.minecraft.network.play.server.SPacketTimeUpdate;

import static net.minecraft.init.MobEffects.BLINDNESS;
import static net.minecraft.init.MobEffects.NAUSEA;

public class NoRender extends Module {

    public BooleanSetting armour = new BooleanSetting("Armour", this, true);
    public BooleanSetting fire = new BooleanSetting("Fire", this, true);
    public BooleanSetting blind = new BooleanSetting("Blindness", this, true);
    public BooleanSetting nausea = new BooleanSetting("Nausea", this, true);
    public BooleanSetting hurt = new BooleanSetting("Hurt Cam", this, true);
    public BooleanSetting weather = new BooleanSetting("Weather", this, true);
    public BooleanSetting time = new BooleanSetting("Time", this, true);
    public NumberSetting setTime = new NumberSetting("Set Time", this, 0, 0, 23000, 1);

    public NoRender() {
        super("No Render", "Doesn't render certain things." , Category.RENDER);
        this.addSetting(armour, fire, blind, nausea, hurt, weather, time, setTime);
    }

    @Override
    public void onUpdate() {
        if (mc.player == null || mc.world == null) {
            return;
        }
        if (blind.isOn() && mc.player.isPotionActive(BLINDNESS)) {
            mc.player.removePotionEffect(BLINDNESS);
        }
        if (blind.isOn() && mc.player.isPotionActive(NAUSEA)) {
            mc.player.removePotionEffect(NAUSEA);
        }
        if (weather.isOn()) {
            mc.world.setRainStrength(0f);
        }
        if (time.isOn()) {
            mc.world.setWorldTime((long) setTime.getNumber());
            // TODO: Fix bug where the sky will flash when time is on.
        }
    }

    private final Listener<PacketEvent.Receive> recieveListener = new Listener<>(event -> {
        if (event.getPacket() instanceof SPacketTimeUpdate && time.isOn()) {
            event.cancel();
        }
    });
}
