package com.doge.client;

import com.doge.api.Reference;
import com.doge.api.config.SaveLoadConfig;
import com.doge.api.event.EventProcessor;
import com.doge.api.proxy.CommonProxy;
import com.doge.api.util.font.CFontRenderer;
import com.doge.client.clickgui.DogeGUI;
import com.doge.client.friend.FriendManager;
import com.doge.client.manager.ModuleManager;
import com.doge.client.manager.SettingManager;
import me.zero.alpine.EventBus;
import me.zero.alpine.EventManager;
import net.minecraft.client.Minecraft;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.Display;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

@Mod(modid=Reference.MODID, name=Reference.NAME, version=Reference.VERSION)
public class Main {
    public static final Logger LOGGER = LogManager.getLogger(Reference.NAME);
    public static final EventBus EVENT_BUS = new EventManager();

    public static ModuleManager MODULE_MANAGER;
    public static SettingManager SETTING_MANAGER;
    public static SaveLoadConfig SAVELOAD_CONFIG;
    public FriendManager FRIEND_MANAGER;
    public EventProcessor EVENT_PROCESSOR;
    public CFontRenderer cFontRenderer;
    public DogeGUI dogeGUI;

    @Mod.Instance
    public static Main INSTANCE;

    public Main() {
        INSTANCE = this;
    }

    @SidedProxy(clientSide = Reference.CLIENT_PROXY_CLASS, serverSide = Reference.COMMON_PROXY_CLASS)
    public static CommonProxy proxy;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) { }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        LOGGER.info("Starting initilization for " + Reference.NAME);

        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(INSTANCE);

        Minecraft.getMinecraft().gameSettings.setSoundLevel(SoundCategory.MASTER, 0);
        LOGGER.info("Set master sound level to 0");

        MODULE_MANAGER = new ModuleManager();
        LOGGER.info("Loaded Module Manager");
        SETTING_MANAGER = new SettingManager();
        LOGGER.info("Loaded Setting Manager");
        EVENT_PROCESSOR = new EventProcessor();
        LOGGER.info("Loaded Event Processor");
        SAVELOAD_CONFIG = new SaveLoadConfig();
        LOGGER.info("Loaded Save Load Config");
        SAVELOAD_CONFIG.load();
        LOGGER.info("Loaded Configs");
        SAVELOAD_CONFIG.loadFriends();
        LOGGER.info("Loaded Friends");
        cFontRenderer = new CFontRenderer(new Font("Verdana", Font.PLAIN, 18), true, true);
        LOGGER.info("Loaded CFontRenderer");
        dogeGUI = new DogeGUI();
        LOGGER.info("Loaded Doge GUI");
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        Display.setTitle(Reference.NAME + " v" + Reference.VERSION);
        LOGGER.info("Set title for " + Reference.NAME);
        LOGGER.info("Finished initialization for " + Reference.NAME + " v" + Reference.VERSION);
    }

}
