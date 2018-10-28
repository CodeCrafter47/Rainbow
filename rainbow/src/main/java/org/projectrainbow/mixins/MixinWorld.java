package org.projectrainbow.mixins;

import PluginReference.MC_Entity;
import PluginReference.MC_EventInfo;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.GameRules;
import net.minecraft.world.IWorldReaderBase;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.storage.WorldInfo;
import org.projectrainbow.Hooks;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Random;

@Mixin(World.class)
public abstract class MixinWorld {

    @Final
    @Shadow
    public Dimension dimension;

    @Final
    @Shadow
    public List<Entity> loadedEntityList;

    @Final
    @Shadow
    public Random rand;

    @Shadow
    protected IChunkProvider chunkProvider;

    @Shadow
    public abstract boolean setBlockState(BlockPos pos, IBlockState state);

    @Shadow
    public abstract BlockPos getSpawnPoint();

    @Shadow
    public abstract WorldInfo getWorldInfo();

    @Shadow
    public abstract GameRules getGameRules();

    @Shadow
    public abstract long shadow$getDayTime();

    @Shadow
    public abstract long shadow$getGameTime();

    @Shadow
    public abstract IBlockState getBlockState(BlockPos pos);

    @Shadow
    public abstract TileEntity getTileEntity(BlockPos pos);

    @Shadow
    public abstract Biome getBiome(BlockPos pos);

    @Shadow
    public abstract Chunk getChunk(BlockPos pos);

    @Shadow
    public abstract Chunk getChunk(int x, int z);

    @Inject(method = "spawnEntity", at = @At("HEAD"), cancellable = true)
    private void onEntitySpawned(Entity entity, CallbackInfoReturnable<Boolean> callbackInfo) {

        int var2 = MathHelper.floor(entity.posX / 16.0D);
        int var3 = MathHelper.floor(entity.posZ / 16.0D);
        boolean var4 = entity.forceSpawn;
        if (entity instanceof EntityPlayer) {
            var4 = true;
        }

        if (var4 || ((IWorldReaderBase)this).isChunkLoaded(var2, var3, false)) {
            MC_EventInfo ei = new MC_EventInfo();
            Hooks.onAttemptEntitySpawn((MC_Entity) entity, ei);
            if (ei.isCancelled) {
                callbackInfo.setReturnValue(false);
            }
        }
    }

    /* todo
    @Inject(method = "updateWeather", at = @At("HEAD"), cancellable = true)
    private void updateWeather(CallbackInfo callbackInfo) {
        if (!_DiwUtils.DoWeather) {
            callbackInfo.cancel();
        }
    }*/
}
