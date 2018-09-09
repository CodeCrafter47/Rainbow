package org.projectrainbow.mixins;

import PluginReference.*;
import com.google.common.base.MoreObjects;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.BlockStandingSign;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTSizeTracker;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.profiler.Profiler;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.gen.ChunkProviderServer;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.WorldInfo;
import net.minecraft.world.storage.WorldSavedDataStorage;
import org.apache.logging.log4j.LogManager;
import org.projectrainbow.BlockWrapper;
import org.projectrainbow.PluginHelper;
import org.projectrainbow.ServerWrapper;
import org.projectrainbow._DiwUtils;
import org.projectrainbow.interfaces.IMixinWorldServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import javax.annotation.Nullable;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Mixin(WorldServer.class)
public abstract class MixinWorldServer extends World implements MC_World, IMixinWorldServer {


    protected MixinWorldServer(ISaveHandler iSaveHandler, @Nullable WorldSavedDataStorage worldSavedDataStorage, WorldInfo worldInfo, Dimension dimension, Profiler profiler, boolean b) {
        super(iSaveHandler, worldSavedDataStorage, worldInfo, dimension, profiler, b);
        // dummy
    }

    @Shadow
    public abstract boolean isChunkLoaded(int x, int z, boolean ignored);

    /*
    @Redirect(method = "canAddEntity", at = @At(value = "INVOKE", target = "org/apache/logging/log4j/Logger.warn(Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;)V", remap = false))
    private void doLogWarning(Logger logger, String message, Object arg1, Object arg2) {
        if (!_DiwUtils.DoHideAnnoyingDefaultServerOutput) {
            logger.warn(message, arg1, arg2);
        }
    }*/

    @Shadow public abstract boolean spawnEntity(Entity entity);

    @Override
    @Deprecated
    public MC_Block getBlockFromName(String var1) {
        return ServerWrapper.getInstance().getBlockFromName(var1);
    }

    @Override
    public MC_Block getBlockAt(int x, int y, int z) {
        return new BlockWrapper(getBlockState(new BlockPos(x, y, z)));
    }

    @Override
    public MC_DirectionNESWUD getBlockFacing(int x, int y, int z) {
        try {
            EnumFacing value = getBlockState(new BlockPos(x, y, z)).getValue(BlockDirectional.FACING);
            return MoreObjects.firstNonNull(PluginHelper.directionMap.get(value), MC_DirectionNESWUD.UNSPECIFIED);
        } catch (Throwable ignored) {
            return MC_DirectionNESWUD.UNSPECIFIED;
        }
    }

    @Override
    public void setBlockRotation(int x, int y, int z, int rotation) {
        BlockPos blockPos = new BlockPos(x, y, z);
        try {
            setBlockState(blockPos, getBlockState(blockPos).func_206870_a(BlockStandingSign.ROTATION, rotation));
        } catch (Throwable ignored) {
            // block not a sign, ignore
        }
    }

    @Override
    public void setBlockFacing(int x, int y, int z, MC_DirectionNESWUD direction) {
        if (direction == null || direction == MC_DirectionNESWUD.UNSPECIFIED) {
            return;
        }

        BlockPos blockPos = new BlockPos(x, y, z);
        try {
            setBlockState(blockPos, getBlockState(blockPos).func_206870_a(BlockDirectional.FACING, PluginHelper.directionMap.inverse().get(direction)));
        } catch (Throwable ignored) {
            // block not directional, ignore
        }
    }

    @Override
    public int getBlockRotation(int x, int y, int z) {
        BlockPos coords = new BlockPos(x, y, z);

        try {
            return getBlockState(coords).getValue(BlockStandingSign.ROTATION);
        } catch (Throwable ignored) {
            return 0;
        }
    }

    @Override
    public MC_Block getBlockAt(MC_Location loc) {
        return new BlockWrapper(getBlockState(new BlockPos(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ())));
    }

    @Override
    public void setBlockAt(int x, int y, int z, MC_Block block) {
        BlockPos blockPos = new BlockPos(x, y, z);
        setBlockState(blockPos, ((BlockWrapper) block).m_blockObject.getDefaultState());
    }

