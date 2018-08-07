package org.projectrainbow.mixins;

import PluginReference.MC_Skeleton;
import PluginReference.MC_SkeletonType;
import net.minecraft.entity.monster.AbstractSkeleton;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntityStray;
import net.minecraft.entity.monster.EntityWitherSkeleton;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(AbstractSkeleton.class)
public abstract class MixinEntitySkeleton implements MC_Skeleton {

    @Override
    @Deprecated
    public MC_SkeletonType getSkeletonType() {
        AbstractSkeleton handle = (EntitySkeleton) (Object) this;
        if (handle instanceof EntityWitherSkeleton)
            return MC_SkeletonType.WITHER_SKELETON;
        if (handle instanceof EntityStray)
            return MC_SkeletonType.STRAY;
        if (handle instanceof EntitySkeleton)
            return MC_SkeletonType.SKELETON;
        return MC_SkeletonType.UNSPECIFIED;
    }

    @Override
    public void setSkeletonType(MC_SkeletonType argType) {
        throw new UnsupportedOperationException("setSkeletonType no longer works");
    }
}
