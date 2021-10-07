package com.doge.api.event;

import com.doge.api.Global;
import me.zero.alpine.type.Cancellable;

public class Event extends Cancellable implements Global {

    private Era era;
    private float partialTicks;

    public Event() {
        this.partialTicks = mc.getRenderPartialTicks();
    }

    public Event(Era era) {
        this.era = era;
        this.partialTicks = mc.getRenderPartialTicks();
    }

    public enum Era {
        PRE,
        POST,
        PERI
    }

    public Era getEra() {
        return era;
    }

    public void setEra(Era era) {
        this.era = era;
    }

    public float getPartialTicks() {
        return partialTicks;
    }

    public void setPartialTicks(float partialTicks) {
        this.partialTicks = partialTicks;
    }
}
