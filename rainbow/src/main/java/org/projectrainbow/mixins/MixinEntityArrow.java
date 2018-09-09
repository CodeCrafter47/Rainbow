package org.projectrainbow.mixins;

import PluginReference.MC_Arrow;
import PluginReference.MC_Entity;
import PluginReference.MC_EventInfo;
import PluginReference.MC_Location;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import org.projectrainbow.Hooks;
import org.projectrainbow.PluginHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityArrow.class)
public abstract class MixinEntityArrow extends MixinEntity implements MC_Arrow {

    @Override
    public MC_Entity getProjectileSource() {
        return (MC_Entity) ((EntityArrow) (Object) this).k();
    }

    @Inject(method = "onHit", at = @At("HEAD"), cancellable = true)
    private void onHit(RayTraceResult hit, CallbackInfo ci) {
        MC_EventInfo ei = new MC_EventInfo();
        Vec3d hitVec = hit.hitVec;
        if (hit.typeOfHit == RayTraceResult.Type.ENTITY) {
            Hooks.onAttemptProjectileHitEntity(this, (MC_Entity) hit.entityHit, new MC_Location(hitVec.x, hitVec.y, hitVec.z, PluginHelper.getLegacyDimensionId(ap)), ei);
        } else if (hit.typeOfHit == RayTraceResult.Type.BLOCK) {
            BlockPos pos = hit.getBlockPos();
            Hooks.onAttemptProjectileHitBlock(this, new MC_Location(pos.getX(), pos.getY(), pos.getZ(), PluginHelper.getLegacyDimensionId(ap)), PluginHelper.directionMap.get(hit.sideHit), new MC_Location(hitVec.x, hitVec.y, hitVec.z, PluginHelper.getLegacyDimensionId(ap)), ei);
        }
        if (ei.isCancelled) {
            ci.cancel();
        }
    }
}
