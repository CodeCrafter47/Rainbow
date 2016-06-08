package org.projectrainbow.mixins;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.ChunkPrimer;
import org.projectrainbow.GeneratedColumnWrapper;
import org.projectrainbow.Hooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin(Biome.class)
public class MixinBiome {

    @Inject(method = "generateBiomeTerrain", at = @At("HEAD"), cancellable = true)
    private void onChunkGen(World var1, Random var2, ChunkPrimer var3, int xCoord, int zCoord, double var6, CallbackInfo callbackInfo) {
        GeneratedColumnWrapper gcw = new GeneratedColumnWrapper();

        Hooks.onGenerateWorldColumn(xCoord, zCoord, gcw);

        IBlockState var20;

        if (gcw.isChanged) {
            for (int var19 = 0; var19 < 256; ++var19) {
                var20 = Block.getStateById(gcw.data[var19]);
                var3.setBlockState(xCoord, var19, zCoord, var20);
            }
            callbackInfo.cancel();
        }
    }
}
