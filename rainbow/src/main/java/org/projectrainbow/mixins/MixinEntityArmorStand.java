package org.projectrainbow.mixins;

import PluginReference.MC_ArmorStand;
import PluginReference.MC_ArmorStandActionType;
import PluginReference.MC_Entity;
import PluginReference.MC_EventInfo;
import PluginReference.MC_FloatTriplet;
import PluginReference.MC_ItemStack;
import PluginReference.MC_Player;
import com.google.common.base.Preconditions;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.Rotations;
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
    private NonNullList<ItemStack> handItems;
    @Shadow
    @Final
    private NonNullList<ItemStack> armorItems;
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
    abstract void setNoBasePlate(boolean var1);

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
        attacker = (MC_Entity) damageSource.getTrueSource();

        MC_EventInfo ei = new MC_EventInfo();

        Hooks.onAttemptEntityDamage(this, PluginHelper.wrap(damageSource), damage, ei);

        if (ei.isCancelled) {
            callbackInfo.cancel();
            callbackInfo.setReturnValue(false);
        }
    }

    @Inject(method = "swapItem", at = @At("HEAD"), cancellable = true)
    private void onArmorStandInteract(EntityPlayer var1, EntityEquipmentSlot var2, ItemStack var3, EnumHand var4, CallbackInfo callbackInfo) {
        MC_ArmorStandActionType type = MC_ArmorStandActionType.UNSPECIFIED;

        switch (var2) {
            case MAINHAND:
                type = MC_ArmorStandActionType.HELD_ITEM;
                break;
            case OFFHAND:
                type = MC_ArmorStandActionType.OFFHAND_ITEM;
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
        return PluginHelper.copyInvList(armorItems);
    }

    @Override
    public void setArmor(List<MC_ItemStack> var1) {
        PluginHelper.updateInv(armorItems, var1);
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
        setNoBasePlate(!flag);
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
        return (MC_ItemStack) (Object) handItems.get(0);
    }

    @Override
    public void setItemInHand(MC_ItemStack item) {
        handItems.set(0, PluginHelper.getItemStack(item));
    }

    @Override
    public MC_ItemStack getItemInOffHand() {
        return (MC_ItemStack) (Object) handItems.get(1);
    }

    @Override
    public void setItemInOffHand(MC_ItemStack item) {
        handItems.set(1, PluginHelper.getItemStack(item));
    }

    private static MC_FloatTriplet wrap(Rotations rotations) {
        return new MC_FloatTriplet(rotations.getX(), rotations.getY(), rotations.getZ());
    }

    private static Rotations unwrap(MC_FloatTriplet floatTriplet) {
        return new Rotations(floatTriplet.x, floatTriplet.y, floatTriplet.z);
    }
}
