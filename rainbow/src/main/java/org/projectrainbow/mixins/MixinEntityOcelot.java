package org.projectrainbow.mixins;

import PluginReference.MC_Ocelot;
import PluginReference.MC_OcelotType;
import net.minecraft.src.EntityOzelot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(EntityOzelot.class)
public abstract class MixinEntityOcelot implements MC_Ocelot {
    @Shadow
    public abstract int di();

    @Shadow
    public abstract void l(int var1);

    @Override
    public MC_OcelotType getCatType() {
        int idx = di();

        return idx == 0
                ? MC_OcelotType.WILD
                : (idx == 1
                ? MC_OcelotType.BLACK
                : (idx == 2
                ? MC_OcelotType.RED
                : (idx == 3
                ? MC_OcelotType.SIAMESE
                : MC_OcelotType.UNKNOWN)));
    }

    @Override
    public void setCatType(MC_OcelotType catType) {
        if (catType == MC_OcelotType.WILD) {
            l(0);
        }

        if (catType == MC_OcelotType.BLACK) {
            l(1);
        }

        if (catType == MC_OcelotType.RED) {
            l(2);
        }

        if (catType == MC_OcelotType.SIAMESE) {
            l(3);
        }
    }
}
