package org.projectrainbow.mixins;

import PluginReference.MC_Entity;
import PluginReference.MC_EventInfo;
import PluginReference.MC_Location;
import PluginReference.MC_World;
import com.google.common.collect.Lists;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
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
    private World world;
    @Shadow
    @Final
    private double x;
    @Shadow
    @Final
    private double y;
    @Shadow
    @Final
    private double z;
    @Shadow
    @Final
    private Entity exploder;
    @Shadow
    @Final
    private List<BlockPos> affectedBlockPositions;

    @Inject(method = "doExplosionA", at = @At("HEAD"), cancellable = true)
    private void onExplosion(CallbackInfo callbackInfo) {
        try {
            MC_EventInfo ei = new MC_EventInfo();
            Hooks.onAttemptExplosion(new MC_Location(x, y, z, ((MC_World) world).getDimension()), ei);
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
            Hooks.onAttemptExplodeSpecific((MC_Entity) exploder, Lists.transform(affectedBlockPositions, new LocationTransformer(world)));
            if (ei.isCancelled) {
                callbackInfo.cancel();
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

}
