package org.projectrainbow.mixins;

import net.minecraft.src.ContainerHopper;
import net.minecraft.src.EntityMinecartHopper;
import net.minecraft.src.qg;
import org.projectrainbow.interfaces.IMixinContainerHopper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ContainerHopper.class)
public class MixinContainerHopper implements IMixinContainerHopper {
    @Shadow
    @Final
    private qg a;

    @Override
    public boolean isMinecart() {
        return a instanceof EntityMinecartHopper;
    }
}
