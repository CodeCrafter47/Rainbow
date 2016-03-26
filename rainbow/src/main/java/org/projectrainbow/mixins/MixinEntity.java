package org.projectrainbow.mixins;

import PluginReference.MC_DamageType;
import PluginReference.MC_Entity;
import PluginReference.MC_EntityType;
import PluginReference.MC_EventInfo;
import PluginReference.MC_FloatTriplet;
import PluginReference.MC_ItemStack;
import PluginReference.MC_Location;
import PluginReference.MC_MotionData;
import PluginReference.MC_PotionEffect;
import PluginReference.MC_World;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicates;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.BlockPos;
import net.minecraft.src.DamageSource;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityList;
import net.minecraft.src.IBlockState;
import net.minecraft.src.World;
import org.projectrainbow.Hooks;
import org.projectrainbow.PluginHelper;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Intrinsic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

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
    public int dimension;
    @Shadow
    public World worldObj;
    @Shadow
    public boolean isDead;
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
    public abstract AxisAlignedBB getEntityBoundingBox();

    @Shadow
    public abstract void setSneaking(boolean sneaking);

    @Shadow
    public abstract UUID getUniqueID();

    @Shadow
    public abstract void onKillCommand();

    @Shadow
    public abstract void setCustomNameTag(String name);

    @Shadow
    public abstract String getCustomNameTag();

    @Shadow
    public abstract void setDead();

    @Shadow
    public abstract boolean isEntityInvulnerable(DamageSource var1);

    @Shadow(prefix = "isSneaking$")
    public abstract boolean isSneaking$aK();

    @Shadow(prefix = "isInvisible$")
    public abstract boolean isInvisible$aN();

    @Shadow(prefix = "startRiding$")
    public abstract boolean startRiding$m(Entity var1);

    @Shadow
    public abstract String getName();

    @Shadow
    public abstract boolean isSprinting();

    @Shadow
    public abstract void setInvisible(boolean var1);

    @Shadow
    public abstract boolean hasCustomName();

    @Shadow
    public abstract int getEntityId();

    @Shadow(prefix = "getRiddenEntity$")
    public abstract Entity getRiddenEntity$getRider();

    @Shadow
    public abstract void setPosition(double var1, double var3, double var5);

    protected void setInvulnerable(boolean value) {
        invulnerable = value;
    }

    private float waterFallDistance = 0;

    @Inject(method = "handleWaterMovement", at = @At(value = "FIELD", target = "net.minecraft.src.Entity.fallDistance:F"))
    private void onWaterEntered(CallbackInfoReturnable<Boolean> callbackInfo) {
        waterFallDistance = fallDistance;
    }

    @Inject(method = "a(DZLnet/minecraft/src/IBlockState;Lnet/minecraft/src/BlockPos;)V", at = @At("HEAD"))
    protected void a(double var1, boolean onGround, IBlockState var4, BlockPos var5, CallbackInfo callbackInfo) {
        if (onGround && fallDistance > 0) {
            Hooks.onFallComplete(this, this.fallDistance, new MC_Location(var5.getX(), var5.getY(), var5.getZ(), dimension), inWater);
        } else if (inWater && waterFallDistance > 0) {
            Hooks.onFallComplete(this, this.waterFallDistance, new MC_Location(var5.getX(), var5.getY(), var5.getZ(), dimension), inWater);
            waterFallDistance = 0;
        }
    }

    @Redirect(method = "applyEntityCollision", at = @At(value = "INVOKE", target = "net.minecraft.src.Entity.addVelocity(DDD)V"))
    void onEntityPushed(Entity pushedEntity, double xVelocity, double yVelocity, double zVelocity, Entity other) {
        MC_Entity entity = pushedEntity == other ? this : (MC_Entity) other;
        MC_EventInfo ei = new MC_EventInfo();
        MC_FloatTriplet velocity = new MC_FloatTriplet((float) xVelocity, (float) yVelocity, (float) zVelocity);
        Hooks.onEntityPushed(entity, (MC_Entity) pushedEntity, velocity, ei);
        if (!ei.isCancelled) {
            pushedEntity.addVelocity(velocity.x, velocity.y, velocity.z);
        }
    }

    @Override
    public MC_Location getLocation() {
        return new MC_Location(posX, posY, posZ, dimension, rotationYaw, rotationPitch);
    }

    @Override
    public MC_World getWorld() {
        return (MC_World) worldObj;
    }

    @Override
    public MC_EntityType getType() {
        MC_EntityType type = PluginHelper.getEntityType((Class<? extends Entity>) (Object) getClass());
        return Objects.firstNonNull(type, MC_EntityType.UNSPECIFIED);
    }

    @Override
    public boolean isDead() {
        return isDead;
    }

    @Override
    public void kill() {
        onKillCommand();
    }

    @Override
    public MC_Entity getVehicle() {
        return (MC_Entity) getRiddenEntity$getRider();
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
        ((Entity) var1).m((Entity) (Object) this);
    }

    @Override
    public void setVehicle(MC_Entity var1) {
        if (var1 == null) {
            ((Entity) (Object) this).stopRiding();
            return;
        }
        startRiding$m((Entity) var1);
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
        return EntityList.getEntityString((Entity) (Object) this) + ": " + toString();
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
        setCustomNameTag(var1);
    }

    @Override
    public String getCustomName() {
        return getCustomNameTag();
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
    public List getNearbyEntities(float var1) {
        return worldObj.getEntitiesInAABBexcluding((Entity) (Object) this, new AxisAlignedBB(posX - var1, posY - var1, posZ - var1, posX + var1, posY + var1, posZ + var1), Predicates.<Entity>alwaysTrue());
    }

    @Override
    public void removeEntity() {
        setDead();
    }

    @Override
    public boolean isSneaking() {
        return isSneaking$aK();
    }

    @Override
    public boolean isInvisible() {
        return isInvisible$aN();
    }

    @Intrinsic
    public String api$getName() {
        return getName();
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
        ((Entity) ent).m((Entity) (Object) this);
    }

    @Override
    public void removeRider(MC_Entity ent) {
        ((Entity)ent).stopRiding();
    }
}
