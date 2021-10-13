package com.doge.client.module.modules.render;

import com.doge.api.setting.settings.BooleanSetting;
import com.doge.api.setting.settings.ColorSetting;
import com.doge.api.setting.settings.ModeSetting;
import com.doge.api.setting.settings.NumberSetting;
import com.doge.api.util.color.DColor;
import com.doge.client.friend.FriendManager;
import com.doge.client.manager.ModuleManager;
import com.doge.client.module.Category;
import com.doge.client.module.Module;
import com.doge.client.module.modules.client.ColorMain;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.EntityAmbientCreature;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.IAnimals;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL32;

import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glHint;

public class Tracer extends Module {

    public NumberSetting renderDistance = new NumberSetting("Distance", this, 100, 10, 260, 1);
    public BooleanSetting hostileMobs = new BooleanSetting("Hostile Mobs", this, true);
    public BooleanSetting passiveMobs = new BooleanSetting("Passive Mobs", this, true);
    public BooleanSetting players = new BooleanSetting("Players", this, true);
    public BooleanSetting useDistanceColor = new BooleanSetting("Color via Distance", this, false);
    public ColorSetting friendColor = new ColorSetting("Friend Color", this, new DColor(5, 217, 255));
    public ColorSetting playerColor = new ColorSetting("Player Color", this, new DColor(255, 200, 53));
    public ColorSetting hostileColor = new ColorSetting("Hostile Color", this, new DColor(255, 0, 0));
    public ColorSetting passiveColor = new ColorSetting("Passive Color", this, new DColor(95, 255, 95));

    public DColor tracerColor;

    public Tracer() {
        super("Tracer", "Draws a line to entities", Category.RENDER);
        this.addSetting(hostileMobs, passiveMobs, players, useDistanceColor, friendColor, playerColor, hostileColor, passiveColor, renderDistance);
    }

    @Override
    public void onRender() {
        if (mc.player == null || mc.world == null) { return; }
        mc.world.loadedEntityList.stream()
                .filter(e -> e != mc.player)
                .filter(e -> e instanceof EntityLivingBase)
                .forEach(e -> {
                    if (mc.player.getDistance(e) > renderDistance.getNumber()) {
                        return;
                    } else {
                        if (useDistanceColor.isOn()) {
                            tracerColor = getDistanceColor((int) mc.player.getDistance(e));
                        }
                        // TODO: Add friend function
                        if (e instanceof EntityPlayer && players.isOn()) {
                            if (FriendManager.isFriend(e.getName())) {
                                if (useDistanceColor.isOn()) {
                                    drawLineToEntityPlayer(e, tracerColor);
                                } else {
                                    drawLineToEntityPlayer(e, friendColor.getColor());
                                }
                            } else {
                                if (useDistanceColor.isOn()) {
                                    drawLineToEntityPlayer(e, tracerColor);
                                } else {
                                    drawLineToEntityPlayer(e, playerColor.getColor());
                                }
                            }
                        }
                        if (e instanceof EntityAnimal && passiveMobs.isOn()) {
                            if (useDistanceColor.isOn()) {
                                drawLineToEntityPlayer(e, tracerColor);
                            } else {
                                drawLineToEntityPlayer(e, passiveColor.getColor());
                            }
                        }
                        if (e instanceof EntityMob && hostileMobs.isOn()) {
                            if (useDistanceColor.isOn()) {
                                drawLineToEntityPlayer(e, tracerColor);
                            } else {
                                drawLineToEntityPlayer(e, hostileColor.getColor());
                            }
                        }
                    }
                });
    }

    public void drawLineToEntityPlayer(Entity e, DColor color) {
        double[] xyz = interpolate(e);
        drawLine1(xyz[0], xyz[1], xyz[2], e.height, color);
    }

    public static double[] interpolate(Entity entity) {
        double posX = interpolate(entity.posX, entity.lastTickPosX);
        double posY = interpolate(entity.posY, entity.lastTickPosY);
        double posZ = interpolate(entity.posZ, entity.lastTickPosZ);
        return new double[]{posX, posY, posZ};
    }

    public static double interpolate(double now, double then) {
        return then + (now - then) * mc.getRenderPartialTicks();
    }

    public void drawLine1(double posx, double posy, double posz, double up, DColor color) {
        Vec3d eyes = ActiveRenderInfo.getCameraPosition().add(mc.getRenderManager().viewerPosX, mc.getRenderManager().viewerPosY, mc.getRenderManager().viewerPosZ);
        prepare();
        drawLine(eyes.x, eyes.y, eyes.z, posx, posy + up, posz, color);
        release();
    }

    public static void prepare() {
        glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_NICEST);
        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ZERO, GL11.GL_ONE);
        GlStateManager.shadeModel(GL11.GL_SMOOTH);
        GlStateManager.depthMask(false);
        GlStateManager.enableBlend();
        GlStateManager.disableDepth();
        GlStateManager.disableTexture2D();
        GlStateManager.disableLighting();
        GlStateManager.disableCull();
        GlStateManager.enableAlpha();
        glEnable(GL11.GL_LINE_SMOOTH);
        glEnable(GL32.GL_DEPTH_CLAMP);
    }

    public static void release() {
        GL11.glDisable(GL32.GL_DEPTH_CLAMP);
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GlStateManager.enableAlpha();
        GlStateManager.enableCull();
        GlStateManager.enableTexture2D();
        GlStateManager.enableDepth();
        GlStateManager.disableBlend();
        GlStateManager.depthMask(true);
        GlStateManager.glLineWidth(1.0f);
        GlStateManager.shadeModel(GL11.GL_FLAT);
        glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_DONT_CARE);
    }

    public static void drawLine(double posx, double posy, double posz, double posx2, double posy2, double posz2, DColor color) {
        drawLine(posx, posy, posz, posx2, posy2, posz2, color, 1);
    }

    public static void drawLine(double posx, double posy, double posz, double posx2, double posy2, double posz2, DColor color, float width) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        GlStateManager.glLineWidth(width);
        color.glColor();
        bufferbuilder.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION);
        vertex(posx, posy, posz, bufferbuilder);
        vertex(posx2, posy2, posz2, bufferbuilder);
        tessellator.draw();
    }

    private static void vertex(double x, double y, double z, BufferBuilder bufferbuilder) {
        bufferbuilder.pos(x - mc.getRenderManager().viewerPosX, y - mc.getRenderManager().viewerPosY, z - mc.getRenderManager().viewerPosZ).endVertex();
    }

    private DColor getDistanceColor(int distance) {
        if (distance > 50) {
            distance = 50;
        }

        int red = (int) (255 - (distance * 5.1));
        int green = 255 - red;

        return new DColor(red, green, 0, 255);
    }

}
