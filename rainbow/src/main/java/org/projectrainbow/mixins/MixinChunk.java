package org.projectrainbow.mixins;

import PluginReference.MC_Chunk;
import net.minecraft.src.Chunk;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Chunk.class)
public class MixinChunk implements MC_Chunk {
    @Shadow
    @Final
    public int xPosition;
    @Shadow
    @Final
    public int zPosition;


    @Override
    public int getCX() {
        return xPosition;
    }

    @Override
    public int getCZ() {
        return zPosition;
    }
}
