package com.doge.client.module.modules.client;

import com.doge.api.util.Discord;
import com.doge.client.module.Category;
import com.doge.client.module.Module;

public class DiscordRPC extends Module {
    public DiscordRPC() {
        super("Discord RPC", "Show to your friends how cool you are.", Category.CLIENT);
    }

    @Override
    public void onEnable() {
        Discord.startRPC();
    }

    @Override
    public void onDisable() {
        Discord.stopRPC();
    }
}
