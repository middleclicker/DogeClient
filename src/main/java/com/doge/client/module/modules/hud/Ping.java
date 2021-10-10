package com.doge.client.module.modules.hud;

import com.doge.api.setting.settings.BooleanSetting;
import com.doge.api.setting.settings.ColorSetting;
import com.doge.api.util.color.DColor;
import com.doge.client.module.Category;
import com.doge.client.module.HUDModule;
import com.lukflug.panelstudio.hud.HUDList;
import com.lukflug.panelstudio.hud.ListComponent;
import com.lukflug.panelstudio.theme.Theme;
import com.mojang.realmsclient.gui.ChatFormatting;

import java.awt.*;

public class Ping extends HUDModule {
    public ColorSetting color = new ColorSetting("Color", this, new DColor(230, 0, 0, 255));
    public BooleanSetting sort = new BooleanSetting("Sort Right", this, false);

    public Ping() {
        super("Ping", "Shows your ping", new Point(-2,19), Category.HUD);
        this.addSetting(color, sort);
    }

    @Override
    public void populate(Theme theme) {
        component = new ListComponent(getName(), theme.getPanelRenderer(), position, new PingList());
    }

    private static int getPing () {
        int p = -1;
        if (mc.player == null || mc.getConnection() == null || mc.getConnection().getPlayerInfo(mc.player.getName()) == null) {
            p = -1;
        } else {
            p = mc.getConnection().getPlayerInfo(mc.player.getName()).getResponseTime();
        }
        return p;
    }

    private class PingList implements HUDList {

        @Override
        public int getSize() {
            return 1;
        }

        @Override
        public String getItem(int index) {
            if(getPing() >= 200) return "Ping " + getPing();
            else return ChatFormatting.WHITE + "Ping " + getPing();
        }

        @Override
        public Color getItemColor(int index) {
            return color.getValue();
        }

        @Override
        public boolean sortUp() {
            return false;
        }

        @Override
        public boolean sortRight() {
            return sort.isOn();
        }
    }
}
