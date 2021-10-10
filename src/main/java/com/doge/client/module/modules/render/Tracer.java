package com.doge.client.module.modules.render;

import com.doge.api.setting.settings.BooleanSetting;
import com.doge.api.setting.settings.ColorSetting;
import com.doge.api.setting.settings.ModeSetting;
import com.doge.api.setting.settings.NumberSetting;
import com.doge.api.util.color.DColor;
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
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.IAnimals;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL32;

import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glHint;

public class Tracer extends Module {

    // TODO: Rewrite and fix some hostile mobs not registering

    public BooleanSetting hostileMobs = new BooleanSetting("Hostile Mobs", this, true); // Still broken
    public BooleanSetting passiveMobs = new BooleanSetting("Passive Mobs", this, true);
    public BooleanSetting entities = new BooleanSetting("Entities", this, true);
    public BooleanSetting players = new BooleanSetting("Players", this, true);
    public NumberSetting renderDistance = new NumberSetting("Distance", this, 100, 10, 260, 1);
    public ModeSetting pointsTo = new ModeSetting("Draw To", this, "Feet", "Feet", "Head");
    public BooleanSetting colorType = new BooleanSetting("Color Sync", this, true);
    public ColorSetting nearColor = new ColorSetting("Near Color", this, new DColor(255, 0, 0, 255));
    public ColorSetting midColor = new ColorSetting("Middle Color", this, new DColor(255, 255, 0, 255));
    public ColorSetting farColor = new ColorSetting("Far Color", this, new DColor(0, 255, 0, 255));

    public DColor tracerColor;

    public Tracer() {
        super("Tracer", "Draws a line to entities", Category.RENDER);
        this.addSetting(hostileMobs, passiveMobs, entities, players, pointsTo, colorType, nearColor, midColor, farColor);
    }

    @Override
    public void onRender() {
        if (mc.player == null || mc.world == null) { return; }
        mc.world.loadedEntityList.stream()
                .filter(e -> e != mc.player)
                .forEach(e -> {
                    if (e instanceof IAnimals && !passiveMobs.isOn()) { return; }
                    else if (e instanceof IMob && !hostileMobs.isOn()) { return; } // Doesn't work
                    else if (e instanceof EntityPlayer && !players.isOn()) { return; }
                    else if (e instanceof EntityItem && !entities.isOn()) { return; }
                    if (mc.player.getDistance(e) > renderDistance.getNumber()) {
                        return;
                    } else {
                        // TODO: Add friend function
                        if (mc.player.getDistance(e) < 20) {
                            tracerColor = nearColor.getColor();
                        }
                        if (mc.player.getDistance(e) >= 20 && mc.player.getDistance(e) < 50) {
                            tracerColor = midColor.getColor();
                        }
                        if (mc.player.getDistance(e) >= 50) {
                            tracerColor = farColor.getColor();
                        }

                        if (colorType.isOn()) {
                            tracerColor = getDistanceColor((int) mc.player.getDistance(e));
                        }
                    }
                    drawLineToEntityPlayer(e, tracerColor);
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
        if (pointsTo.getValueName().equalsIgnoreCase("Head")) {
            drawLine(eyes.x, eyes.y, eyes.z, posx, posy + up, posz, color);
        } else {
            drawLine(eyes.x, eyes.y, eyes.z, posx, posy, posz, color);
        }
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
