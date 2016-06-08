package org.projectrainbow.mixins;

import PluginReference.MC_Skeleton;
import PluginReference.MC_SkeletonType;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.src.ze;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(EntitySkeleton.class)
@Implements(@Interface(iface = MC_Skeleton.class, prefix = "api$"))
public abstract class MixinEntitySkeleton {

    @Shadow
    public abstract ze df();

    @Shadow
    public abstract void a(ze var1);

    public MC_SkeletonType api$getSkeletonType() {
        switch (df()) {
            case WITHER:
                return MC_SkeletonType.WITHER_SKELETON;
            case STRAY:
                return MC_SkeletonType.STRAY;
            case NORMAL:
            default:
                return MC_SkeletonType.SKELETON;
        }
    }

    public void setSkeletonType(MC_SkeletonType argType) {
        switch (argType) {
            case UNSPECIFIED:
            case SKELETON:
                a(ze.NORMAL);
                break;
            case WITHER_SKELETON:
                a(ze.WITHER);
                break;
            case STRAY:
                a(ze.STRAY);
                break;
        }
    }
}
