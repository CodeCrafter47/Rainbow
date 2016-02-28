package org.projectrainbow.mixins;

import net.minecraft.src.ContainerDispenser;
import net.minecraft.src.TileEntityDropper;
import net.minecraft.src.qg;
import org.projectrainbow.interfaces.IMixinContainerDispenser;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ContainerDispenser.class)
public class MixinContainerDispenser implements IMixinContainerDispenser {
    @Shadow
    private qg a;

    @Override
    public boolean isDropper() {
        return a instanceof TileEntityDropper;
    }
}
