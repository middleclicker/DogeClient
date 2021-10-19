package com.doge.client.clickgui;

import com.doge.api.setting.Setting;
import com.doge.api.setting.settings.BooleanSetting;
import com.doge.api.setting.settings.ModeSetting;
import com.doge.api.setting.settings.NumberSetting;
import com.doge.api.setting.settings.ColorSetting;
import com.doge.api.util.color.DColor;
import com.doge.api.util.font.FontUtil;
import com.doge.client.clickgui.components.DogeColor;
import com.doge.client.clickgui.components.DogeKeybind;
import com.doge.client.manager.ModuleManager;
import com.doge.client.manager.SettingManager;
import com.doge.client.module.Category;
import com.doge.client.module.HUDModule;
import com.doge.client.module.Module;
import com.doge.client.module.modules.client.ClickGuiModule;
import com.doge.client.module.modules.client.ColorMain;
import com.lukflug.panelstudio.CollapsibleContainer;
import com.lukflug.panelstudio.DraggableContainer;
import com.lukflug.panelstudio.FixedComponent;
import com.lukflug.panelstudio.SettingsAnimation;
import com.lukflug.panelstudio.hud.HUDClickGUI;
import com.lukflug.panelstudio.hud.HUDPanel;
import com.lukflug.panelstudio.mc12.GLInterface;
import com.lukflug.panelstudio.mc12.MinecraftHUDGUI;
import com.lukflug.panelstudio.settings.*;
import com.lukflug.panelstudio.theme.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class DogeGUI extends MinecraftHUDGUI {
    public static final int WIDTH = 100, HEIGHT = 12, DISTANCE = 10, HUD_BORDER = 2;
    private final Toggleable colorToggle;
    public final GUIInterface guiInterface;
    public final HUDClickGUI gui;
    private final Theme theme, gameSenseTheme, clearTheme, clearGradientTheme;

    // TODO: Add more themes, hopefully a custom one.

    public DogeGUI() {
        ClickGuiModule clickGuiModule = (ClickGuiModule) ModuleManager.getModuleByName("Click GUI");
        ColorMain colorMain = (ColorMain) ModuleManager.getModuleByName("Colors");
        ColorScheme scheme = new SettingsColorScheme(clickGuiModule.enabledColor, clickGuiModule.backgroundColor, clickGuiModule.settingBackgroundColor, clickGuiModule.outlineColor, clickGuiModule.fontColor, clickGuiModule.opacity);
        gameSenseTheme = new GameSenseTheme(scheme, HEIGHT, 2, 5);
        clearTheme = new ClearTheme(scheme, false, HEIGHT, 1);
        clearGradientTheme = new ClearTheme(scheme, true, HEIGHT, 1);
        theme = new ThemeMultiplexer() {
            @Override
            protected Theme getTheme() {
                if (clickGuiModule.theme.getValueName().equals("Clear Theme")) return clearTheme;
                else if (clickGuiModule.theme.getValueName().equals("Clear Gradient Theme")) return clearGradientTheme;
                else return gameSenseTheme;
            }
        };
        colorToggle = new Toggleable() {
            @Override
            public void toggle() {
                colorMain.colorModel.increment();
            }

            @Override
            public boolean isOn() {
                return colorMain.colorModel.getValueName().equals("HSB");
            }
        };
        guiInterface = new GUIInterface(true) {
            @Override
            public void drawString(Point pos, String s, Color c) {
                GLInterface.end();
                int x = pos.x + 2, y = pos.y + 1;
                if (!colorMain.customFont.isOn()) {
                    x += 1;
                    y += 1;
                }
                FontUtil.drawStringWithShadow(colorMain.customFont.isOn(), s, x, y, new DColor(c));
                GLInterface.begin();
            }

            @Override
            public int getFontWidth(String s) {
                return Math.round(FontUtil.getStringWidth(colorMain.customFont.isOn(), s)) + 4;
            }

            @Override
            public int getFontHeight() {
                return Math.round(FontUtil.getFontHeight(colorMain.customFont.isOn())) + 2;
            }

            @Override
            public String getResourcePrefix() {
                return "doge:gui/";
            }
        };
        gui = new HUDClickGUI(guiInterface, null) {
            @Override
            public void handleScroll(int diff) {
                super.handleScroll(diff);
                if (clickGuiModule.scrolling.getValueName().equals("Screen")) {
                    for (FixedComponent component : components) {
                        if (!hudComponents.contains(component)) {
                            Point p = component.getPosition(guiInterface);
                            p.translate(0, -diff);
                            component.setPosition(guiInterface, p);
                        }
                    }
                }
            }
        };
        Toggleable hudToggle = new Toggleable() {
            @Override
            public void toggle() {
                render();
            }

            @Override
            public boolean isOn() {
                return hudEditor;
            }
        };
        for (Module m : ModuleManager.getModules()) {
            if (m instanceof HUDModule) {
                ((HUDModule) m).populate(theme);
                gui.addHUDComponent(new HUDPanel(((HUDModule) m).getComponent(), theme.getPanelRenderer(), m, new SettingsAnimation(clickGuiModule.animationSpeed), hudToggle, HUD_BORDER));
            }
        }
        Point pos = new Point(DISTANCE, DISTANCE);
        for (Category category : Category.values()) {
            DraggableContainer panel = new DraggableContainer(category.name(), null, theme.getPanelRenderer(), new SimpleToggleable(false), new SettingsAnimation(clickGuiModule.animationSpeed), null, new Point(pos), WIDTH) {

                @Override
                protected int getScrollHeight(int childHeight) {
                    if (clickGuiModule.scrolling.getValueName().equals("Screen")) {
                        return childHeight;
                    }
                    return Math.min(childHeight, Math.max(HEIGHT * 4, DogeGUI.this.height - getPosition(guiInterface).y - renderer.getHeight(open.getValue() != 0) - HEIGHT));
                }
            };
            gui.addComponent(panel);
            pos.translate(WIDTH + DISTANCE, 0);
            for (Module module : ModuleManager.getModulesByCategory(category)) {
                addModule(panel, module);
            }
        }
    }

    private void addModule(CollapsibleContainer panel, Module module) {
        ClickGuiModule clickGuiModule = (ClickGuiModule) ModuleManager.getModuleByName("Click GUI");
        CollapsibleContainer container = new CollapsibleContainer(module.name, null, theme.getContainerRenderer(), new SimpleToggleable(false), new SettingsAnimation(clickGuiModule.animationSpeed), module);
        panel.addComponent(container);
        for (Setting property : SettingManager.getSettingsInModule(module)) {
            if (property instanceof BooleanSetting) {
                container.addComponent(new BooleanComponent(property.name, null, theme.getComponentRenderer(), (BooleanSetting) property));
            } else if (property instanceof NumberSetting) {
                container.addComponent(new NumberComponent(property.name, null, theme.getComponentRenderer(), (NumberSetting) property, ((NumberSetting) property).getMinimumValue(), ((NumberSetting) property).getMaximumValue()));
            } else if (property instanceof ModeSetting) {
                container.addComponent(new EnumComponent(property.name, null, theme.getComponentRenderer(), (ModeSetting) property));
            } else if (property instanceof ColorSetting) {
                container.addComponent(new DogeColor(theme, (ColorSetting) property, colorToggle, new SettingsAnimation(clickGuiModule.animationSpeed)));
            }
        }
        container.addComponent(new DogeKeybind(theme.getComponentRenderer(), module));
    }

    public static void renderItem(ItemStack item, Point pos) {
        GlStateManager.enableTexture2D();
        GlStateManager.depthMask(true);
        GL11.glPushAttrib(GL11.GL_SCISSOR_BIT);
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
        GlStateManager.clear(GL11.GL_DEPTH_BUFFER_BIT);
        GL11.glPopAttrib();
        GlStateManager.enableDepth();
        GlStateManager.disableAlpha();
        GlStateManager.pushMatrix();
        Minecraft.getMinecraft().getRenderItem().zLevel = -150.0f;
        RenderHelper.enableGUIStandardItemLighting();
        Minecraft.getMinecraft().getRenderItem().renderItemAndEffectIntoGUI(item, pos.x, pos.y);
        Minecraft.getMinecraft().getRenderItem().renderItemOverlays(Minecraft.getMinecraft().fontRenderer, item, pos.x, pos.y);
        RenderHelper.disableStandardItemLighting();
        Minecraft.getMinecraft().getRenderItem().zLevel = 0.0F;
        GlStateManager.popMatrix();
        GlStateManager.disableDepth();
        GlStateManager.depthMask(false);
        GLInterface.begin();
    }

    public static void renderEntity(EntityLivingBase entity, Point pos, int scale) {
        GlStateManager.enableTexture2D();
        GlStateManager.depthMask(true);
        GL11.glPushAttrib(GL11.GL_SCISSOR_BIT);
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
        GlStateManager.clear(GL11.GL_DEPTH_BUFFER_BIT);
        GL11.glPopAttrib();
        GlStateManager.enableDepth();
        GlStateManager.disableAlpha();
        GlStateManager.pushMatrix();
        GlStateManager.color(1, 1, 1, 1);
        GuiInventory.drawEntityOnScreen(pos.x, pos.y, scale, 28, 60, entity);
        GlStateManager.popMatrix();
        GlStateManager.disableDepth();
        GlStateManager.depthMask(false);
        GLInterface.begin();
    }
    @Override
    protected HUDClickGUI getHUDGUI() {
        return gui;
    }

    @Override
    protected GUIInterface getInterface() {
        return guiInterface;
    }

    @Override
    protected int getScrollSpeed() {
        ClickGuiModule clickGuiModule = (ClickGuiModule) ModuleManager.getModuleByName("Click GUI");
        return (int) clickGuiModule.scrollSpeed.getNumber();
    }
}
