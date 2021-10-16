package com.doge.api.util.font;

import com.doge.api.Global;
import com.doge.api.util.color.DColor;
import com.doge.client.Main;

public class FontUtil implements Global {
    public static float drawStringWithShadow(boolean customFont, String text, int x, int y, DColor color) {
        if (customFont) {
            return Main.INSTANCE.cFontRenderer.drawStringWithShadow(text, x, y, color);
        } else {
            return mc.fontRenderer.drawStringWithShadow(text, x, y, color.getRGB());
        }
    }

    public static int getStringWidth(boolean customFont, String string) {
        if (customFont) {
            return Main.INSTANCE.cFontRenderer.getStringWidth(string);
        } else {
            return mc.fontRenderer.getStringWidth(string);
        }
    }

    public static int getFontHeight(boolean customFont) {
        if (customFont) {
            return Main.INSTANCE.cFontRenderer.getHeight();
        } else {
            return mc.fontRenderer.FONT_HEIGHT;
        }
    }
}
