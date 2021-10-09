package com.doge.api.util;

import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRPC;
import club.minnced.discord.rpc.DiscordRichPresence;
import com.doge.api.Reference;

/**
 * @author Hoosiers
 * @since 10/27/2020
 */

public class Discord {

    private static String discordID = "896304179281281044";
    private static DiscordRichPresence discordRichPresence = new DiscordRichPresence();
    private static DiscordRPC discordRPC = DiscordRPC.INSTANCE;

    private static String clientVersion = "v" + Reference.VERSION;

    public static void startRPC() {
        DiscordEventHandlers eventHandlers = new DiscordEventHandlers();
        eventHandlers.disconnected = ((var1, var2) -> System.out.println("Discord RPC disconnected, var1: " + var1 + ", var2: " + var2));

        discordRPC.Discord_Initialize(discordID, eventHandlers, true, null);

        discordRichPresence.startTimestamp = System.currentTimeMillis() / 1000L;
        discordRichPresence.details = clientVersion;
        discordRichPresence.largeImageKey = "doge";
        discordRichPresence.largeImageText = "Doge on top!";
        discordRichPresence.state = null;
        discordRPC.Discord_UpdatePresence(discordRichPresence);
    }

    public static void stopRPC() {
        discordRPC.Discord_Shutdown();
        discordRPC.Discord_ClearPresence();
    }
}
