package org.projectrainbow.mixins;

import PluginReference.MC_Entity;
import PluginReference.MC_EventInfo;
import PluginReference.MC_ItemStack;
import PluginReference.MC_Player;
import com.google.common.base.Objects;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.item.ItemStack;
import net.minecraft.world.storage.loot.LootContext;
import org.projectrainbow.EmptyItemStack;
import org.projectrainbow.Hooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Iterator;

@Mixin(EntityFishHook.class)
public abstract class MixinEntityFishHook {
    @Shadow
    public Entity caughtEntity;
    @Shadow
    private boolean inGround;
    @Shadow
    public EntityPlayer angler;

    @Shadow
    public abstract void setDead();

    @Inject(method = "handleHookRetraction", at = @At("HEAD"), cancellable = true)
    private void hook(CallbackInfoReturnable<Integer> callbackInfo) {
        if (caughtEntity != null) {
            MC_EventInfo ei = new MC_EventInfo();
            Hooks.onAttemptFishingReel((MC_Player) angler, null, (MC_Entity) caughtEntity, false, ei);
            if (ei.isCancelled) {
                this.setDead();
                this.angler.fishEntity = null;
                callbackInfo.setReturnValue(0);
            }
        } else if (inGround) {
            MC_EventInfo ei = new MC_EventInfo();
            Hooks.onAttemptFishingReel((MC_Player) angler, null, null, true, ei);
            if (ei.isCancelled) {
                this.setDead();
                this.angler.fishEntity = null;
                callbackInfo.setReturnValue(0);
            }
        }
    }

    @Inject(method = "handleHookRetraction", at = @At(value = "NEW", args = "class=net.minecraft.entity.item.EntityItem", shift = At.Shift.AFTER), locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true)
    private void hook(CallbackInfoReturnable<Integer> callbackInfo, int var1, LootContext.Builder var2, Iterator var3, ItemStack var4) {
        MC_EventInfo ei = new MC_EventInfo();
        Hooks.onAttemptFishingReel((MC_Player) angler, Objects.firstNonNull((MC_ItemStack) (Object) var4, EmptyItemStack.getInstance()), null, false, ei);
        if (ei.isCancelled) {
            this.setDead();
            this.angler.fishEntity = null;
            callbackInfo.setReturnValue(0);
        }
    }
}
