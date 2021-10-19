package com.doge.client.module.modules.misc;

import com.doge.api.util.ClientMessage;
import com.doge.client.Main;
import com.doge.client.friend.FriendManager;
import com.doge.client.module.Category;
import com.doge.client.module.Module;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Mouse;

public class MiddleClickFriend extends Module {
    public MiddleClickFriend() {
        super("MCF", "Middle click a player to add them as friend.", Category.MISC);
    }

    @EventHandler
    private final Listener<InputEvent.MouseInputEvent> mouseListener = new Listener<>(event -> {
        if (Mouse.isButtonDown(2)) {
            if (mc.currentScreen == null) {
                if (mc.objectMouseOver != null && mc.objectMouseOver.typeOfHit == RayTraceResult.Type.ENTITY && mc.objectMouseOver.entityHit instanceof EntityPlayer) {
                    try {
                        String friendName = mc.objectMouseOver.entityHit.getName();
                        if (FriendManager.isFriend(friendName)) {
                            FriendManager.removeFriend(friendName);
                            ClientMessage.sendMessage("Removed Friend " + friendName);
                        } else {
                            FriendManager.addFriend(friendName);
                            ClientMessage.sendMessage("Added Friend " + friendName);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    });

}
