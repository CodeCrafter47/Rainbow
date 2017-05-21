package org.projectrainbow.mixins;

import PluginReference.MC_Chunk;
import net.minecraft.world.chunk.Chunk;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Chunk.class)
public class MixinChunk implements MC_Chunk {
    @Shadow
    @Final
    public int x;
    @Shadow
    @Final
    public int z;


    @Override
    public int getCX() {
        return x;
    }

    @Override
    public int getCZ() {
        return z;
    }
}