    @Override
    public void setBlockAt(MC_Location loc, MC_Block block) {
        BlockPos blockPos = new BlockPos(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
        setBlockState(blockPos, ((BlockWrapper) block).m_blockObject.getDefaultState());
    }

    @Override
    @Deprecated
    public void setBlockAt(int x, int y, int z, MC_Block block, int metaData) {
        BlockPos blockPos = new BlockPos(x, y, z);
        setBlockState(blockPos, ((BlockWrapper) block).m_blockObject.getDefaultState());
    }

    @Override
    @Deprecated
    public void setBlockAt(MC_Location loc, MC_Block block, int metaData) {
        BlockPos blockPos = new BlockPos(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
        setBlockState(blockPos, ((BlockWrapper) block).m_blockObject.getDefaultState());
    }

    @Override
    public boolean breakNaturallyAt(int x, int y, int z) {
        return breakNaturallyAt(x, y, z, null);
    }

    @Override
    public boolean breakNaturallyAt(int x, int y, int z, MC_ItemStack toolOptional) {
        BlockPos coords = new BlockPos(x, y, z);
        IBlockState bs = getBlockState(coords);
        Block bo = bs.getBlock();
        Material mat = bo.getMaterial(bs);

        if (mat == Material.AIR) {
            return false;
        } else {
            bs.func_196949_c(this, coords, toolOptional == null ? 0 : toolOptional.getEnchantmentLevel(MC_EnchantmentType.FORTUNE)); // dropBlockAsItem
            setBlockState(coords, Blocks.AIR.getDefaultState());
            return true;
        }
    }

    @Override
    public MC_Location getSpawnLocation() {
        BlockPos spawnPoint = getSpawnPoint();
        return new MC_Location(spawnPoint.getX(), spawnPoint.getY(), spawnPoint.getZ(), getDimension());
    }

    @Override
    public String getName() {
        return super.getWorldInfo().getWorldName() + provider.getDimensionType().getSuffix();
    }

    @Override
    public List<MC_Entity> getEntities() {
        return (List<MC_Entity>) (Object) Collections.unmodifiableList(loadedEntityList);
    }

    @Override
    public boolean getGameRuleBool(MC_GameRuleType var1) {
        return getGameRules().getBoolean(PluginHelper.gameRuleMap.get(var1));
    }

    @Override
    public void setGameRule(MC_GameRuleType var1, boolean var2) {
        String rule = PluginHelper.gameRuleMap.get(var1);
        getGameRules().setOrCreateGameRule(rule, "" + var2, _DiwUtils.getMinecraftServer());
    }

    @Override
    public MC_Sign getSignAt(MC_Location loc) {
        try {
            return (MC_Sign) getTileEntity(new BlockPos(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()));
        } catch (Throwable ignored) {
            // no sign here
            return null;
        }
    }

    @Override
    public MC_Chest getChestAt(MC_Location loc) {
        try {
            return (MC_Chest) getTileEntity(new BlockPos(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()));
        } catch (Throwable ignored) {
            // no chest here
            return null;
        }
    }

    @Override
    public MC_Entity spawnEntity(MC_EntityType var1, MC_Location loc, String name) {
        try {
            Class<? extends Entity> aClass = PluginHelper.entityMap.inverse().get(var1);

            if (aClass == null) {
                LogManager.getLogger("Minecraft").warn("No class associated with EntityType " + var1);
                return null;
            }

            Constructor<? extends Entity> constructor = aClass.getConstructor(World.class);

            Entity entity = constructor.newInstance(this);

            entity.setPositionAndRotation(loc.x, loc.y, loc.z,
                    loc.yaw, loc.pitch);
            if (name != null) {
                ((MC_Entity)entity).setCustomName(name);
            }
            entity.forceSpawn = true;

            if (!spawnEntity(entity)) {
                return null;
            }
            return (MC_Entity) entity;
        } catch (NoSuchMethodException e) {
            LogManager.getLogger("Minecraft").warn("Failed to spawn entity " + var1, e);
        } catch (IllegalAccessException e) {
            LogManager.getLogger("Minecraft").warn("Failed to spawn entity " + var1, e);
        } catch (InstantiationException e) {
            LogManager.getLogger("Minecraft").warn("Failed to spawn entity " + var1, e);
        } catch (InvocationTargetException e) {
            LogManager.getLogger("Minecraft").warn("Failed to spawn entity " + var1, e);
        }
        return null;
    }

    @Override
    public MC_Entity dropItem(MC_ItemStack var1, MC_Location var2, String var3) {
        float v = 0.5F;
        double var4 = (double) (rand.nextFloat() * v) + (double) (1.0F - v) * 0.5D;
        double var6 = (double) (rand.nextFloat() * v) + (double) (1.0F - v) * 0.5D;
        double var8 = (double) (rand.nextFloat() * v) + (double) (1.0F - v) * 0.5D;
        EntityItem var10 = new EntityItem(this, var2.x + var4, var2.y + var6, var2.z + var8, PluginHelper.getItemStack(var1));
        var10.setDefaultPickupDelay();
        spawnEntity(var10);
        return (MC_Entity) var10;
    }

    @Override
    public int getDimension() {
        return provider.getDimensionType().getId();
    }

    @Override
    public int getDayTime() {
        return (int) getWorldTime();
    }

    @Override
    public int getGameTime() {
        return (int) getTotalWorldTime();
    }

    @Override
    public MC_Container getContainerAt(MC_Location loc) {
        try {
            return (MC_Container) getTileEntity(new BlockPos(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()));
        } catch (Throwable ignored) {
            // no container here
            return null;
        }
    }

    @Override
    public MC_WorldBiomeType getBiomeTypeAt(int x, int z) {
        Biome biomeGenBase = getBiome(new BlockPos(x, 0, z));

        return MoreObjects.firstNonNull(PluginHelper.biomeMap.get(biomeGenBase), MC_WorldBiomeType.UNSPECIFIED);
    }

    @Override
    public void setBiomeTypeAt(int x, int z, MC_WorldBiomeType var3) {
        if (var3 == null || var3 == MC_WorldBiomeType.UNSPECIFIED) {
            return;
        }
        Biome biomeGenBase = PluginHelper.biomeMap.inverse().get(var3);
        BlockPos blockPos = new BlockPos(x, 0, z);
        Chunk var2 = this.getChunk(blockPos);
        int xInChunk = x & 15;
        int zInChunk = z & 15;

        var2.func_201590_e()[zInChunk << 4 | xInChunk] = biomeGenBase;
    }

    @Override
    public boolean loadChunk(int x, int z) {
        return super.getChunk(x, z) != null;
    }

    @Override
    public boolean isChunkLoaded(int x, int z) {
        return isChunkLoaded(x, z, false);
    }

    @Override
    public List<MC_Chunk> getLoadedChunks() {
        return (List<MC_Chunk>) new ArrayList(((ChunkProviderServer) super.chunkProvider).a()); // getLoadedChunks
    }

    @Override
    public MC_NoteBlock getNoteBlockAt(MC_Location location) {
        TileEntity tileEntity = getTileEntity(new BlockPos(location.getBlockX(), location.getBlockY(), location.getBlockZ()));
        return tileEntity instanceof MC_NoteBlock ? (MC_NoteBlock) tileEntity : null;
    }

    @Override
    public int getClientDimension() {
        return getDimension();
    }

    @Override
    public MC_Entity spawnEntity(MC_Location loc, byte[] rawEntityData) {
        try {
            if (rawEntityData.length == 0) {
                return null;
            }
            NBTTagCompound compound = new NBTTagCompound();
            ByteArrayInputStream bis = new ByteArrayInputStream(rawEntityData);
            DataInputStream dis = new DataInputStream(bis);

            compound.read(dis, 0, new NBTSizeTracker(Long.MAX_VALUE));
            bis.close();

            Entity entity = EntityType.func_200716_a(compound, this);
            entity.setPositionAndRotation(loc.x, loc.y, loc.z, loc.yaw, loc.pitch);
            entity.forceSpawn = true;

            if (spawnEntity(entity)) {
                return (MC_Entity) entity;
            }
        } catch (Exception var6) {
            var6.printStackTrace();
        }
        return null;
    }
}
