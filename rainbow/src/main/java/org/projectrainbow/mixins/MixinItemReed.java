package org.projectrainbow.mixins;

import net.minecraft.src.Block;
import net.minecraft.src.ItemReed;
import org.projectrainbow.interfaces.IMixinItemReed;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ItemReed.class)
public class MixinItemReed implements IMixinItemReed {
    @Shadow
    private Block block;

    @Override
    public Block getBlock() {
        return block;
    }
}
