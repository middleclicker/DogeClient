package com.doge.client.module.modules.render;

import com.doge.api.event.events.Render3DEvent;
import com.doge.api.setting.settings.BooleanSetting;
import com.doge.api.setting.settings.ColorSetting;
import com.doge.api.setting.settings.ModeSetting;
import com.doge.api.setting.settings.NumberSetting;
import com.doge.api.util.GeometryUtil;
import com.doge.api.util.HoleUtil;
import com.doge.api.util.color.DColor;
import com.doge.api.util.render.RenderUtil;
import com.doge.client.module.Category;
import com.doge.client.module.Module;
import com.google.common.collect.Sets;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @reworked by 0b00101010 on 14/01/2021
 * @author Gamesense
 */

// TODO: Rewrite

public class HoleESP extends Module {

    public NumberSetting range = new NumberSetting("Range", this, 10, 1, 20, 1);
    ModeSetting customHoles = new ModeSetting("Show", this, "Single", "Single", "Double");
    ModeSetting type = new ModeSetting("Render", this,"Both", "Outline", "Fill", "Both");
    ModeSetting mode = new ModeSetting("Mode", this,"Air", "Air", "Ground", "Flat", "Slab", "Double");
    BooleanSetting hideOwn = new BooleanSetting("Hide Own", this, false);
    BooleanSetting flatOwn = new BooleanSetting("Flat Own", this, false);
    BooleanSetting ignoreBottomBlock = new BooleanSetting("Ignore Bottom Block", this, false);
    NumberSetting slabHeight = new NumberSetting("Slab Height", this, 0.5, 0.1, 1.5, 0.1);
    NumberSetting width = new NumberSetting("Width", this, 1, 1, 10, 1);
    ColorSetting bedrockColor = new ColorSetting("Bedrock Color", this, new DColor(25, 141, 250, 1));
    ColorSetting obsidianColor = new ColorSetting("Obsidian Color", this, new DColor(204, 18, 74, 1));
    ColorSetting customColor = new ColorSetting("Custom Color", this, new DColor(0, 0, 255));
    NumberSetting ufoAlpha = new NumberSetting("UFOAlpha", this, 255, 0, 255, 1);

    public HoleESP() {
        super("Hole ESP", "Highlights safe holes.", Category.RENDER);
        this.addSetting(range, customHoles, type, mode, hideOwn, flatOwn, slabHeight, width, bedrockColor, obsidianColor, customColor, ufoAlpha);
    }

    private ConcurrentHashMap<AxisAlignedBB, DColor> holes;

    public void onUpdate() {
        if (mc.player == null || mc.world == null) {
            return;
        }

        if (holes == null) {
            holes = new ConcurrentHashMap<>();
        } else {
            holes.clear();
        }

        int range = (int) Math.ceil(this.range.getValue());

        HashSet<BlockPos> possibleHoles = Sets.newHashSet();
        List<BlockPos> blockPosList = getSphere(getPlayerPos(), range, range, false, true, 0);

        for (BlockPos pos : blockPosList) {

            if (!mc.world.getBlockState(pos).getBlock().equals(Blocks.AIR)) {
                continue;
            }

            if (mc.world.getBlockState(pos.add(0, -1, 0)).getBlock().equals(Blocks.AIR)) {
                continue;
            }
            if (!mc.world.getBlockState(pos.add(0, 1, 0)).getBlock().equals(Blocks.AIR)) {
                continue;
            }

            if (mc.world.getBlockState(pos.add(0, 2, 0)).getBlock().equals(Blocks.AIR)) {
                possibleHoles.add(pos);
            }
        }

        possibleHoles.forEach(pos -> {
            HoleUtil.HoleInfo holeInfo = HoleUtil.isHole(pos, false, ignoreBottomBlock.isOn());
            HoleUtil.HoleType holeType = holeInfo.getType();
            if (holeType != HoleUtil.HoleType.NONE) {

                HoleUtil.BlockSafety holeSafety = holeInfo.getSafety();
                AxisAlignedBB centreBlocks = holeInfo.getCentre();

                if (centreBlocks == null)
                    return;

                DColor colour;

                if (holeSafety == HoleUtil.BlockSafety.UNBREAKABLE) {
                    colour = new DColor(bedrockColor.getValue(), 255);
                } else {
                    colour = new DColor(obsidianColor.getValue(), 255);
                }
                if (holeType == HoleUtil.HoleType.CUSTOM) {
                    colour = new DColor(customColor.getValue(), 255);
                }

                String mode = customHoles.getValueName();
                if (mode.equalsIgnoreCase("Double") && holeType == HoleUtil.HoleType.DOUBLE) {
                    holes.put(centreBlocks, colour);
                } else if (holeType == HoleUtil.HoleType.SINGLE) {
                    holes.put(centreBlocks, colour);
                }
            }
        });
    }

    @Override
    public void onRender3D(Render3DEvent event) {
        if (mc.player == null || mc.world == null || holes == null || holes.isEmpty()) {
            return;
        }

        holes.forEach(this::renderHoles);
    }

    private void renderHoles(AxisAlignedBB hole, DColor color) {
        switch (type.getValueName()) {
            case "Outline": {
                renderOutline(hole, color);
                break;
            }
            case "Fill": {
                renderFill(hole, color);
                break;
            }
            case "Both": {
                renderOutline(hole, color);
                renderFill(hole, color);
                break;
            }
        }
    }

