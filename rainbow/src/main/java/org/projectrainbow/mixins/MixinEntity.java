package org.projectrainbow.mixins;

import PluginReference.*;
import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.dimension.DimensionType;
import org.projectrainbow.Hooks;
import org.projectrainbow.PluginHelper;
import org.projectrainbow.ServerWrapper;
import org.projectrainbow._DiwUtils;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Mixin(Entity.class)
@Implements(@Interface(iface = MC_Entity.class, prefix = "api$"))
public abstract class MixinEntity implements MC_Entity {
    protected float m_rainbowAdjustedDamage = 0;
    protected boolean damageModified = false;

    @Shadow
    public float rotationYaw;
    @Shadow
    public float rotationPitch;
    @Shadow
    public World world;
    @Shadow
    public double posX;
    @Shadow
    public double posY;
    @Shadow
    public double posZ;
    @Shadow
    public double motionX;
    @Shadow
    public double motionY;
    @Shadow
    public double motionZ;
    @Shadow
    public boolean onGround;
    @Shadow
    public float fallDistance;
    @Shadow
    protected boolean inWater;
    @Shadow
    private boolean invulnerable;
    @Shadow
    private int fire;

    protected MC_Entity attacker = null;

    @Shadow
    public abstract List<Entity> getPassengers();

    @Shadow
    public abstract void setWorld(World w);

    @Shadow
    public abstract void setPositionAndRotation(double x, double y, double z, float yaw, float pitch);

    @Shadow
    public abstract AxisAlignedBB getBoundingBox();

    @Shadow
    public abstract void setSneaking(boolean sneaking);

    @Shadow
    public abstract UUID getUniqueID();

    @Shadow
    public abstract void onKillCommand();

    @Shadow
    public abstract void setCustomName(ITextComponent name);

    @Shadow
    public abstract ITextComponent shadow$getCustomName();

    @Shadow
    public abstract void remove();

    @Shadow
    public abstract boolean isInvulnerableTo(DamageSource var1);

    @Shadow
    public abstract boolean shadow$isSneaking();

    @Shadow
    public abstract boolean shadow$isInvisible();

    @Shadow
    public abstract boolean shadow$startRiding(Entity var1);

    @Shadow
    public abstract boolean isSprinting();

    @Shadow
    public abstract void setInvisible(boolean var1);

    @Shadow
    public abstract boolean hasCustomName();

    @Shadow
    public abstract int getEntityId();

    @Shadow
    public abstract Entity getRidingEntity();

    @Shadow
    public abstract void setPosition(double var1, double var3, double var5);

    @Shadow
    public abstract void removePassengers();

    @Shadow
    @Final
    protected abstract String getEntityString();

    @Shadow public abstract ITextComponent getDisplayName();

    @Shadow public DimensionType dimension;

    @Shadow public abstract boolean isAlive();

    @Shadow public boolean removed;

    protected void setInvulnerable(boolean value) {
        invulnerable = value;
    }

    private float waterFallDistance = 0;

    private int getLegacyDimensionId() {
        return PluginHelper.getLegacyDimensionId(((Entity) (Object) this).dimension);
    }

    @Inject(method = "handleWaterMovement", at = @At(value = "FIELD", target = "net.minecraft.entity.Entity.fallDistance:F"))
    private void onWaterEntered(CallbackInfoReturnable<Boolean> callbackInfo) {
        waterFallDistance = fallDistance;
    }

    @Inject(method = "updateFallState", at = @At("HEAD"))
    protected void a(double var1, boolean onGround, IBlockState var4, BlockPos var5, CallbackInfo callbackInfo) {
        if (onGround && fallDistance > 0) {
            Hooks.onFallComplete(this, this.fallDistance, new MC_Location(var5.getX(), var5.getY(), var5.getZ(), getLegacyDimensionId()), inWater);
        } else if (inWater && waterFallDistance > 0) {
            Hooks.onFallComplete(this, this.waterFallDistance, new MC_Location(var5.getX(), var5.getY(), var5.getZ(), getLegacyDimensionId()), inWater);
            waterFallDistance = 0;
        }
    }

