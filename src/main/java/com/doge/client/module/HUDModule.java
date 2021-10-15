package com.doge.client.module;

import com.doge.client.Main;
import com.lukflug.panelstudio.FixedComponent;
import com.lukflug.panelstudio.theme.Theme;

import java.awt.*;

public abstract class HUDModule extends Module {

    protected FixedComponent component;
    protected Point position;

    public HUDModule (String title, String description, Point defaultPos, Category category) {
        super(title, description, category);
        position = defaultPos;
    }

    public abstract void populate (Theme theme);

    public FixedComponent getComponent() {
        return component;
    }

    public void resetPosition() {
        component.setPosition(Main.INSTANCE.dogeGUI.guiInterface,position);
    }
}
