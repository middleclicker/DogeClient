package com.doge.api.config;

import com.doge.api.Global;
import com.doge.api.setting.Setting;
import com.doge.api.setting.settings.*;
import com.doge.client.Main;
import com.doge.client.friend.Friend;
import com.doge.client.friend.FriendManager;
import com.doge.client.manager.ModuleManager;
import com.doge.client.manager.SettingManager;
import com.doge.client.module.Module;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.lwjgl.input.Keyboard;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class SaveLoadConfig implements Global {

    private File dir;
    private File friendFile;

    public SaveLoadConfig() {
        dir = new File(mc.gameDir, "Doge");
        friendFile = new File(dir, "friends.txt");
        if (!friendFile.exists()) {
            try {
                friendFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (!dir.exists()) {
            dir.mkdir();
        }
    }

    @SuppressWarnings("unchecked")
    public void save() {
        JSONArray moduleDataset = new JSONArray();
        JSONObject modules = new JSONObject();
        for (Module m : ModuleManager.getModules()) {
            JSONObject settings = new JSONObject();
            for (Setting s : SettingManager.getSettingsInModule(m)) {
                if (s instanceof BooleanSetting) {
                    settings.put(s.name, ((BooleanSetting) s).isOn());
                }
                if (s instanceof ColorSetting) {
                    settings.put(s.name, ((ColorSetting) s).toInteger());
                }
                if (s instanceof ModeSetting) {
                    settings.put(s.name, ((ModeSetting) s).getValueName());
                }
                if (s instanceof NumberSetting) {
                    settings.put(s.name, ((NumberSetting) s).getNumber());
                }
            }
            settings.put("Keybind", Keyboard.getKeyName(m.key));
            settings.put("Enabled", m.isOn());
            modules.put(m.getName(), settings);
        }

        moduleDataset.add(modules);

        try (FileWriter file = new FileWriter("Doge/config.json")) {
            file.write(moduleDataset.toJSONString());
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveFriends() {
        List<String> friends = new ArrayList<String>();
        if (FriendManager.friends.size() > 0) {
            for (int i = 0; i < FriendManager.friends.size(); i++) {
                friends.add(i, FriendManager.friends.get(i).getName());
            }
        }
        try {
            PrintWriter pw = new PrintWriter(this.friendFile);
            for(String str : friends) {
                pw.println(str);
            }
            pw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void loadFriends() {
        ArrayList<String> lines = new ArrayList<String>();
        List<Friend> friends = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(this.friendFile));
            String line = reader.readLine();
            while(line != null) {
                lines.add(line);
                line = reader.readLine();
            }
            reader.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
        for (int i = 0; i < lines.size(); i++) {
            friends.add(i, new Friend(lines.get(i)));
        }
        FriendManager.friends = friends;
    }

    public void load() {
        JSONParser jsonParser = new JSONParser();
        try (FileReader reader = new FileReader("Doge/config.json")) {
            Object obj = jsonParser.parse(reader);
            JSONArray modulesJSONArray = (JSONArray) obj;
            for (Object object : modulesJSONArray) {
                for (Module m : ModuleManager.modules) {
                    JSONObject tempObj = (JSONObject) object;
                    for (Setting s : SettingManager.getSettingsInModule(m)) {
                        if (s instanceof BooleanSetting) {
                            ((BooleanSetting) s).setEnabled((Boolean) ((JSONObject) tempObj.get(m.name)).get(s.name));
                        }
                        if (s instanceof ColorSetting) {
                            ((ColorSetting) s).fromInteger((Long) ((JSONObject) tempObj.get(m.name)).get(s.name));
                        }
                        if (s instanceof ModeSetting) {
                            ((ModeSetting) s).setMode((String) ((JSONObject) tempObj.get(m.name)).get(s.name));
                        }
                        if (s instanceof NumberSetting) {
                            ((NumberSetting) s).setNumber((Double) ((JSONObject) tempObj.get(m.name)).get(s.name));
                        }
                    }
                    m.setKey(Keyboard.getKeyIndex((String) ((JSONObject) tempObj.get(m.name)).get("Keybind")));
                    m.setToggled((Boolean) ((JSONObject) tempObj.get(m.name)).get("Enabled"));
                }
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }
}

