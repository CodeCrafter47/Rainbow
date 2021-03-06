package org.projectrainbow.mixins;

import PluginReference.MC_Entity;
import PluginReference.MC_PrimedTNT;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityTNTPrimed;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(EntityTNTPrimed.class)
public abstract class MixinEntityTNTPrimed extends MixinEntity implements MC_PrimedTNT {

    @Shadow
    private EntityLivingBase tntPlacedBy;

    @Override
    public MC_Entity getEntityWhichCausedExplosion() {
        return (MC_Entity) tntPlacedBy;
    }
}
