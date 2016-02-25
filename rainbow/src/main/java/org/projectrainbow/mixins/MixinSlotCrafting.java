package org.projectrainbow.mixins;

import PluginReference.MC_ItemStack;
import PluginReference.MC_Player;
import com.google.common.base.Objects;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ItemStack;
import net.minecraft.src.SlotCrafting;
import org.projectrainbow.EmptyItemStack;
import org.projectrainbow.Hooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SlotCrafting.class)
public class MixinSlotCrafting {

    @Inject(method = "onPickupFromSlot", at = @At("HEAD"))
    private void onItemCrafted(EntityPlayer var1, ItemStack var2, CallbackInfo callbackInfo) {
        Hooks.onItemCrafted((MC_Player) var1, Objects.firstNonNull((MC_ItemStack) (Object) var2, EmptyItemStack.getInstance()));
    }
}
