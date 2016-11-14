package org.projectrainbow.mixins;

import PluginReference.MC_ItemStack;
import PluginReference.MC_Player;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.SlotCrafting;
import net.minecraft.item.ItemStack;
import org.projectrainbow.Hooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SlotCrafting.class)
public class MixinSlotCrafting {

    @Inject(method = "onPickupFromSlot", at = @At("HEAD"))
    private void onItemCrafted(EntityPlayer var1, ItemStack var2, CallbackInfoReturnable<ItemStack> callbackInfo) {
        Hooks.onItemCrafted((MC_Player) var1, (MC_ItemStack) (Object) var2);
    }

    @Inject(method = "onPickupFromSlot", at = @At("RETURN"))
    private void onItemCrafted2(EntityPlayer var1, ItemStack var2, CallbackInfoReturnable<ItemStack> callbackInfo) {
        ((MC_Player) var1).updateInventory();
    }
}
