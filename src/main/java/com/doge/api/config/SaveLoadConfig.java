package com.doge.api.config;

import com.doge.api.Global;
import com.doge.api.Reference;
import com.doge.api.setting.Setting;
import com.doge.api.setting.settings.BooleanSetting;
import com.doge.api.setting.settings.ModeSetting;
import com.doge.api.setting.settings.NumberSetting;
import com.doge.client.Main;
import com.doge.client.manager.ModuleManager;
import com.doge.client.module.Module;
import net.minecraft.client.Minecraft;

import java.io.*;
import java.util.ArrayList;

/**
 * Copied from postman
 */

public class SaveLoadConfig implements Global {

    // TODO: Rewrite

    private File dir;
    private File dataFile;

    public SaveLoadConfig() {
        dir = new File(Minecraft.getMinecraft().gameDir, Reference.NAME);
        if (!dir.exists()) {
            dir.mkdir();
        }
        dataFile = new File(dir, "config.txt");
        if (!dataFile.exists()) {
            try {
                dataFile.createNewFile();
            } catch (IOException e) {e.printStackTrace();}
        }

        this.load();
    }

    public void save() {
        ArrayList<String> toSave = new ArrayList<String>();
        for (Module m : ModuleManager.modules) {
            toSave.add("MOD:" + m.name + ":" + m.isOn() + ":" + m.getKey());
        }

        for (Module m : ModuleManager.modules) {
            for (Setting setting : m.settings) {
                if (setting instanceof BooleanSetting) {
                    BooleanSetting bool = (BooleanSetting) setting;
                    if (m.name.equals("Click GUI")) {
                        toSave.add("SET:" + m.name + ":" + setting.name + ":" + false);
                    } else {
                        toSave.add("SET:" + m.name + ":" + setting.name + ":" + bool.isOn());
                    }
                }
                if(setting instanceof NumberSetting) {
                    NumberSetting numb = (NumberSetting) setting;
                    toSave.add("SET:" + m.name + ":" + setting.name + ":" + numb.getNumber());
                }
                if(setting instanceof ModeSetting) {
                    ModeSetting mode = (ModeSetting) setting;
                    toSave.add("SET:" + m.name + ":" + setting.name + ":" + mode.getValueName());
                }
            }
        }

        try {
            PrintWriter pw = new PrintWriter(this.dataFile);
            for(String str : toSave) {
                pw.println(str);
            }
            pw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void load() {
        ArrayList<String> lines = new ArrayList<String>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(this.dataFile));
            String line = reader.readLine();
            while(line != null) {
                lines.add(line);
                line = reader.readLine();
            }
            reader.close();
        } catch(Exception e) {
            e.printStackTrace();
        }

        for(String s : lines) {
            String[] args = s.split(":");
            if (s.toLowerCase().startsWith("mod:")) {
                Module m = ModuleManager.getModuleByName(args[1]);
                if (m != null) {
                    if (m.name.equals("Click GUI")) {
                        m.setToggled(!Boolean.parseBoolean(args[2]));
                    }
                    if (!m.name.equals("Click GUI")) {
                        m.setToggled(Boolean.parseBoolean(args[2]));
                    }
                    m.setKey(Integer.parseInt(args[3]));
                }
            } else if (s.toLowerCase().startsWith("set:")) {
                Module m = ModuleManager.getModuleByName(args[1]);
                if (m != null) {
                    Setting setting = Main.SETTING_MANAGER.getSettingByName(m, args[2]);
                    if (setting != null) {
                        if (setting instanceof BooleanSetting) {
                            ((BooleanSetting) setting).setEnabled(Boolean.parseBoolean(args[3]));
                        }
                        if (setting instanceof NumberSetting) {
                            ((NumberSetting)setting).setNumber(Double.parseDouble(args[3]));
                        }
                        if(setting instanceof ModeSetting) {
                            ((ModeSetting)setting).setMode(args[3]);
                        }
                    }
                }
            }
        }
    }
}

