package org.projectrainbow.mixins;

import PluginReference.MC_Entity;
import PluginReference.MC_EventInfo;
import PluginReference.MC_Location;
import PluginReference.MC_Projectile;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import org.projectrainbow.Hooks;
import org.projectrainbow.PluginHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(EntityThrowable.class)
public abstract class MixinEntityThrowable extends MixinEntity implements MC_Projectile {

    @Shadow
    public abstract EntityLivingBase getThrower();

    @Shadow
    protected abstract void onImpact(RayTraceResult var1);

    @Override
    public MC_Entity getProjectileSource() {
        return (MC_Entity) getThrower();
    }

    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "net.minecraft.entity.projectile.EntityThrowable.onImpact(Lnet/minecraft/util/math/RayTraceResult;)V"))
    private void onHit(EntityThrowable self, RayTraceResult hit) {
        MC_EventInfo ei = new MC_EventInfo();
        Vec3d hitVec = hit.hitVec;
        if (hit.type == RayTraceResult.Type.ENTITY) {
            Hooks.onAttemptProjectileHitEntity(this, (MC_Entity) hit.entity, new MC_Location(hitVec.x, hitVec.y, hitVec.z, PluginHelper.getLegacyDimensionId(dimension)), ei);
        } else if (hit.type == RayTraceResult.Type.BLOCK) {
            BlockPos pos = hit.getBlockPos();
            Hooks.onAttemptProjectileHitBlock(this, new MC_Location(pos.getX(), pos.getY(), pos.getZ(), PluginHelper.getLegacyDimensionId(dimension)), PluginHelper.directionMap.get(hit.sideHit), new MC_Location(hitVec.x, hitVec.y, hitVec.z, PluginHelper.getLegacyDimensionId(dimension)), ei);
        }
        if (!ei.isCancelled) {
            onImpact(hit);
        }
    }
}
