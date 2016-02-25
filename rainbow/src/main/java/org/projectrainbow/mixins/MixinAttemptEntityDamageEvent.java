package org.projectrainbow.mixins;

import PluginReference.MC_Entity;
import PluginReference.MC_EventInfo;
import PluginReference.MC_Player;
import net.minecraft.src.DamageSource;
import net.minecraft.src.EntityEnderCrystal;
import org.projectrainbow.Hooks;
import org.projectrainbow.PluginHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({EntityEnderCrystal.class})
public abstract class MixinAttemptEntityDamageEvent extends MixinEntity {

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
}
