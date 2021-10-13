package com.doge.api.util;

import com.doge.api.Global;
import com.doge.api.Reference;
import com.doge.client.Main;
import com.doge.client.manager.ModuleManager;
import com.doge.client.module.Module;
import com.doge.client.module.modules.client.ToggleMessages;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentBase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClientMessage implements Global {

    private static final String opener = ChatFormatting.GOLD + Reference.NAME + ChatFormatting.WHITE + " : " + ChatFormatting.RESET;

    public static void sendToggleMessage(Module m, boolean enabled) {
        if(mc.world != null && mc.player != null) {
            if(ModuleManager.getModuleByName("Toggle Messages").isOn()) {
                ChatFormatting open = (enabled ? ChatFormatting.YELLOW : ChatFormatting.WHITE);
                if (m.name.equals("Click GUI") || m.name.equals("Toggle Messages")) { return; }
                sendMessage(open + m.name);
            }
        }
    }

    public static void sendMessage(String message) {
        sendClientMessage(opener + message);
    }

    private static void sendClientMessage(String message) {
        if (mc.player != null) {
            mc.player.sendMessage(new ChatMessage(message));
        }
    }

    public static class ChatMessage extends TextComponentBase {
        String message_input;

        public ChatMessage(String message) {
            Pattern p       = Pattern.compile("&[0123456789abcdefrlosmk]");
            Matcher m       = p.matcher(message);
            StringBuffer sb = new StringBuffer();

            while (m.find()) {
                String replacement = "\u00A7" + m.group().substring(1);
                m.appendReplacement(sb, replacement);
            }

            m.appendTail(sb);
            this.message_input = sb.toString();
        }

        public String getUnformattedComponentText() {
            return this.message_input;
        }

        @Override
        public ITextComponent createCopy() {
            return new ChatMessage(this.message_input);
        }
    }
}