    @Redirect(method = "applyEntityCollision", at = @At(value = "INVOKE", target = "net.minecraft.entity.Entity.addVelocity(DDD)V"))
    void onEntityPushed(Entity pushedEntity, double xVelocity, double yVelocity, double zVelocity, Entity other) {
        MC_Entity entity = pushedEntity == other ? this : (MC_Entity) other;
        MC_EventInfo ei = new MC_EventInfo();
        MC_FloatTriplet velocity = new MC_FloatTriplet((float) xVelocity, (float) yVelocity, (float) zVelocity);
        Hooks.onEntityPushed(entity, (MC_Entity) pushedEntity, velocity, ei);
        if (!ei.isCancelled) {
            pushedEntity.addVelocity(velocity.x, velocity.y, velocity.z);
        }
    }

    @ModifyConstant(method = "Lnet/minecraft/entity/Entity;func_212321_a(Lnet/minecraft/world/dimension/DimensionType;)Lnet/minecraft/entity/Entity;", constant = @Constant(doubleValue = 8.0D))
    private double injectNetherDistanceRatio(double ignored) {
        return _DiwUtils.netherDistanceRatio;
    }

    @Override
    public MC_Location getLocation() {
        return new MC_Location(posX, posY, posZ, getLegacyDimensionId(), rotationYaw, rotationPitch);
    }

    @Override
    public MC_World getWorld() {
        return (MC_World) world;
    }

    @Override
    public MC_EntityType getType() {
        MC_EntityType type = PluginHelper.getEntityType((Class<? extends Entity>) (Object) getClass());
        return MoreObjects.firstNonNull(type, MC_EntityType.UNSPECIFIED);
    }

    @Override
    public boolean isDead() {
        return !isAlive();
    }

    @Override
    public void kill() {
        onKillCommand();
    }

    @Override
    public MC_Entity getVehicle() {
        return (MC_Entity) getRidingEntity();
    }

    @Override
    public MC_Entity getRider() {
        return getPassengers().isEmpty() ? null : (MC_Entity) getPassengers().get(0);
    }

    @Override
    public void setRider(MC_Entity var1) {
        if (var1 == null) {
            MC_Entity rider = getRider();
            if (rider != null) {
                ((Entity) rider).stopRiding();
            }
            return;
        }
        ((Entity) var1).startRiding((Entity) (Object) this);
    }

    @Override
    public void setVehicle(MC_Entity var1) {
        if (var1 == null) {
            ((Entity) (Object) this).stopRiding();
            return;
        }
        shadow$startRiding((Entity) var1);
    }

    @Override
    public List<MC_ItemStack> getArmor() {
        throw new UnsupportedOperationException("This entity has no armor.");
    }

    @Override
    public void setArmor(List<MC_ItemStack> var1) {
        throw new UnsupportedOperationException("This entity has no armor.");
    }

    @Override
    public String internalInfo() {
        return getEntityString() + ": " + toString();
    }

    @Override
    public int getFireTicks() {
        return fire;
    }

    @Override
    public void setFireTicks(int var1) {
        fire = var1;
    }

    @Override
    public MC_MotionData getMotionData() {
        MC_MotionData motionData = new MC_MotionData();
        motionData.xMotion = motionX;
        motionData.yMotion = motionY;
        motionData.zMotion = motionZ;
        motionData.onGround = onGround;
        motionData.fallDistance = fallDistance;
        motionData.inWater = inWater;
        return motionData;
    }

    @Override
    public void setMotionData(MC_MotionData motionData) {
        Preconditions.checkNotNull(motionData, "motionData");
        motionX = motionData.xMotion;
        motionY = motionData.yMotion;
        motionZ = motionData.zMotion;
        onGround = motionData.onGround;
        fallDistance = (float) motionData.fallDistance;
        inWater = motionData.inWater;
    }

    @Override
    public void setCustomName(String var1) {
        setCustomName(new TextComponentString(var1));
    }

    @Override
    public String getCustomName() {
        return shadow$getCustomName().getString();
    }

    @Override
    public float getHealth() {
        return 1;
    }

    @Override
    public void setHealth(float var1) {
    }

    @Override
    public float getMaxHealth() {
        return 1;
    }

    @Override
    public void setMaxHealth(float var1) {
    }

    @Override
    public int getBaseArmorScore() {
        return 0;
    }

    @Override
    public float getArmorAdjustedDamage(MC_DamageType var1, float var2) {
        return 0;
    }

    @Override
    public float getTotalAdjustedDamage(MC_DamageType var1, float var2) {
        return 0;
    }

    @Override
    public float getAbsorptionAmount() {
        return 0;
    }

