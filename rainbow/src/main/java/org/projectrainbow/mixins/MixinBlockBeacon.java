package org.projectrainbow.mixins;

import net.minecraft.block.BlockBeacon;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBeacon;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(BlockBeacon.class)
public class MixinBlockBeacon {

    @Overwrite
    public static void updateColorAsync(final World world, final BlockPos blockPos) {
        // Based on Paper's "Shame on you Mojang", found at:
        // https://github.com/PaperMC/Paper/blob/master/Spigot-Server-Patches/0202-Shame-on-you-Mojang.patch
        BlockPos blockPos2;
        Chunk chunk = world.getChunkFromBlockCoords(blockPos);
        for (int i = blockPos.getY() - 1; i >= 0 && chunk.canSeeSky(blockPos2 = new BlockPos(blockPos.getX(), i, blockPos.getZ())); --i) {
            IBlockState iBlockState = world.getBlockState(blockPos2);
            if (iBlockState.getBlock() != Blocks.BEACON) continue;
            
            TileEntity tileEntity = world.getTileEntity(blockPos2);
            if (tileEntity instanceof TileEntityBeacon) {
                ((TileEntityBeacon)tileEntity).updateBeacon();
                world.addBlockEvent(blockPos2, Blocks.BEACON, 1, 0);
            }
        }
    }
}
