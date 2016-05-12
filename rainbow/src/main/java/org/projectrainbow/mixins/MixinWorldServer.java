package org.projectrainbow.mixins;

import PluginReference.MC_Block;
import PluginReference.MC_Chest;
import PluginReference.MC_Chunk;
import PluginReference.MC_Container;
import PluginReference.MC_DirectionNESWUD;
import PluginReference.MC_EnchantmentType;
import PluginReference.MC_Entity;
import PluginReference.MC_EntityType;
import PluginReference.MC_GameRuleType;
import PluginReference.MC_ItemStack;
import PluginReference.MC_Location;
import PluginReference.MC_NoteBlock;
import PluginReference.MC_Sign;
import PluginReference.MC_World;
import PluginReference.MC_WorldBiomeType;
import com.google.common.base.Objects;
import net.minecraft.src.BiomeGenBase;
import net.minecraft.src.Block;
import net.minecraft.src.BlockDirectional;
import net.minecraft.src.BlockPos;
import net.minecraft.src.BlockStandingSign;
import net.minecraft.src.Blocks;
import net.minecraft.src.Chunk;
import net.minecraft.src.ChunkProviderServer;
import net.minecraft.src.CommandGameRule;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityItem;
import net.minecraft.src.EntityList;
import net.minecraft.src.EnumFacing;
import net.minecraft.src.IBlockState;
import net.minecraft.src.ISaveHandler;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.Profiler;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;
import net.minecraft.src.WorldInfo;
import net.minecraft.src.WorldProvider;
import net.minecraft.src.WorldServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.projectrainbow.BlockWrapper;
import org.projectrainbow.EmptyItemStack;
import org.projectrainbow.PluginHelper;
import org.projectrainbow.ServerWrapper;
import org.projectrainbow._DiwUtils;
import org.projectrainbow.interfaces.IMixinNBTBase;
import org.projectrainbow.interfaces.IMixinWorldServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Mixin(WorldServer.class)
public abstract class MixinWorldServer extends World implements MC_World, IMixinWorldServer {

    protected MixinWorldServer(ISaveHandler iSaveHandler, WorldInfo worldInfo, WorldProvider worldProvider, Profiler profiler, boolean b) {
        super(iSaveHandler, worldInfo, worldProvider, profiler, b);
        // dummy
    }

    @Shadow
    protected abstract boolean isChunkLoaded(int x, int z, boolean ignored);

    @Redirect(method = "i(Lnet/minecraft/src/Entity;)Z", at = @At(value = "INVOKE", target = "warn", remap = false))
    private void doLogWarning(Logger logger, String message) {
        if (_DiwUtils.DoHideAnnoyingDefaultServerOutput == false) {
            logger.warn(message);
        }
    }

    @Override
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
            return Objects.firstNonNull(PluginHelper.directionMap.get(value), MC_DirectionNESWUD.UNSPECIFIED);
        } catch (Throwable ignored) {
            return MC_DirectionNESWUD.UNSPECIFIED;
        }
    }

    @Override
    public void setBlockRotation(int x, int y, int z, int rotation) {
        BlockPos blockPos = new BlockPos(x, y, z);
        try {
            setBlockState(blockPos, getBlockState(blockPos).withProperty(BlockStandingSign.ROTATION, rotation));
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
            setBlockState(blockPos, getBlockState(blockPos).withProperty(BlockDirectional.FACING, PluginHelper.directionMap.inverse().get(direction)));
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
    public void setBlockAt(int x, int y, int z, MC_Block block, int metaData) {
        // todo we have duplicate metadata as it is already in MC_Block, suggestion: add a method to the api that does not take the metaData argument
        BlockPos blockPos = new BlockPos(x, y, z);
        setBlockState(blockPos, ((BlockWrapper) block).m_blockObject.getStateFromMeta(metaData));
    }

    @Override
    public void setBlockAt(MC_Location loc, MC_Block block, int metaData) {
        // todo we have duplicate metadata as it is already in MC_Block, suggestion: add a method to the api that does not take the metaData argument
        BlockPos blockPos = new BlockPos(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
        setBlockState(blockPos, ((BlockWrapper) block).m_blockObject.getStateFromMeta(metaData));
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
        Material mat = bo.q(bs);

        if (mat == Material.air) {
            return false;
        } else {
            bo.dropBlockAsItem(this, coords, bs, toolOptional == null ? 0 : toolOptional.getEnchantmentLevel(MC_EnchantmentType.FORTUNE));
            setBlockState(coords, Blocks.air.getDefaultState(), 3);
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
        return super.getWorldInfo().getWorldName() + provider.getDimension().getInternalNameSuffix();
    }

    @Override
    public List<MC_Entity> getEntities() {
        return (List<MC_Entity>) (Object) Collections.unmodifiableList(loadedEntityList);
    }

    @Override
    public boolean getGameRuleBool(MC_GameRuleType var1) {
        return getGameRules().getGameRuleBooleanValue(PluginHelper.gameRuleMap.get(var1));
    }

    @Override
    public void setGameRule(MC_GameRuleType var1, boolean var2) {
        String rule = PluginHelper.gameRuleMap.get(var1);
        getGameRules().setOrCreateGameRule(rule, "" + var2);
        CommandGameRule.a(getGameRules(), rule, _DiwUtils.getMinecraftServer());
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
                entity.setCustomNameTag(name);
            }
            entity.forceSpawn = true;

            if (!spawnEntityInWorld(entity)) {
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
        EntityItem var10 = new EntityItem(this, var2.x + var4, var2.y + var6, var2.z + var8, var1 instanceof EmptyItemStack ? null : (ItemStack) (Object) var1);
        var10.setDefaultPickupDelay();
        spawnEntityInWorld(var10);
        return (MC_Entity) var10;
    }

    @Override
    public int getDimension() {
        return provider.getDimension().getDimensionId();
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
        BiomeGenBase biomeGenBase = getBiomeGenForCoords(new BlockPos(x, 0, z));

        return Objects.firstNonNull(PluginHelper.biomeMap.get(biomeGenBase), MC_WorldBiomeType.UNSPECIFIED);
    }

    @Override
    public void setBiomeTypeAt(int x, int z, MC_WorldBiomeType var3) {
        if (var3 == null || var3 == MC_WorldBiomeType.UNSPECIFIED) {
            return;
        }
        BiomeGenBase biomeGenBase = PluginHelper.biomeMap.inverse().get(var3);
        BlockPos blockPos = new BlockPos(x, 0, z);
        Chunk var2 = this.getChunkFromBlockCoords(blockPos);
        int xInChunk = x & 15;
        int zInChunk = z & 15;

        var2.getBiomeArray()[zInChunk << 4 | xInChunk] = (byte) BiomeGenBase.a(biomeGenBase);
    }

    @Override
    public boolean loadChunk(int x, int z) {
        return super.getChunkFromChunkCoords(x, z) != null;
    }

    @Override
    public boolean isChunkLoaded(int x, int z) {
        return isChunkLoaded(x, z, false);
    }

    @Override
    public List<MC_Chunk> getLoadedChunks() {
        return (List<MC_Chunk>) new ArrayList(((ChunkProviderServer) v).a());
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

            ((IMixinNBTBase) compound).read1(dis);
            bis.close();

            Entity entity = EntityList.createEntityFromNBT(compound, this);
            entity.setPositionAndRotation(loc.x, loc.y, loc.z, loc.yaw, loc.pitch);
            entity.forceSpawn = true;

            if (spawnEntityInWorld(entity)) {
                return (MC_Entity) entity;
            }
        } catch (Exception var6) {
            var6.printStackTrace();
        }
        return null;
    }
}
