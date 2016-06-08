package org.projectrainbow.mixins;

import net.minecraft.inventory.ContainerDispenser;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntityDropper;
import org.projectrainbow.interfaces.IMixinContainerDispenser;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ContainerDispenser.class)
public class MixinContainerDispenser implements IMixinContainerDispenser {
    @Shadow
    private IInventory dispenserInventory;

    @Override
    public boolean isDropper() {
        return dispenserInventory instanceof TileEntityDropper;
    }
}
