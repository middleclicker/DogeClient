package com.doge.client.module.modules.render;

import com.doge.api.event.events.RenderItemEvent;
import com.doge.api.setting.settings.NumberSetting;
import com.doge.client.module.Category;
import com.doge.client.module.Module;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumHandSide;

/**
 * Inspiration from Gamesense and credits to W+3 for settings.
 */

public class ViewModel extends Module {

    public NumberSetting mainX = new NumberSetting("MainX", this, 1.2, 0, 6, 0.1);
    public NumberSetting mainY = new NumberSetting("MainY", this, -0.95, -3.0, 3.0, 0.1);
    public NumberSetting mainZ = new NumberSetting("MainZ", this, -1.45, -5.0, 5.0, 0.1);
    public NumberSetting offX = new NumberSetting("OffX", this, -1.2, -6.0, 0.0, 0.1);
    public NumberSetting offY = new NumberSetting("OffY", this, -0.95, -3.0, 3.0, 0.1);
    public NumberSetting offZ = new NumberSetting("OffZ", this, -1.45, -5.0, 5.0, 0.1);
    public NumberSetting mainAngel = new NumberSetting("MainAngle", this, 0.0, 0.0, 360.0, 0.1);
    public NumberSetting mainRx = new NumberSetting("MainRotationPointX", this, 0.0, -1.0, 1.0, 0.1);
    public NumberSetting mainRy = new NumberSetting("MainRotationPointY", this, 0.0, -1.0, 1.0, 0.1);
    public NumberSetting mainRz = new NumberSetting("MainRotationPointZ", this, 0.0, -1.0, 1.0, 0.1);
    public NumberSetting offAngle = new NumberSetting("OffAngle", this, 0.0, 0.0, 360.0, 0.1);
    public NumberSetting offRx = new NumberSetting("OffRotationPointX", this, 0.0, -1.0, 1.0, 0.1);
    public NumberSetting offRy = new NumberSetting("OffRotationPointY", this, 0.0, -1.0, 1.0, 0.1);
    public NumberSetting offRz = new NumberSetting("OffRotationPointZ", this, 0.0, -1.0, 1.0, 0.1);
    public NumberSetting mainScaleX = new NumberSetting("MainScaleX", this, 1.0, -5.0, 10.0, 0.1);
    public NumberSetting mainScaleY = new NumberSetting("MainScaleY", this, 1.0, -5.0, 10.0, 0.1);
    public NumberSetting mainScaleZ = new NumberSetting("MainScaleZ", this, 1.0, -5.0, 10.0, 0.1);
    public NumberSetting offScaleX = new NumberSetting("OffScaleX", this, 1.0, -5.0, 10.0, 0.1);
    public NumberSetting offScaleY = new NumberSetting("OffScaleY", this, 1.0, -5.0, 10.0, 0.1);
    public NumberSetting offScaleZ = new NumberSetting("OffScaleZ", this, 1.0, -5.0, 10.0, 0.1);

    public ViewModel() {
        super("View Model", "Makes your hand look cool.", Category.RENDER);
        this.addSetting(mainX, mainY, mainZ, offX, offY, offZ, mainAngel, mainRx, mainRy, mainRz, offAngle, offRx, offRy, offRz, mainScaleX, mainScaleY, mainScaleZ, offScaleX, offScaleY, offScaleZ);
    }

    @SuppressWarnings("unused")
    @EventHandler
    private final Listener<RenderItemEvent> eventListener = new Listener<>(event -> {
        if (event.getEnumHandSide() == EnumHandSide.RIGHT) {
            GlStateManager.translate(mainX.getNumber(), mainY.getNumber(), mainZ.getNumber());
            GlStateManager.scale(mainScaleX.getNumber(), mainScaleY.getNumber(), mainScaleZ.getNumber());
            GlStateManager.rotate((float) mainAngel.getNumber(), (float) mainRx.getNumber(), (float) mainRy.getNumber(), (float) mainRz.getNumber());
        } else if (event.getEnumHandSide() == EnumHandSide.LEFT) {
            GlStateManager.translate(offX.getNumber(), offY.getNumber(), offZ.getNumber());
            GlStateManager.scale(offScaleX.getNumber(), offScaleY.getNumber(), offScaleZ.getNumber());
            GlStateManager.rotate((float) offAngle.getNumber(), (float) offRx.getNumber(), (float) offRy.getNumber(), (float) offRz.getNumber());
        }
    });
}
