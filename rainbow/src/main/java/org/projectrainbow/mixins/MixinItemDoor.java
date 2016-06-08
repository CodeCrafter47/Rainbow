package org.projectrainbow.mixins;

import net.minecraft.block.Block;
import net.minecraft.item.ItemDoor;
import org.projectrainbow.interfaces.IMixinItemDoor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ItemDoor.class)
public class MixinItemDoor implements IMixinItemDoor {
    @Shadow
    private Block block;

    @Override
    public Block getBlock() {
        return block;
    }
}
