package com.doge.api.util.color;

import com.doge.api.setting.settings.ModeSetting;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.util.text.TextFormatting;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

public class ColorUtil {

    public static List<String> colors = Arrays.asList("Black", "Dark Green", "Dark Red", "Gold", "Dark Gray", "Green", "Red", "Yellow", "Dark Blue", "Dark Aqua", "Dark Purple", "Gray", "Blue", "Aqua", "Light Purple", "White");

    public static TextFormatting settingToTextFormatting(ModeSetting setting) {
        if (setting.getValueName().equalsIgnoreCase("Black")) {
            return TextFormatting.BLACK;
        }
        if (setting.getValueName().equalsIgnoreCase("Dark Green")) {
            return TextFormatting.DARK_GREEN;
        }
        if (setting.getValueName().equalsIgnoreCase("Dark Red")) {
            return TextFormatting.DARK_RED;
        }
        if (setting.getValueName().equalsIgnoreCase("Gold")) {
            return TextFormatting.GOLD;
        }
        if (setting.getValueName().equalsIgnoreCase("Dark Gray")) {
            return TextFormatting.DARK_GRAY;
        }
        if (setting.getValueName().equalsIgnoreCase("Green")) {
            return TextFormatting.GREEN;
        }
        if (setting.getValueName().equalsIgnoreCase("Red")) {
            return TextFormatting.RED;
        }
        if (setting.getValueName().equalsIgnoreCase("Yellow")) {
            return TextFormatting.YELLOW;
        }
        if (setting.getValueName().equalsIgnoreCase("Dark Blue")) {
            return TextFormatting.DARK_BLUE;
        }
        if (setting.getValueName().equalsIgnoreCase("Dark Aqua")) {
            return TextFormatting.DARK_AQUA;
        }
        if (setting.getValueName().equalsIgnoreCase("Dark Purple")) {
            return TextFormatting.DARK_PURPLE;
        }
        if (setting.getValueName().equalsIgnoreCase("Gray")) {
            return TextFormatting.GRAY;
        }
        if (setting.getValueName().equalsIgnoreCase("Blue")) {
            return TextFormatting.BLUE;
        }
        if (setting.getValueName().equalsIgnoreCase("Light Purple")) {
            return TextFormatting.LIGHT_PURPLE;
        }
        if (setting.getValueName().equalsIgnoreCase("White")) {
            return TextFormatting.WHITE;
        }
        if (setting.getValueName().equalsIgnoreCase("Aqua")) {
            return TextFormatting.AQUA;
        }
        return null;
    }

    public static ChatFormatting textToChatFormatting(ModeSetting setting) {
        if (setting.getValueName().equalsIgnoreCase("Black")) {
            return ChatFormatting.BLACK;
        }
        if (setting.getValueName().equalsIgnoreCase("Dark Green")) {
            return ChatFormatting.DARK_GREEN;
        }
        if (setting.getValueName().equalsIgnoreCase("Dark Red")) {
            return ChatFormatting.DARK_RED;
        }
        if (setting.getValueName().equalsIgnoreCase("Gold")) {
            return ChatFormatting.GOLD;
        }
        if (setting.getValueName().equalsIgnoreCase("Dark Gray")) {
            return ChatFormatting.DARK_GRAY;
        }
        if (setting.getValueName().equalsIgnoreCase("Green")) {
            return ChatFormatting.GREEN;
        }
        if (setting.getValueName().equalsIgnoreCase("Red")) {
            return ChatFormatting.RED;
        }
        if (setting.getValueName().equalsIgnoreCase("Yellow")) {
            return ChatFormatting.YELLOW;
        }
        if (setting.getValueName().equalsIgnoreCase("Dark Blue")) {
            return ChatFormatting.DARK_BLUE;
        }
        if (setting.getValueName().equalsIgnoreCase("Dark Aqua")) {
            return ChatFormatting.DARK_AQUA;
        }
        if (setting.getValueName().equalsIgnoreCase("Dark Purple")) {
            return ChatFormatting.DARK_PURPLE;
        }
        if (setting.getValueName().equalsIgnoreCase("Gray")) {
            return ChatFormatting.GRAY;
        }
        if (setting.getValueName().equalsIgnoreCase("Blue")) {
            return ChatFormatting.BLUE;
        }
        if (setting.getValueName().equalsIgnoreCase("Light Purple")) {
            return ChatFormatting.LIGHT_PURPLE;
        }
        if (setting.getValueName().equalsIgnoreCase("White")) {
            return ChatFormatting.WHITE;
        }
        if (setting.getValueName().equalsIgnoreCase("Aqua")) {
            return ChatFormatting.AQUA;
        }
        return null;
    }

    public static Color settingToColor(ModeSetting setting) {
        if (setting.getValueName().equalsIgnoreCase("Black")) {
            return Color.BLACK;
        }
        if (setting.getValueName().equalsIgnoreCase("Dark Green")) {
            return Color.GREEN.darker();
        }
        if (setting.getValueName().equalsIgnoreCase("Dark Red")) {
            return Color.RED.darker();
        }
        if (setting.getValueName().equalsIgnoreCase("Gold")) {
            return Color.yellow.darker();
        }
        if (setting.getValueName().equalsIgnoreCase("Dark Gray")) {
            return Color.DARK_GRAY;
        }
        if (setting.getValueName().equalsIgnoreCase("Green")) {
            return Color.green;
        }
        if (setting.getValueName().equalsIgnoreCase("Red")) {
            return Color.red;
        }
        if (setting.getValueName().equalsIgnoreCase("Yellow")) {
            return Color.yellow;
        }
        if (setting.getValueName().equalsIgnoreCase("Dark Blue")) {
            return Color.blue.darker();
        }
        if (setting.getValueName().equalsIgnoreCase("Dark Aqua")) {
            return Color.CYAN.darker();
        }
        if (setting.getValueName().equalsIgnoreCase("Dark Purple")) {
            return Color.MAGENTA.darker();
        }
        if (setting.getValueName().equalsIgnoreCase("Gray")) {
            return Color.GRAY;
        }
        if (setting.getValueName().equalsIgnoreCase("Blue")) {
            return Color.blue;
        }
        if (setting.getValueName().equalsIgnoreCase("Light Purple")) {
            return Color.magenta;
        }
        if (setting.getValueName().equalsIgnoreCase("White")) {
            return Color.WHITE;
        }
        if (setting.getValueName().equalsIgnoreCase("Aqua")) {
            return Color.cyan;
        }
        return Color.WHITE;
    }
}
