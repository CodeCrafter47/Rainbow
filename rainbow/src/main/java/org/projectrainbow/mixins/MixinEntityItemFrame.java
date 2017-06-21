package org.projectrainbow.mixins;

import PluginReference.MC_Entity;
import PluginReference.MC_EventInfo;
import PluginReference.MC_ItemFrameActionType;
import PluginReference.MC_Player;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import org.projectrainbow.Hooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityItemFrame.class)
public abstract class MixinEntityItemFrame extends MixinEntityHanging{

    @Shadow
    public abstract ItemStack getDisplayedItem();

    @Inject(method = "attackEntityFrom", at = @At("HEAD"), cancellable = true)
    private void onAttacked(DamageSource damageSource, float damage, CallbackInfoReturnable<Boolean> callbackInfo) {
        m_rainbowAdjustedDamage = damage;
        MC_Entity entity = (MC_Entity) damageSource.getTrueSource();
        MC_Player player = entity instanceof MC_Player ? (MC_Player) entity : null;

        MC_EventInfo ei = new MC_EventInfo();

        Hooks.onAttemptItemFrameInteract(player, getLocation(), MC_ItemFrameActionType.BREAK_INNER_ITEM, ei);

        if (ei.isCancelled) {
            callbackInfo.cancel();
            callbackInfo.setReturnValue(false);
        }
    }

    @Inject(method = "processInitialInteract", at = @At("HEAD"), cancellable = true)
    private void onInteract(EntityPlayer var1, EnumHand var3, CallbackInfoReturnable<Boolean> callbackInfo) {
        MC_ItemFrameActionType type;
        if (getDisplayedItem() == null && !var1.getHeldItem(var3).isEmpty()) {
            type = MC_ItemFrameActionType.PLACE_INNER_ITEM;
        } else {
            type = MC_ItemFrameActionType.ROTATE_INNER_ITEM;
        }

        MC_EventInfo ei = new MC_EventInfo();

        Hooks.onAttemptItemFrameInteract((MC_Player) var1, getLocation(), type, ei);

        if (ei.isCancelled) {
            callbackInfo.cancel();
            callbackInfo.setReturnValue(false);
        }
    }
}
