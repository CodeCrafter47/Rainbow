package org.projectrainbow.mixins;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import org.projectrainbow.interfaces.IMixinItemBlock;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ItemBlock.class)
public class MixinItemBlock implements IMixinItemBlock {
    @Shadow
    @Final
    private Block block;

    @Override
    public Block getBlock() {
        return block;
    }
}
