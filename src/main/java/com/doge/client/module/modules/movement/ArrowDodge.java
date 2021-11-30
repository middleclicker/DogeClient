package com.doge.client.module.modules.movement;

import com.doge.api.setting.settings.ModeSetting;
import com.doge.client.module.Category;
import com.doge.client.module.Module;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ArrowDodge extends Module {

    public ModeSetting mode = new ModeSetting("Mode", this, "Legit", "Legit"); // TODO: Add packet mode

    public ArrowDodge() {
        super("Arrow Dodge", "Automatically dodges arrows.", Category.MOVEMENT);
        this.addSetting(mode);
    }

    private EntityPlayer target;
    private ArrayList<EntityPlayer> targets = new ArrayList<>();
    private final Queue<Vec3d> flightPoint = new ConcurrentLinkedQueue<>();
    boolean hit = false;
    private Vec3d facing = null;
    private BlockPos pos = null;
    private int temp = 0;
    private boolean moving = false;

    @Override
    public void onEnable() {
        target = null;
        targets.clear();
        flightPoint.clear();
        hit = false;
        facing = null;
        pos = null;
        moving = false;
        temp = 0;
    }

    @Override
    public void onDisable() {
        target = null;
        targets.clear();
        flightPoint.clear();
        hit = false;
        facing = null;
        pos = null;
        moving = false;
        temp = 0;
    }

    @Override
    public void onUpdate() {
        targets.clear();
        target = null;
        facing = null;
        if (mc.player == null || mc.world == null) return;
        for (Entity entity : mc.world.loadedEntityList) {
            if (entity instanceof EntityPlayer) {
                EntityPlayer player = (EntityPlayer) entity;
                if (player.getActiveItemStack().getItem() == Items.BOW) { // && player != mc.player
                    targets.add(player);
                }
            }
            if (entity instanceof EntitySkeleton) {
                EntitySkeleton skeleton = (EntitySkeleton) entity;
                // Until I find a way to know if skeletons are attacking, this feature is a rip
            }
        }
        if (targets.isEmpty()) return;
        target = targets.stream().min(Comparator.comparing(c -> mc.player.getDistance(c))).get();
        ThrowableType throwingType = this.getTypeFromCurrentItem(target);
        if (throwingType == ThrowableType.NONE) return;

        FlightPath flightPath = new FlightPath(target, throwingType);

        while (!flightPath.isCollided()) {
            flightPath.onUpdate();

            flightPoint.offer(new Vec3d(flightPath.position.x - mc.getRenderManager().viewerPosX,
                    flightPath.position.y - mc.getRenderManager().viewerPosY,
                    flightPath.position.z - mc.getRenderManager().viewerPosZ));
        }

        if (flightPath.collided) {
            final RayTraceResult hit = flightPath.target;
            AxisAlignedBB bb = null;

            if (hit.typeOfHit == RayTraceResult.Type.ENTITY && hit.entityHit instanceof EntityPlayer) {
                if (hit.entityHit == mc.player) {
                    this.hit = true;
                    pos = mc.player.getPosition();
                }
            }
        }

        System.out.println(hit);

        if (!moving) {
            this.temp = (Math.random() <= 0.5) ? 1 : 2;
        }

        if (this.hit) {
            if (mode.is("Legit")) {
                if (mc.player.getDistance(pos.x, pos.y, pos.z) <= 1) {
                    moving = true;
                    if (temp == 1) KeyBinding.setKeyBindState(mc.gameSettings.keyBindLeft.getKeyCode(), true);
                    if (temp == 2) KeyBinding.setKeyBindState(mc.gameSettings.keyBindRight.getKeyCode(), true);
                } else {
                    moving = false;
                    KeyBinding.setKeyBindState(mc.gameSettings.keyBindLeft.getKeyCode(), false);
                    KeyBinding.setKeyBindState(mc.gameSettings.keyBindRight.getKeyCode(), false);
                    this.hit = false;
                }
            } else if (mode.is("Packet")) { // Unfinished, do not use
                if (temp == 1) {
                    float leftAngle = mc.player.cameraYaw - 90; //or 90 if somehow in degrees
                    mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX + Math.cos(leftAngle), mc.player.posY, mc.player.posZ + Math.sin(leftAngle), true));
                    mc.player.setPosition(mc.player.posX + Math.cos(leftAngle), mc.player.posY, mc.player.posZ + Math.sin(leftAngle));
                }
                if (temp == 2) {
                    /*
                    mc.player.connection.sendPacket(new CPacketPlayer.Position(Math.floor(mc.player.posX)-1, mc.player.posY, Math.floor(mc.player.posZ), true));
                    mc.player.setPosition(Math.floor(mc.player.posX)-1, mc.player.posY, Math.floor(mc.player.posZ));

                     */
                }
            }
        }

        // System.out.println(target);
    }

    private ThrowableType getTypeFromCurrentItem(EntityPlayer player) {
        // Check if we're holding an item first
        if (player.getHeldItemMainhand().isEmpty()) {
            return ThrowableType.NONE;
        }

        final ItemStack itemStack = player.getHeldItem(EnumHand.MAIN_HAND);
        // Check what type of item this is
        switch (Item.getIdFromItem(itemStack.getItem())) {
            case 261: // ItemBow
                if (player.isHandActive())
                    return ThrowableType.ARROW;
                break;
            case 346: // ItemFishingRod
                return ThrowableType.FISHING_ROD;
            case 438: //splash potion
            case 441: //splash potion linger
                return ThrowableType.POTION;
            case 384: // ItemExpBottle
                return ThrowableType.EXPERIENCE;
            case 332: // ItemSnowball
            case 344: // ItemEgg
            case 368: // ItemEnderPearl
                return ThrowableType.NORMAL;
            default:
                break;
        }

        return ThrowableType.NONE;
    }

    enum ThrowableType {
        /**
         * Represents a non-throwable object.
         */
        NONE(0.0f, 0.0f),

        /**
         * Arrows fired from a bow.
         */
        ARROW(1.5f, 0.05f),

        /**
         * Splash potion entities
         */
        POTION(0.5f, 0.05f),

        /**
         * Experience bottles.
         */
        EXPERIENCE(0.7F, 0.07f),

        /**
         * The fishhook entity with a fishing rod.
         */
        FISHING_ROD(1.5f, 0.04f),

        /**
         * Any throwable entity that doesn't have unique
         * world velocity/gravity constants.
         */
        NORMAL(1.5f, 0.03f);

        private final float velocity;
        private final float gravity;

        ThrowableType(float velocity, float gravity) {
            this.velocity = velocity;
            this.gravity = gravity;
        }

        /**
         * The initial velocity of the entity.
         *
         * @return entity velocity
         */

        public float getVelocity() {
            return velocity;
        }

        /**
         * The constant gravity applied to the entity.
         *
         * @return constant world gravity
         */
        public float getGravity() {
            return gravity;
        }
    }

    /**
     * A class used to mimic the flight of an entity.  Actual
     * implementation resides in multiple classes but the parent of all
     * of them is {@link net.minecraft.entity.projectile.EntityThrowable}
     */
    private final class FlightPath {
        private final EntityPlayer shooter;
        private Vec3d position;
        private Vec3d motion;
        private float yaw;
        private float pitch;
        private AxisAlignedBB boundingBox;
        private boolean collided;
        private RayTraceResult target;
        private final ThrowableType throwableType;

        FlightPath(EntityPlayer player, ThrowableType throwableType) {
            this.shooter = player;
            this.throwableType = throwableType;

            // Set the starting angles of the entity
            this.setLocationAndAngles(this.shooter.posX, this.shooter.posY + this.shooter.getEyeHeight(), this.shooter.posZ,
                    this.shooter.rotationYaw, this.shooter.rotationPitch);

            Vec3d startingOffset = new Vec3d(MathHelper.cos(this.yaw / 180.0F * (float) Math.PI) * 0.16F, 0.1d,
                    MathHelper.sin(this.yaw / 180.0F * (float) Math.PI) * 0.16F);

            this.position = this.position.subtract(startingOffset);
            // Update the entity's bounding box
            this.setPosition(this.position);

            // Set the entity's motion based on the shooter's rotations
            this.motion = new Vec3d(-MathHelper.sin(this.yaw / 180.0F * (float) Math.PI) * MathHelper.cos(this.pitch / 180.0F * (float) Math.PI),
                    -MathHelper.sin(this.pitch / 180.0F * (float) Math.PI),
                    MathHelper.cos(this.yaw / 180.0F * (float) Math.PI) * MathHelper.cos(this.pitch / 180.0F * (float) Math.PI));

            this.setThrowableHeading(this.motion, this.getInitialVelocity());
        }

        /**
         * Update the entity's data in the world.
         */
        public void onUpdate() {
            // Get the predicted positions in the world
            Vec3d prediction = this.position.add(this.motion);
            // Check if we've collided with a block in the same time
            RayTraceResult blockCollision = this.shooter.getEntityWorld().rayTraceBlocks(this.position, prediction,
                    this.throwableType == ThrowableType.FISHING_ROD, !this.collidesWithNoBoundingBox(), false);
            // Check if we got a block collision
            if (blockCollision != null) {
                prediction = blockCollision.hitVec;
            }

            // Check entity collision
            this.onCollideWithEntity(prediction, blockCollision);

            // Check if we had a collision
            if (this.target != null) {
                this.collided = true;
                // Update position
                this.setPosition(this.target.hitVec);
                return;
            }

            // Sanity check to see if we've gone below the world (if we have we will never collide)
            if (this.position.y <= 0.0d) {
                // Force this to true even though we haven't collided with anything
                this.collided = true;
                return;
            }

            // Update the entity's position based on velocity
            this.position = this.position.add(this.motion);
            float motionModifier = 0.99F;
            // Check if our path will collide with water
            if (this.shooter.getEntityWorld().isMaterialInBB(this.boundingBox, Material.WATER)) {
                // Arrows move slower in water than normal throwables
                motionModifier = this.throwableType == ThrowableType.ARROW ? 0.6F : 0.8F;
            }

            // Apply the fishing rod specific motion modifier
            if (this.throwableType == ThrowableType.FISHING_ROD) {
                motionModifier = 0.92f;
            }

            // Slowly decay the velocity of the path
            this.motion = mult(this.motion, motionModifier);
            // Drop the motionY by the constant gravity
            this.motion = this.motion.subtract(0.0d, this.getGravityVelocity(), 0.0d);
            // Update the position and bounding box
            this.setPosition(this.position);
        }

        /**
         * Checks if a specific item type will collide
         * with a block that has no collision bounding box.
         *
         * @return true if type collides
         */
        private boolean collidesWithNoBoundingBox() {
            switch (this.throwableType) {
                case FISHING_ROD:
                case NORMAL:
                    return true;
                default:
                    return false;
            }
        }

        /**
         * Check if our path collides with an entity.
         *
         * @param prediction     the predicted position
         * @param blockCollision block collision if we had one
         */
        private void onCollideWithEntity(Vec3d prediction, RayTraceResult blockCollision) {
            Entity collidingEntity = null;
            RayTraceResult collidingPosition = null;

            double currentDistance = 0.0d;
            // Get all possible collision entities disregarding the local player
            List<Entity> collisionEntities = Minecraft.getMinecraft().world.getEntitiesWithinAABBExcludingEntity(this.shooter, this.boundingBox.expand(this.motion.x, this.motion.y, this.motion.z).grow(1.0D, 1.0D, 1.0D));

            // Loop through every loaded entity in the world
            for (Entity entity : collisionEntities) {
                // Check if we can collide with the entity or it's ourself
                if (!entity.canBeCollidedWith()) {
                    continue;
                }

                // Check if we collide with our bounding box
                float collisionSize = entity.getCollisionBorderSize();
                AxisAlignedBB expandedBox = entity.getEntityBoundingBox().expand(collisionSize, collisionSize, collisionSize);
                RayTraceResult objectPosition = expandedBox.calculateIntercept(this.position, prediction);

                // Check if we have a collision
                if (objectPosition != null) {
                    double distanceTo = this.position.distanceTo(objectPosition.hitVec);

                    // Check if we've gotten a closer entity
                    if (distanceTo < currentDistance || currentDistance == 0.0D) {
                        collidingEntity = entity;
                        collidingPosition = objectPosition;
                        currentDistance = distanceTo;
                    }
                }
            }

            // Check if we had an entity
            if (collidingEntity != null) {
                // Set our target to the result
                this.target = new RayTraceResult(collidingEntity, collidingPosition.hitVec);
            } else {
                // Fallback to the block collision
                this.target = blockCollision;
            }
        }

        /**
         * Return the initial velocity of the entity at it's exact starting
         * moment in flight.
         *
         * @return entity velocity in flight
         */
        private float getInitialVelocity() {
            switch (this.throwableType) {
                // Arrows use the current use duration as a velocity multplier
                case ARROW:
                    // Check how long we've been using the bow
                    int useDuration = this.shooter.getHeldItem(EnumHand.MAIN_HAND).getItem().getMaxItemUseDuration(this.shooter.getHeldItem(EnumHand.MAIN_HAND)) - this.shooter.getItemInUseCount();
                    float velocity = (float) useDuration / 20.0F;
                    velocity = (velocity * velocity + velocity * 2.0F) / 3.0F;
                    if (velocity > 1.0F) {
                        velocity = 1.0F;
                    }

                    // When the arrow is spawned inside of ItemBow, they multiply it by 2
                    return (velocity * 2.0f) * throwableType.getVelocity();
                default:
                    return throwableType.getVelocity();
            }
        }

        /**
         * Get the constant gravity of the item in use.
         *
         * @return gravity relating to item
         */
        private float getGravityVelocity() {
            return throwableType.getGravity();
        }

        /**
         * Set the position and rotation of the entity in the world.
         *
         * @param x     x position in world
         * @param y     y position in world
         * @param z     z position in world
         * @param yaw   yaw rotation axis
         * @param pitch pitch rotation axis
         */
        private void setLocationAndAngles(double x, double y, double z, float yaw, float pitch) {
            this.position = new Vec3d(x, y, z);
            this.yaw = yaw;
            this.pitch = pitch;
        }

        /**
         * Sets the x,y,z of the entity from the given parameters. Also seems to set
         * up a bounding box.
         *
         * @param position position in world
         */
        private void setPosition(Vec3d position) {
            this.position = new Vec3d(position.x, position.y, position.z);
            // Usually this is this.width / 2.0f but throwables change
            double entitySize = (this.throwableType == ThrowableType.ARROW ? 0.5d : 0.25d) / 2.0d;
            // Update the path's current bounding box
            this.boundingBox = new AxisAlignedBB(position.x - entitySize,
                    position.y - entitySize,
                    position.z - entitySize,
                    position.x + entitySize,
                    position.y + entitySize,
                    position.z + entitySize);
        }

        /**
         * Set the entity's velocity and position in the world.
         *
         * @param motion   velocity in world
         * @param velocity starting velocity
         */
        private void setThrowableHeading(Vec3d motion, float velocity) {
            // Divide the current motion by the length of the vector
            this.motion = div(motion, (float) motion.length());
            // Multiply by the velocity
            this.motion = mult(this.motion, velocity);
        }

        /**
         * Check if the path has collided with an object.
         *
         * @return path collides with ground
         */
        public boolean isCollided() {
            return collided;
        }

        /**
         * Get the target we've collided with if it exists.
         *
         * @return moving object target
         */
        public RayTraceResult getCollidingTarget() {
            return target;
        }
    }

    public static Vec3d mult(Vec3d factor, float multiplier) {
        return new Vec3d(factor.x * multiplier, factor.y * multiplier, factor.z * multiplier);
    }

    public static Vec3d div(Vec3d factor, float divisor) {
        return new Vec3d(factor.x / divisor, factor.y / divisor, factor.z / divisor);
    }
}