    private void renderFill(AxisAlignedBB hole, DColor color) {
        DColor fillColor = new DColor(color, 50);
        int ufoAlpha = (int) ((this.ufoAlpha.getValue() * 50) / 255);

        if (hideOwn.isOn() && hole.intersects(mc.player.getEntityBoundingBox())) return;

        switch (mode.getValueName()) {
            case "Air": {
                if (flatOwn.isOn() && hole.intersects(mc.player.getEntityBoundingBox())) {
                    RenderUtil.drawBox(hole, true, 1, fillColor, ufoAlpha, GeometryUtil.Quad.DOWN);
                } else {
                    RenderUtil.drawBox(hole, true, 1, fillColor, ufoAlpha, GeometryUtil.Quad.ALL);
                }
                break;
            }
            case "Ground": {
                RenderUtil.drawBox(hole.offset(0, -1, 0), true, 1, new DColor(fillColor, ufoAlpha), fillColor.getAlpha(), GeometryUtil.Quad.ALL);
                break;
            }
            case "Flat": {
                RenderUtil.drawBox(hole, true, 1, fillColor, ufoAlpha, GeometryUtil.Quad.DOWN);
                break;
            }
            case "Slab": {
                if (flatOwn.isOn() && hole.intersects(mc.player.getEntityBoundingBox())) {
                    RenderUtil.drawBox(hole, true, 1, fillColor, ufoAlpha, GeometryUtil.Quad.DOWN);
                } else {
                    RenderUtil.drawBox(hole, false, slabHeight.getValue(), fillColor, ufoAlpha, GeometryUtil.Quad.ALL);
                }
                break;
            }
            case "Double": {
                if (flatOwn.isOn() && hole.intersects(mc.player.getEntityBoundingBox())) {
                    RenderUtil.drawBox(hole, true, 1, fillColor, ufoAlpha, GeometryUtil.Quad.DOWN);
                } else {
                    RenderUtil.drawBox(hole.setMaxY(hole.maxY + 1), true, 2, fillColor, ufoAlpha, GeometryUtil.Quad.ALL);
                }
                break;
            }
        }
    }

    private void renderOutline(AxisAlignedBB hole, DColor color) {
        DColor outlineColor = new DColor(color, 255);

        if (hideOwn.isOn() && hole.intersects(mc.player.getEntityBoundingBox())) return;

        switch (mode.getValueName()) {
            case "Air": {
                if (flatOwn.isOn() && hole.intersects(mc.player.getEntityBoundingBox())) {
                    RenderUtil.drawBoundingBoxWithSides(hole, (int) width.getValue(), outlineColor, (int) ufoAlpha.getValue(), GeometryUtil.Quad.DOWN);
                } else {
                    RenderUtil.drawBoundingBox(hole, width.getValue(), outlineColor, (int) ufoAlpha.getValue());
                }
                break;
            }
            case "Ground": {
                RenderUtil.drawBoundingBox(hole.offset(0, -1, 0), width.getValue(), new DColor(outlineColor, (int) ufoAlpha.getValue()), outlineColor.getAlpha());
                break;
            }
            case "Flat": {
                RenderUtil.drawBoundingBoxWithSides(hole, (int) width.getValue(), outlineColor, (int) ufoAlpha.getValue(), GeometryUtil.Quad.DOWN);
                break;
            }
            case "Slab": {
                if (this.flatOwn.isOn() && hole.intersects(mc.player.getEntityBoundingBox())) {
                    RenderUtil.drawBoundingBoxWithSides(hole, (int) width.getValue(), outlineColor, (int) ufoAlpha.getValue(), GeometryUtil.Quad.DOWN);
                } else {
                    RenderUtil.drawBoundingBox(hole.setMaxY(hole.minY + slabHeight.getValue()), width.getValue(), outlineColor, (int) ufoAlpha.getValue());
                }
                break;
            }
            case "Double": {
                if (this.flatOwn.isOn() && hole.intersects(mc.player.getEntityBoundingBox())) {
                    RenderUtil.drawBoundingBoxWithSides(hole, (int) width.getValue(), outlineColor, (int) ufoAlpha.getValue(), GeometryUtil.Quad.DOWN);
                } else {
                    RenderUtil.drawBoundingBox(hole.setMaxY(hole.maxY + 1), width.getValue(), outlineColor, (int) ufoAlpha.getValue());
                }
                break;
            }
        }
    }

    public static List<BlockPos> getSphere(BlockPos loc, float r, int h, boolean hollow, boolean sphere, int plus_y) {
        List<BlockPos> circleBlocks = new ArrayList<>();
        int cx = loc.getX();
        int cy = loc.getY();
        int cz = loc.getZ();
        for (int x = cx - (int) r; x <= cx + r; x++) {
            for (int z = cz - (int) r; z <= cz + r; z++) {
                for (int y = (sphere ? cy - (int) r : cy); y < (sphere ? cy + r : cy + h); y++) {
                    double dist = (cx - x) * (cx - x) + (cz - z) * (cz - z) + (sphere ? (cy - y) * (cy - y) : 0);
                    if (dist < r * r && !(hollow && dist < (r - 1) * (r - 1))) {
                        BlockPos l = new BlockPos(x, y + plus_y, z);
                        circleBlocks.add(l);
                    }
                }
            }
        }
        return circleBlocks;
    }

    public static BlockPos getPlayerPos() {
        return new BlockPos(Math.floor(mc.player.posX), Math.floor(mc.player.posY), Math.floor(mc.player.posZ));
    }

}