package org.projectrainbow.mixins;

import net.minecraft.entity.item.EntityMinecartHopper;
import net.minecraft.inventory.ContainerHopper;
import net.minecraft.inventory.IInventory;
import org.projectrainbow.interfaces.IMixinContainerHopper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ContainerHopper.class)
public class MixinContainerHopper implements IMixinContainerHopper {
    @Shadow
    @Final
    private IInventory hopperInventory;

    @Override
    public boolean isMinecart() {
        return hopperInventory instanceof EntityMinecartHopper;
    }
}
