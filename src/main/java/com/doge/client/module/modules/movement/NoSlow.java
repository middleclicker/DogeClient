package com.doge.client.module.modules.movement;

import com.doge.api.setting.settings.BooleanSetting;
import com.doge.client.module.Category;
import com.doge.client.module.Module;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraftforge.client.event.InputUpdateEvent;

public class NoSlow extends Module {

    public static NoSlow INSTANCE = new NoSlow();

    public final BooleanSetting soulsand = new BooleanSetting("Soul Sand", this, true);
    public final BooleanSetting slime = new BooleanSetting("Slime", this, true);
    public final BooleanSetting web = new BooleanSetting("Cob Web", this, true);
    public final BooleanSetting food = new BooleanSetting("Food", this, true);

    public NoSlow() {
        super("No Slow", "Prevents slowdown.", Category.MOVEMENT);
        this.addSetting(soulsand, slime, web, food);
    }

    @EventHandler
    private final Listener<InputUpdateEvent> eventListener = new Listener<>(event -> {
        if (mc.player.isHandActive() && !mc.player.isRiding() && food.isOn()) {
            event.getMovementInput().moveStrafe *= 5;
            event.getMovementInput().moveForward *= 5;
        }
    });
}
