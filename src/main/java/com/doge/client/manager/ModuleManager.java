package com.doge.client.manager;

import com.doge.client.Main;
import com.doge.client.module.Category;
import com.doge.client.module.Module;
import com.doge.client.module.modules.client.*;
import com.doge.client.module.modules.combat.AutoArmour;
import com.doge.client.module.modules.hud.Ping;
import com.doge.client.module.modules.misc.AutoDisconnect;
import com.doge.client.module.modules.misc.FakePlayer;
import com.doge.client.module.modules.misc.FastUse;
import com.doge.client.module.modules.exploit.XCarry;
import com.doge.client.module.modules.misc.YawLock;
import com.doge.client.module.modules.movement.*;
import com.doge.client.module.modules.render.Fov;
import com.doge.client.module.modules.render.Fullbright;
import com.doge.client.module.modules.render.Tracer;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;

public class ModuleManager {
    public static ArrayList<Module> modules = new ArrayList<>();

    public ModuleManager() {
        MinecraftForge.EVENT_BUS.register(this);
        Main.EVENT_BUS.subscribe(this);

        // Combat
        modules.add(new AutoArmour());

        // Exploit
        modules.add(new XCarry());

        // Movement
        modules.add(new Step());
        modules.add(new ReverseStep());
        modules.add(new Sprint());
        modules.add(new Fly());
        modules.add(new Velocity());

        // Misc
        modules.add(new FastUse());
        modules.add(new YawLock());
        modules.add(new FakePlayer());
        modules.add(new AutoDisconnect());

        // Render
        modules.add(new Fullbright());
        modules.add(new Fov());
        modules.add(new Tracer());

        // Client
        modules.add(new ClickGuiModule());
        modules.add(new ColorMain());
        modules.add(new DiscordRPC());
        modules.add(new ToggleMessages());
        modules.add(new HUDEditor());

        // Hud
        modules.add(new Ping());
    }

    public static ArrayList<Module> getModulesByCategory(Category c) {
        ArrayList<Module> output = new ArrayList<>();
        for (Module m : modules) {
            if (m.category == c) {
                output.add(m);
            }
        }
        return output;
    }

    public static Module getModuleByName(String name) {
        for (Module m : modules) {
            if (m.name.equalsIgnoreCase(name)) {
                return m;
            }
        }
        return null;
    }

    public static ArrayList<Module> getModules() {
        return modules;
    }

    @SubscribeEvent
    public void onKey(InputEvent.KeyInputEvent event) {
        if (Keyboard.getEventKeyState()) {
            for (Module m : modules) {
                if (m.key == Keyboard.getEventKey()) {
                    m.toggle();
                }
            }
        }
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        for (Module m : modules) {
            if (m.isOn()) {
                m.onUpdate();
            }
        }
    }

    @SubscribeEvent
    public void onRender(RenderWorldLastEvent event) {
        for (Module m : modules) {
            if (m.isOn()) {
                m.onRender();
            }
        }
    }
}
