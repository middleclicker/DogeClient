package com.doge.mixin.mixins;

import com.doge.api.Reference;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;

@Mixin({GuiMainMenu.class})
public class MixinGuiMainMenu extends GuiScreen {

    @Inject(method = {"drawScreen"}, at = {@At("TAIL")}, cancellable = true)
    public void drawText(CallbackInfo ci) {
        Gui.drawRect(0, 0, mc.fontRenderer.getStringWidth(Reference.NAME + " | v" + Reference.VERSION) + 2, 11, 0x90000000);
        GL11.glLineWidth(1f);
        mc.fontRenderer.drawStringWithShadow(Reference.NAME + " | v" + Reference.VERSION, 1, 1, new Color(225,179,3).getRGB());
    }

}
