package org.projectrainbow.mixins;

import PluginReference.MC_Arrow;
import PluginReference.MC_Entity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntityArrow;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(EntityArrow.class)
public abstract class MixinEntityArrow extends MixinEntity implements MC_Arrow {
    @Shadow
    public Entity shootingEntity;

    @Override
    public MC_Entity getProjectileSource() {
        return (MC_Entity) shootingEntity;
    }
}
