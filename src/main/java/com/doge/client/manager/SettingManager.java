package com.doge.client.manager;

import com.doge.api.setting.Setting;
import com.doge.client.module.Module;

import java.util.ArrayList;

public class SettingManager {

    public static ArrayList<Setting> settings = new ArrayList<>();

    public static ArrayList<Setting> getSettingsInModule(Module mod) {
        ArrayList<Setting> sets = new ArrayList<>();
        for (Setting s : settings) {
            if (s.parent == mod) {
                sets.add(s);
            }
        }
        return sets;
    }

}
