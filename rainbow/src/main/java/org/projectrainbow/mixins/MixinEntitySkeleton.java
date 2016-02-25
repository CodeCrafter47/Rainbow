package org.projectrainbow.mixins;

import PluginReference.MC_Skeleton;
import PluginReference.MC_SkeletonType;
import net.minecraft.src.EntitySkeleton;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(EntitySkeleton.class)
@Implements(@Interface(iface = MC_Skeleton.class, prefix = "api$"))
public abstract class MixinEntitySkeleton {

    @Shadow
    public abstract int getSkeletonType();

    @Shadow
    public abstract void setSkeletonType(int var1);

    public MC_SkeletonType api$getSkeletonType() {
        int t = getSkeletonType();

        return t == 1
                ? MC_SkeletonType.WITHER_SKELETON
                : (t == 0
                ? MC_SkeletonType.SKELETON
                : MC_SkeletonType.UNSPECIFIED);
    }

    public void setSkeletonType(MC_SkeletonType argType) {
        if (argType == MC_SkeletonType.WITHER_SKELETON) {
            setSkeletonType(1);
        } else {
            setSkeletonType(0);
        }
    }
}
