package com.doge.api.util.color;

import net.minecraft.client.renderer.GlStateManager;

import java.awt.*;

public class DColor extends Color {

    private static final long serialVersionUID = 1L;

    public DColor(int rgb) {
        super(rgb);
    }

    public DColor(int rgba, boolean hasalpha) {
        super(rgba,hasalpha);
    }

    public DColor(int r, int g, int b) {
        super(r,g,b);
    }

    public DColor(int r, int g, int b, int a) {
        super(r,g,b,a);
    }

    public DColor(Color color) {
        super(color.getRed(),color.getGreen(),color.getBlue(),color.getAlpha());
    }

    public DColor(DColor color, int a) {
        super(color.getRed(),color.getGreen(),color.getBlue(),a);
    }

    public static DColor fromHSB (float hue, float saturation, float brightness) {
        return new DColor(Color.getHSBColor(hue,saturation,brightness));
    }

    public float getHue() {
        return RGBtoHSB(getRed(),getGreen(),getBlue(),null)[0];
    }

    public float getSaturation() {
        return RGBtoHSB(getRed(),getGreen(),getBlue(),null)[1];
    }

    public float getBrightness() {
        return RGBtoHSB(getRed(),getGreen(),getBlue(),null)[2];
    }

    public void glColor() {
        GlStateManager.color(getRed()/255.0f,getGreen()/255.0f,getBlue()/255.0f,getAlpha()/255.0f);
    }

}
