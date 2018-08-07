package org.projectrainbow.mixins;

import PluginReference.MC_Entity;
import PluginReference.MC_EventInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IWorldReaderBase;
import net.minecraft.world.World;
import org.projectrainbow.Hooks;
import org.projectrainbow._DiwUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(World.class)
public abstract class MixinWorld implements IWorldReaderBase {

    @Inject(method = "spawnEntity", at = @At("HEAD"), cancellable = true)
    private void onEntitySpawned(Entity entity, CallbackInfoReturnable<Boolean> callbackInfo) {

        int var2 = MathHelper.floor(entity.posX / 16.0D);
        int var3 = MathHelper.floor(entity.posZ / 16.0D);
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
