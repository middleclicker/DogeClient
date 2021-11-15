package com.doge.client.clickgui.components;

import com.doge.api.setting.settings.ColorSetting;
import com.doge.client.manager.ModuleManager;
import com.doge.client.module.modules.client.ClickGuiModule;
import com.lukflug.panelstudio.Animation;
import com.lukflug.panelstudio.Context;
import com.lukflug.panelstudio.FocusableComponent;
import com.lukflug.panelstudio.Interface;
import com.lukflug.panelstudio.settings.ColorComponent;
import com.lukflug.panelstudio.settings.Toggleable;
import com.lukflug.panelstudio.theme.Renderer;
import com.lukflug.panelstudio.theme.Theme;
import net.minecraft.util.text.TextFormatting;

public class DogeColor extends ColorComponent {

    public DogeColor(Theme theme, ColorSetting setting, Toggleable colorToggle, Animation animation) {
        super(TextFormatting.BOLD + setting.name, null, theme.getContainerRenderer(), animation, theme.getComponentRenderer(), setting, false, true, colorToggle);
        ClickGuiModule clickGuiModule = (ClickGuiModule) ModuleManager.getModuleByName("Click GUI");
        if (setting != clickGuiModule.enabledColor) addComponent(new SyncButton(theme.getComponentRenderer()));
    }

    private class SyncButton extends FocusableComponent {

        public SyncButton(Renderer renderer) {
            super("Sync Color", null, renderer);
        }

        @Override
        public void render(Context context) {
            super.render(context);
            renderer.overrideColorScheme(overrideScheme);
            renderer.renderTitle(context, title, hasFocus(context), false);
            renderer.restoreColorScheme();
        }

        @Override
        public void handleButton(Context context, int button) {
            super.handleButton(context, button);
            ClickGuiModule clickGuiModule = (ClickGuiModule) ModuleManager.getModuleByName("Click GUI");
            if (button == Interface.LBUTTON && context.isClicked()) {
                setting.setValue(clickGuiModule.enabledColor.getColor());
                setting.setRainbow(clickGuiModule.enabledColor.getRainbow());
            }
        }
    }
}
