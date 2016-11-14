package org.projectrainbow.mixins;

import PluginReference.MC_Skeleton;
import PluginReference.MC_SkeletonType;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntitySkeletonGeneric;
import net.minecraft.entity.monster.EntitySkeletonStray;
import net.minecraft.entity.monster.EntitySkeletonWither;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(EntitySkeleton.class)
public abstract class MixinEntitySkeleton implements MC_Skeleton {

    @Override
    public MC_SkeletonType getSkeletonType() {
        EntitySkeleton handle = (EntitySkeleton) (Object) this;
        if (handle instanceof EntitySkeletonWither)
            return MC_SkeletonType.WITHER_SKELETON;
        if (handle instanceof EntitySkeletonStray)
            return MC_SkeletonType.STRAY;
        if (handle instanceof EntitySkeletonGeneric)
            return MC_SkeletonType.SKELETON;
        return MC_SkeletonType.UNSPECIFIED;
    }

    @Override
    public void setSkeletonType(MC_SkeletonType argType) {
        throw new UnsupportedOperationException("setSkeletonType no longer works");
    }
}
