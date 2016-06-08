package org.projectrainbow.mixins;

import PluginReference.MC_EventInfo;
import PluginReference.MC_Player;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import org.projectrainbow.Hooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityXPOrb.class)
public class MixinEntityXPOrb {

    @Inject(method = "onCollideWithPlayer", at = @At("HEAD"), cancellable = true)
    private void onPickup(EntityPlayer player, CallbackInfo callbackInfo) {
        MC_EventInfo ei = new MC_EventInfo();
        Hooks.onAttemptItemPickup((MC_Player) player, null, true, ei);
        if (ei.isCancelled) {
            callbackInfo.cancel();
        }
    }
}
