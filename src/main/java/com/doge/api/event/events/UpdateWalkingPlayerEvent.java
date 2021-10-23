package com.doge.api.event.events;

import com.doge.api.event.Event;

public class UpdateWalkingPlayerEvent extends Event {
    public UpdateWalkingPlayerEvent(Era era) {
        super(era);
    }
}
