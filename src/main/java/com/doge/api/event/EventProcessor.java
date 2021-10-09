package com.doge.api.event;

import com.doge.api.Global;
import com.doge.client.Main;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class EventProcessor implements Global {

    public EventProcessor() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (mc.player == null) { return; }
        Main.INSTANCE.MODULE_MANAGER.onTick(event);
    }

    @SubscribeEvent
    public void onWorldRender(RenderWorldLastEvent event) {
        if (event.isCanceled()) {
            return;
        }
        Main.EVENT_BUS.post(event);
    }

    @SubscribeEvent
    public void onRender(RenderGameOverlayEvent event) {
        Main.EVENT_BUS.post(event);
    }

    @SubscribeEvent
    public void onKey(InputEvent.KeyInputEvent event) {
        Main.EVENT_BUS.post(event);
    }

}
