package org.projectrainbow.mixins;

import PluginReference.MC_Entity;
import PluginReference.MC_EventInfo;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.World;
import net.minecraft.src.on;
import org.projectrainbow.Hooks;
import org.projectrainbow._DiwUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(World.class)
public abstract class MixinWorld {

    @Shadow
    protected abstract boolean isChunkLoaded(int var1, int var2, boolean var3);

    @Inject(method = "spawnEntityInWorld", at = @At("HEAD"), cancellable = true)
    private void onEntitySpawned(Entity entity, CallbackInfoReturnable<Boolean> callbackInfo) {

        int var2 = on.c(entity.posX / 16.0D); // MathHelper.floor_double
        int var3 = on.c(entity.posZ / 16.0D);
        boolean var4 = entity.forceSpawn;
        if(entity instanceof EntityPlayer) {
            var4 = true;
        }

        if (var4 || this.isChunkLoaded(var2, var3, false)) {
            MC_EventInfo ei = new MC_EventInfo();
            Hooks.onAttemptEntitySpawn((MC_Entity) entity, ei);
            if (ei.isCancelled) {
                callbackInfo.setReturnValue(false);
            }
        }
    }

    @Inject(method = "updateWeather", at = @At("HEAD"), cancellable = true)
    private void updateWeather(CallbackInfo callbackInfo) {
        if (!_DiwUtils.DoWeather) {
            callbackInfo.cancel();
        }
    }
}
