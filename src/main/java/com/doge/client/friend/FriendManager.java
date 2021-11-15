package com.doge.client.friend;

import com.doge.client.Main;

import java.util.ArrayList;
import java.util.List;

public class FriendManager {
    public static List<Friend> friends = new ArrayList<>(); // You have none why bother

    public FriendManager() {
        // friends.add(new Friend("Middleclicker"));
    }

    public static Friend getFriendByName(String name) {
        Friend friend = null;
        for (Friend f : friends) {
            if (f.getName().equalsIgnoreCase(name)) {
                friend = f;
            }
        }
        return friend;
    }

    public static boolean isFriend(String name) {
        for (Friend f : friends) {
            if (f.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    public static void addFriend(String name) {
        friends.add(new Friend(name));

        if (Main.SAVELOAD_CONFIG != null) {
            Main.SAVELOAD_CONFIG.saveFriends();
        }
    }

    public static void removeFriend(String name) {
        friends.remove(getFriendByName(name));

        if (Main.SAVELOAD_CONFIG != null) {
            Main.SAVELOAD_CONFIG.saveFriends();
        }
    }

    public static void clearFriends() {
        friends.clear();
    }
}
