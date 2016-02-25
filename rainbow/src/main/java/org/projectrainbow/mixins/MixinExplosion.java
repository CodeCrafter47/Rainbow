package org.projectrainbow.mixins;

import PluginReference.MC_Entity;
import PluginReference.MC_EventInfo;
import PluginReference.MC_Location;
import PluginReference.MC_World;
import com.google.common.collect.Lists;
import net.minecraft.src.BlockPos;
import net.minecraft.src.Entity;
import net.minecraft.src.Explosion;
import net.minecraft.src.World;
import org.projectrainbow.Hooks;
import org.projectrainbow.LocationTransformer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(Explosion.class)
public class MixinExplosion {
    @Shadow
    @Final
    private World worldObj;
    @Shadow
    @Final
    private double explosionX;
    @Shadow
    @Final
    private double explosionY;
    @Shadow
    @Final
    private double explosionZ;
    @Shadow
    @Final
    private Entity exploder;
    @Shadow
    @Final
    private float explosionSize;
    @Shadow
    @Final
    private List<BlockPos> affectedBlockPositions;

    @Inject(method = "doExplosionA", at = @At("HEAD"), cancellable = true)
    private void onExplosion(CallbackInfo callbackInfo) {
        try {
            MC_EventInfo ei = new MC_EventInfo();
            Hooks.onAttemptExplosion(new MC_Location(explosionX, explosionY, explosionZ, ((MC_World)worldObj).getDimension()), ei);
            if (ei.isCancelled) {
                callbackInfo.cancel();
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    @Inject(method = "doExplosionB", at = @At("HEAD"), cancellable = true)
    private void onExplosionB(boolean b, CallbackInfo callbackInfo) {
        try {
            MC_EventInfo ei = new MC_EventInfo();
            Hooks.onAttemptExplodeSpecific((MC_Entity) exploder, Lists.transform(affectedBlockPositions, new LocationTransformer(worldObj)));
            if (ei.isCancelled) {
                callbackInfo.cancel();
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

}
