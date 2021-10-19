package com.doge.api;

import net.minecraft.client.Minecraft;

import java.util.Random;

public interface Global {
    public static final Minecraft mc = Minecraft.getMinecraft();
    Random random = new Random();
}
