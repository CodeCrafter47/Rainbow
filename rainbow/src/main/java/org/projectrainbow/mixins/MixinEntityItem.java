package org.projectrainbow.mixins;

import PluginReference.MC_EventInfo;
import PluginReference.MC_ItemEntity;
import PluginReference.MC_ItemStack;
import PluginReference.MC_Player;
import net.minecraft.src.EntityItem;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ItemStack;
import org.projectrainbow.Hooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityItem.class)
public abstract class MixinEntityItem implements MC_ItemEntity {

    @Shadow
    public abstract ItemStack getEntityItem();

    @Shadow
    public abstract void setEntityItemStack(ItemStack var1);

    @Shadow
    public abstract String getOwner();

    @Shadow
    public abstract void setOwner(String var1);

    @Shadow
    public abstract String getThrower();

    @Shadow
    public abstract void setThrower(String var1);

    @Inject(method = "onCollideWithPlayer", at = @At("HEAD"), cancellable = true)
    private void onPickup(EntityPlayer player, CallbackInfo callbackInfo) {
        MC_EventInfo ei = new MC_EventInfo();
        Hooks.onAttemptItemPickup((MC_Player) player, (MC_ItemStack) (Object) getEntityItem(), false, ei);
        if (ei.isCancelled) {
            callbackInfo.cancel();
        }
    }

    @Override
    public MC_ItemStack getItemStack() {
        return (MC_ItemStack) (Object) getEntityItem();
    }

    @Override
    public void setItemStack(MC_ItemStack is) {
        setEntityItemStack((ItemStack) (Object) is);
    }

    @Override
    public String getThrowerName() {
        return getThrower();
    }

    @Override
    public void setThrowerName(String name) {
        setThrower(name);
    }

    @Override
    public String getOwnerName() {
        return getOwner();
    }

    @Override
    public void setOwnerName(String name) {
        setOwner(name);
    }
}
