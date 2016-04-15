package org.projectrainbow.mixins;

import net.minecraft.src.ItemStack;
import net.minecraft.src.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Slot.class)
public abstract class MixinSlot {

    @Shadow
    public abstract void putStack(ItemStack var1);

    @Shadow
    public abstract ItemStack getStack();

    @Overwrite
    public boolean getHasStack() {
        if (getStack() != null && getStack().stackSize == 0) {
            putStack(null);
        }
        return this.getStack() != null;
    }
}
