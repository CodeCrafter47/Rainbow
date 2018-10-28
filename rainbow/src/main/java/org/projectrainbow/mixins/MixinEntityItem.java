package org.projectrainbow.mixins;

import PluginReference.MC_EventInfo;
import PluginReference.MC_ItemEntity;
import PluginReference.MC_ItemStack;
import PluginReference.MC_Player;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.projectrainbow.Hooks;
import org.projectrainbow.PluginHelper;
import org.projectrainbow._DiwUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

@Mixin(EntityItem.class)
public abstract class MixinEntityItem extends MixinEntity implements MC_ItemEntity {

    @Shadow
    public abstract ItemStack getItem();

    @Shadow
    public abstract void setItem(ItemStack var1);

    @Shadow
    private UUID thrower;
    @Shadow
    private UUID owner;

    @Inject(method = "onCollideWithPlayer", at = @At(value = "INVOKE", target = "net.minecraft.entity.player.InventoryPlayer.addItemStackToInventory(Lnet/minecraft/item/ItemStack;)Z"), cancellable = true)
    private void onPickup(EntityPlayer player, CallbackInfo callbackInfo) {
        MC_EventInfo ei = new MC_EventInfo();
        Hooks.onAttemptItemPickup((MC_Player) player, getItemStack(), false, ei);
        if (ei.isCancelled) {
            callbackInfo.cancel();
        }
    }

    @Override
    public MC_ItemStack getItemStack() {
        return (MC_ItemStack) (Object) getItem();
    }

    @Override
    public void setItemStack(MC_ItemStack is) {
        setItem(PluginHelper.getItemStack(is));
    }

    @Override
    public String getThrowerName() {
        EntityPlayer entity = world.getPlayerEntityByUUID(thrower);
        return entity == null ? thrower.toString() : entity.getDisplayName().getFormattedText();
    }

    @Override
    public void setThrowerName(String name) {
        EntityPlayerMP player = _DiwUtils.getMinecraftServer().getPlayerList().getPlayerByUsername(name);
        if (player != null) {
            thrower = player.getUniqueID();
        }
    }

    @Override
    public String getOwnerName() {
        EntityPlayer entity = world.getPlayerEntityByUUID(owner);
        return entity == null ? owner.toString() : entity.getDisplayName().getFormattedText();
    }

    @Override
    public void setOwnerName(String name) {
        EntityPlayerMP player = _DiwUtils.getMinecraftServer().getPlayerList().getPlayerByUsername(name);
        if (player != null) {
            owner = player.getUniqueID();
        }
    }
}
