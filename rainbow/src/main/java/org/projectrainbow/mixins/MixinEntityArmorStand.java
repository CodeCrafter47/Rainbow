package org.projectrainbow.mixins;

import PluginReference.MC_ArmorStand;
import PluginReference.MC_ArmorStandActionType;
import PluginReference.MC_Entity;
import PluginReference.MC_EventInfo;
import PluginReference.MC_FloatTriplet;
import PluginReference.MC_ItemStack;
import PluginReference.MC_Player;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import net.minecraft.src.DamageSource;
import net.minecraft.src.EntityArmorStand;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EnumHand;
import net.minecraft.src.ItemSlot;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Rotations;
import org.projectrainbow.EmptyItemStack;
import org.projectrainbow.Hooks;
import org.projectrainbow.PluginHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

@Mixin(EntityArmorStand.class)
public abstract class MixinEntityArmorStand extends MixinEntityLivingBase implements MC_ArmorStand {

    @Shadow
    @Final
    private ItemStack[] bx;
    @Shadow
    @Final
    private ItemStack[] contents;
    @Shadow
    private Rotations headRotation;
    @Shadow
    private Rotations bodyRotation;
    @Shadow
    private Rotations leftArmRotation;
    @Shadow
    private Rotations rightArmRotation;
    @Shadow
    private Rotations leftLegRotation;
    @Shadow
    private Rotations rightLegRotation;

    @Shadow
    abstract void p(boolean var1);

    @Shadow
    public abstract boolean hasNoBasePlate();

    @Shadow
    abstract void setShowArms(boolean var1);

    @Shadow
    public abstract boolean getShowArms();

    @Shadow
    public abstract void setHeadRotation(Rotations var1);

    @Shadow
    public abstract void setBodyRotation(Rotations var1);

    @Shadow
    public abstract void setLeftArmRotation(Rotations var1);

    @Shadow
    public abstract void setRightArmRotation(Rotations var1);

    @Shadow
    public abstract void setLeftLegRotation(Rotations var1);

    @Shadow
    public abstract void setRightLegRotation(Rotations var1);



    @Inject(method = "attackEntityFrom", at = @At("HEAD"), cancellable = true)
    private void onAttacked(DamageSource damageSource, float damage, CallbackInfoReturnable<Boolean> callbackInfo) {
        m_rainbowAdjustedDamage = damage;
        damageModified = false;
        MC_Entity entity = (MC_Entity) damageSource.getEntity();
        MC_Player player = entity instanceof MC_Player ? (MC_Player) entity : null;
        if (player != null) {
            MC_EventInfo ei = new MC_EventInfo();

            Hooks.onAttemptEntityDamage(this, PluginHelper.wrap(damageSource), damage, ei);

            if (ei.isCancelled) {
                callbackInfo.cancel();
                callbackInfo.setReturnValue(false);
            }
        }
    }

    @Inject(method = "a(Lnet/minecraft/src/EntityPlayer;Lnet/minecraft/src/ItemSlot;Lnet/minecraft/src/ItemStack;Lnet/minecraft/src/EnumHand;)V", at = @At("HEAD"), cancellable = true)
    private void onArmorStandInteract(EntityPlayer var1, ItemSlot var2, ItemStack var3, EnumHand var4, CallbackInfo callbackInfo) {
        MC_ArmorStandActionType type = MC_ArmorStandActionType.UNSPECIFIED;

        switch (var2) {
            case MAINHAND:
                type = MC_ArmorStandActionType.HELD_ITEM;
                break;
            case OFFHAND:
                // todo add offhand to api
                break;
            case FEET:
                type = MC_ArmorStandActionType.FEET;
                break;
            case LEGS:
                type = MC_ArmorStandActionType.LEGS;
                break;
            case CHEST:
                type = MC_ArmorStandActionType.BODY;
                break;
            case HEAD:
                type = MC_ArmorStandActionType.HEAD;
                break;
        }

        MC_EventInfo ei = new MC_EventInfo();
        Hooks.onAttemptArmorStandInteract((MC_Player) var1, this, type, ei);

        if (ei.isCancelled) {
            callbackInfo.cancel();
        }
    }

    @Override
    public List<MC_ItemStack> getArmor() {
        return PluginHelper.invArrayToList(bx);
    }

    @Override
    public void setArmor(List<MC_ItemStack> var1) {
        PluginHelper.updateInv(bx, var1);
    }

    @Override
    public boolean hasArms() {
        return getShowArms();
    }

    @Override
    public boolean hasBase() {
        return !hasNoBasePlate();
    }

    @Override
    public void setHasArms(boolean flag) {
        setShowArms(flag);
    }

    @Override
    public void setHasBase(boolean flag) {
        p(!flag);
    }

    @Override
    public List<MC_FloatTriplet> getPose() {
        ArrayList<MC_FloatTriplet> pose = new ArrayList<MC_FloatTriplet>();
        pose.add(wrap(this.headRotation));
        pose.add(wrap(this.bodyRotation));
        pose.add(wrap(this.leftArmRotation));
        pose.add(wrap(this.rightArmRotation));
        pose.add(wrap(this.leftLegRotation));
        pose.add(wrap(this.rightLegRotation));
        return pose;
    }

    @Override
    public void setPose(List<MC_FloatTriplet> pose) {
        Preconditions.checkNotNull(pose, "pose");
        Preconditions.checkArgument(pose.size() == 6, "size != 6");
        setHeadRotation(unwrap(pose.get(0)));
        setBodyRotation(unwrap(pose.get(1)));
        setLeftArmRotation(unwrap(pose.get(2)));
        setRightArmRotation(unwrap(pose.get(3)));
        setLeftLegRotation(unwrap(pose.get(4)));
        setRightLegRotation(unwrap(pose.get(5)));
    }

    @Override
    public MC_ItemStack getItemInHand() {
        return Objects.firstNonNull((MC_ItemStack) (Object) contents[0], EmptyItemStack.getInstance());
    }

    @Override
    public void setItemInHand(MC_ItemStack item) {
        contents[0] = item instanceof EmptyItemStack ? null : (ItemStack) (Object) item;
    }

    private MC_FloatTriplet wrap(Rotations rotations) {
        return new MC_FloatTriplet(rotations.getX(), rotations.getY(), rotations.getZ());
    }

    private Rotations unwrap(MC_FloatTriplet floatTriplet) {
        return new Rotations(floatTriplet.x, floatTriplet.y, floatTriplet.z);
    }
}