    @Override
    public void setAbsorptionAmount(float var1) {
    }

    @Override
    public void setNumberOfArrowsHitWith(int var1) {
    }

    @Override
    public MC_Entity getAttacker() {
        return attacker;
    }

    @Override
    public List<MC_PotionEffect> getPotionEffects() {
        return Collections.emptyList();
    }

    @Override
    public void setPotionEffects(List<MC_PotionEffect> var1) {
    }

    @Override
    public void setAdjustedIncomingDamage(float var1) {
        m_rainbowAdjustedDamage = var1;
        damageModified = true;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<MC_Entity> getNearbyEntities(float var1) {
        return (List<MC_Entity>) (List) world.getEntitiesInAABBexcluding((Entity) (Object) this, new AxisAlignedBB(posX - var1, posY - var1, posZ - var1, posX + var1, posY + var1, posZ + var1), entity -> true); // getEntitiesInAABBexcluding
    }

    @Override
    public void removeEntity() {
        remove();
    }

    @Intrinsic
    public boolean api$isSneaking() {
        return shadow$isSneaking();
    }

    @Intrinsic
    public boolean api$isInvisible() {
        return shadow$isInvisible();
    }

    @Intrinsic
    public String api$getName() {
        return getDisplayName().getString();
    }

    @Intrinsic
    public boolean api$isSprinting() {
        return isSprinting();
    }

    @Intrinsic
    public void api$setInvisible(boolean var1) {
        setInvisible(var1);
    }

    @Intrinsic
    public boolean api$hasCustomName() {
        return hasCustomName();
    }

    @Intrinsic
    public int api$getEntityId() {
        return getEntityId();
    }

    @Override
    public List<MC_Entity> getRiders() {
        return (List<MC_Entity>) (Object) getPassengers();
    }

    @Override
    public void addRider(MC_Entity ent) {
        ((Entity) ent).startRiding((Entity) (Object) this);
    }

    @Override
    public void removeRider(MC_Entity ent) {
        ((Entity)ent).stopRiding();
    }

    @Override
    public UUID getUUID() {
        return getUniqueID();
    }

    @Override
    public void teleport(MC_Location loc) {
        teleport(loc, true);
    }

    @Override
    public void teleport(MC_Location loc, boolean safe) {
        teleport((WorldServer) ServerWrapper.getInstance().getWorld(loc.dimension), loc.x, loc.y, loc.z, loc.yaw, loc.pitch, safe);
    }

    @Override
    public byte[] serialize() {
        NBTTagCompound data = new NBTTagCompound();
        ((Entity) (Object) this).writeWithoutTypeId(data);
        data.putString("id", getEntityString());
        data.remove("UUIDMost");
        data.remove("UUIDLeast");
        data.remove("Dimension");

        try {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(os);

            data.write(dos);
            dos.flush();
            dos.close();
            return os.toByteArray();
        } catch (Exception var5) {
            var5.printStackTrace();
            return new byte[0];
        }
    }

    public void teleport(WorldServer world, double x, double y, double z) {
        teleport(world, x, y, z, rotationYaw, rotationPitch);
    }

    public void teleport(WorldServer world, double x, double y, double z, float yaw, float pitch) {
        teleport(world, x, y, z, yaw, pitch, true);
    }

    public void teleport(WorldServer world, double x, double y, double z, float yaw, float pitch, boolean safe) {
        removePassengers();
        if (world != this.world) {
            final WorldServer fromWorld = (WorldServer) this.world;
            final WorldServer toWorld = world;

            fromWorld.removeEntityDangerously((EntityPlayerMP) (Object) this);
            ((Entity) (Object) this).dimension = toWorld.dimension.getType();
            setPositionAndRotation(x, y, z, yaw, pitch);

            toWorld.getChunk((int) posX >> 4, (int) posZ >> 4);

            while(safe && !toWorld.checkNoEntityCollision((Entity) (Object) this, this.getBoundingBox()) && this.posY < 255.0D) {
                this.setPosition(this.posX, this.posY + 1.0D, this.posZ);
            }

            this.setWorld(toWorld);
            toWorld.spawnEntity((Entity) (Object) this);

            fromWorld.resetUpdateEntityTick();
            toWorld.resetUpdateEntityTick();
        } else {
            setPositionAndRotation(x, y, z, yaw, pitch);
        }
    }
}
