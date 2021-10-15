package com.doge.client.module.modules.combat;

import com.doge.api.setting.settings.BooleanSetting;
import com.doge.api.setting.settings.NumberSetting;
import com.doge.api.util.Timer;
import com.doge.client.module.Category;
import com.doge.client.module.Module;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Mouse;

public class AutoClicker extends Module {

    public BooleanSetting left = new BooleanSetting("Left", this, true);
    // Removed right click because that is basically fast use
    public BooleanSetting randomize = new BooleanSetting("Randomize", this, false);
    public NumberSetting interval = new NumberSetting("Interval", this, 0.1, 0, 1, 0.1);

    private final Timer timer = new Timer();

    public AutoClicker() {
        super("Auto Clicker", "Automatically clicks when mouse is held down.", Category.COMBAT);
        this.addSetting(left, randomize, interval);
    }

    @Override
    public void onEnable() {
        timer.reset();
    }

    @Override
    public void onUpdate() {
        double randomNumber = getRandomValue(-0.05, 0.05);
        if (left.isOn() && Mouse.isButtonDown(0)) {
            if (randomize.isOn()) {
                if (timer.passedMs((long) ((interval.getNumber()+randomNumber)*1000))) {
                    KeyBinding.setKeyBindState(mc.gameSettings.keyBindAttack.getKeyCode(), true);
                    KeyBinding.onTick(mc.gameSettings.keyBindAttack.getKeyCode());
                    timer.reset();
                } else {
                    KeyBinding.setKeyBindState(mc.gameSettings.keyBindAttack.getKeyCode(), false);
                }
            } else {
                if (timer.passedMs((long) interval.getNumber())) {
                    KeyBinding.setKeyBindState(mc.gameSettings.keyBindAttack.getKeyCode(), true);
                    KeyBinding.onTick(mc.gameSettings.keyBindAttack.getKeyCode());
                    timer.reset();
                } else {
                    KeyBinding.setKeyBindState(mc.gameSettings.keyBindAttack.getKeyCode(), false);
                }
            }
        }
    }

    public double getRandomValue(double min, double max) {
        return (Math.random() * (max-min)) + min;
    }
}
