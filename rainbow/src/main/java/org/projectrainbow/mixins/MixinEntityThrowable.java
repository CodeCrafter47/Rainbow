package org.projectrainbow.mixins;

import PluginReference.MC_Entity;
import PluginReference.MC_Projectile;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(EntityThrowable.class)
public abstract class MixinEntityThrowable extends MixinEntity implements MC_Projectile {

    @Shadow
    private EntityLivingBase thrower;

    @Override
    public MC_Entity getProjectileSource() {
        return (MC_Entity) thrower;
    }
}
