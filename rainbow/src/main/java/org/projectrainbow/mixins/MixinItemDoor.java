package org.projectrainbow.mixins;

import net.minecraft.src.Block;
import net.minecraft.src.ItemDoor;
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
