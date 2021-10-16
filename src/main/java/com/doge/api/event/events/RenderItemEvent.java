package com.doge.api.event.events;

import com.doge.api.event.Event;
import net.minecraft.util.EnumHandSide;

public class RenderItemEvent extends Event {
    private final EnumHandSide enumHandSide;

    public RenderItemEvent(EnumHandSide enumHandSide) {
        this.enumHandSide = enumHandSide;
    }

    public EnumHandSide getEnumHandSide() {
        return this.enumHandSide;
    }
}
