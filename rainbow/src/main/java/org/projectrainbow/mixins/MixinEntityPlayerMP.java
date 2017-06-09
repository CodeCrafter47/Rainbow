package org.projectrainbow.mixins;

import PluginReference.MC_ContainerType;
import PluginReference.MC_Entity;
import PluginReference.MC_EventInfo;
import PluginReference.MC_GameMode;
import PluginReference.MC_InventoryGUI;
import PluginReference.MC_ItemStack;
import PluginReference.MC_Location;
import PluginReference.MC_Player;
import PluginReference.MC_Server;
import PluginReference.MC_World;
import com.google.common.base.MoreObjects;
import com.google.common.io.Files;
import joebkt._JOT_OnlineTimeEntry;
import joebkt._SerializableLocation;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityMinecartContainer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerBeacon;
import net.minecraft.inventory.ContainerBrewingStand;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.ContainerDispenser;
import net.minecraft.inventory.ContainerEnchantment;
import net.minecraft.inventory.ContainerFurnace;
import net.minecraft.inventory.ContainerHopper;
import net.minecraft.inventory.ContainerHorseChest;
import net.minecraft.inventory.ContainerHorseInventory;
import net.minecraft.inventory.ContainerMerchant;
import net.minecraft.inventory.ContainerPlayer;
import net.minecraft.inventory.ContainerRepair;
import net.minecraft.inventory.ContainerWorkbench;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryEnderChest;
import net.minecraft.inventory.InventoryLargeChest;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.play.server.SPacketChat;
import net.minecraft.network.play.server.SPacketPlayerListHeaderFooter;
import net.minecraft.network.play.server.SPacketRespawn;
import net.minecraft.network.play.server.SPacketSetExperience;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.network.play.server.SPacketSpawnPosition;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerInteractionManager;
import net.minecraft.server.management.PlayerList;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.GameRules;
import net.minecraft.world.GameType;
import net.minecraft.world.WorldServer;
import org.projectrainbow.BlockWrapper;
import org.projectrainbow.GUIInventory;
import org.projectrainbow.Hooks;
import org.projectrainbow.PluginHelper;
import org.projectrainbow.ServerWrapper;
import org.projectrainbow._Backpack;
import org.projectrainbow._DiwUtils;
import org.projectrainbow._EconomyManager;
import org.projectrainbow._HomeUtils;
import org.projectrainbow._JOT_OnlineTimeUtils;
import org.projectrainbow._PermMgr;
import org.projectrainbow.commands._CmdNameColor;
import org.projectrainbow.interfaces.IMixinContainerDispenser;
import org.projectrainbow.interfaces.IMixinContainerHopper;
import org.projectrainbow.interfaces.IMixinEntityPlayerMP;
import org.projectrainbow.interfaces.IMixinNBTBase;
import org.projectrainbow.interfaces.IMixinPlayerCapabilities;
import org.projectrainbow.interfaces.IMixinSPacketPlayerListHeaderFooter;
import org.projectrainbow.interfaces.IMixinWorldServer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.SocketAddress;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Mixin(EntityPlayerMP.class)
public abstract class MixinEntityPlayerMP extends MixinEntityPlayer implements IMixinEntityPlayerMP, MC_Player {
    /* Backpack */
    private _Backpack backpack = new _Backpack();

    private float naturalHealthRegenAmount = 1.0F;

    private MC_Location compassTarget = null;

    /*
     * @Shadow allows us to access field of the target class.
     */
    @Shadow
    public NetHandlerPlayServer connection;

    @Shadow
    @Final
    public PlayerInteractionManager interactionManager;

    @Shadow
    private int lastExperience;

    /*
     * we can also use @Shadow to access methods in the target class
     */
    @Shadow
    public abstract void sendContainerToPlayer(Container c);

    @Shadow
    public abstract void sendPlayerAbilities();

    @Shadow
    public abstract Entity getSpectatingEntity();

    @Shadow
    public abstract void setSpectatingEntity(Entity var1);

    @Shadow
    public abstract void setGameType(GameType var1);

    /*
     * add teleport helper methods
     */
    @Override
    public void teleport(WorldServer world, double x, double y, double z) {
        teleport(world, x, y, z, rotationYaw, rotationPitch);
    }

    @Override
    public void teleport(WorldServer world, double x, double y, double z, float yaw, float pitch) {
        teleport(world, x, y, z, yaw, pitch, true);
    }

    public void teleport(WorldServer world, double x, double y, double z, float yaw, float pitch, boolean safe) {
        dismountRidingEntity();
        for (Entity passenger : getPassengers()) {
            passenger.dismountRidingEntity();
        }
        // Close open containers
        if (openContainer != ((EntityPlayerMP) (Object) this).inventoryContainer) {
            ((EntityPlayerMP) (Object) this).closeScreen();
        }
        if (world != this.world) {
            MinecraftServer mcServer = _DiwUtils.getMinecraftServer();
            final WorldServer fromWorld = (WorldServer) this.world;
            final WorldServer toWorld = world;
            fromWorld.getEntityTracker().removePlayerFromTrackers((EntityPlayerMP) (Object) this);
            fromWorld.getPlayerChunkMap().removePlayer((EntityPlayerMP) (Object) this);
            mcServer.getPlayerList().getPlayers().remove(this);
            fromWorld.getEntityTracker().untrack((EntityPlayerMP) (Object) this);

            fromWorld.removeEntityDangerously((EntityPlayerMP) (Object) this);
            int currentDim = dimension;
            dimension = ((MC_World) toWorld).getDimension();
            setPositionAndRotation(x, y, z, yaw, pitch);

            toWorld.getChunkProvider().loadChunk((int) posX >> 4, (int) posZ >> 4);

            EntityPlayerMP entityplayermp1 = (EntityPlayerMP) (Object) this;

            // Support vanilla clients going into custom dimensions
            int clientDimension = ((IMixinWorldServer) toWorld).getClientDimension();
            // Force vanilla client to refresh their chunk cache if same dimension
            if (((IMixinWorldServer) fromWorld).getClientDimension() == clientDimension) {
                entityplayermp1.connection.sendPacket(
                        new SPacketRespawn((byte) (clientDimension >= 0 ? -1 : 0), toWorld.getDifficulty(), toWorld.getWorldInfo().getTerrainType(),
                                entityplayermp1.interactionManager.getGameType()));
            }

            entityplayermp1.connection.sendPacket(new SPacketRespawn(clientDimension, toWorld.getDifficulty(), toWorld.getWorldInfo().getTerrainType(), entityplayermp1.interactionManager.getGameType()));
            setWorld(toWorld);
            isDead = false;
            while (safe && !toWorld.getCollisionBoxes((Entity) (Object) this, this.getEntityBoundingBox()).isEmpty() && this.posY < 255.0D) {
                this.setPosition(this.posX, this.posY + 1.0D, this.posZ);
            }
            entityplayermp1.connection.setPlayerLocation(entityplayermp1.posX, entityplayermp1.posY, entityplayermp1.posZ, entityplayermp1.rotationYaw, entityplayermp1.rotationPitch);
            entityplayermp1.setSneaking(false);
            MC_Location compassTarget = getCompassTarget();
            entityplayermp1.connection.sendPacket(new SPacketSpawnPosition(new BlockPos(compassTarget.getBlockX(), compassTarget.getBlockY(), compassTarget.getBlockZ())));
            entityplayermp1.connection.sendPacket(new SPacketSetExperience(entityplayermp1.experience, entityplayermp1.experienceTotal, entityplayermp1.experienceLevel));
            mcServer.getPlayerList().updateTimeAndWeatherForPlayer(entityplayermp1, toWorld);
            toWorld.getPlayerChunkMap().addPlayer(entityplayermp1);
            toWorld.spawnEntity(entityplayermp1);
            mcServer.getPlayerList().getPlayers().add(entityplayermp1);
            entityplayermp1.interactionManager.setWorld(toWorld);
            entityplayermp1.sendContainerToPlayer(openContainer);
            entityplayermp1.setHealth(entityplayermp1.getHealth());

            fromWorld.resetUpdateEntityTick();
            toWorld.resetUpdateEntityTick();
        } else {
            connection.setPlayerLocation(x, y, z, yaw, pitch);
        }
    }

    @Inject(method = "readEntityFromNBT", at = @At("HEAD"))
    public void hook_readEntityFromNBT(NBTTagCompound nbt, CallbackInfo callbackInfo) {
        NBTTagList tagList = new NBTTagList();

        try {
            File file = this.GetBackpackFile();
            if (file.exists()) {
                FileInputStream f = new FileInputStream(file);
                ObjectInputStream s = new ObjectInputStream(new BufferedInputStream(f));
                ((IMixinNBTBase) tagList).read1(s);
                this.backpack.loadInventoryFromNBT(tagList);
                s.close();
            }
        } catch (Exception var11) {
            String msg = "--- Error loading backpack for " + this.getName() + " : " + var11.toString();
            _DiwUtils.ConsoleMsg(msg);
        }
    }

    @Inject(method = "writeEntityToNBT", at = @At("HEAD"))
    public void hook_writeEntityToNBT(NBTTagCompound nbt, CallbackInfo callbackInfo) {
        try {
            int nActualItems = 0;
            int nPackSize = this.backpack.getSizeInventory();

            for (int i = 0; i < nPackSize; ++i) {
                ItemStack is = this.backpack.getStackInSlot(i);
                if (is != null) {
                    ++nActualItems;
                }
            }

            File directory = new File("Backpacks");
            File file = new File(directory, _DiwUtils.GetUsableFilenameFromUUID(this.getUniqueID()) + ".dat");
            if (nActualItems > 0) {
                NBTTagList tagList = this.backpack.saveInventoryToNBT();
                FileOutputStream f = new FileOutputStream(file);
                ObjectOutputStream s = new ObjectOutputStream(new BufferedOutputStream(f));
                ((IMixinNBTBase) tagList).write1(s);
                s.close();
            } else if (file.exists()) {
                file.delete();
            }
        } catch (Exception var9) {
            String msg = "--- Error saving Backpack for " + this.getName() + " : " + var9.toString();
            _DiwUtils.ConsoleMsg(msg);
        }
    }

    @Redirect(method = "onDeath", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/management/PlayerList;sendMessage(Lnet/minecraft/util/text/ITextComponent;)V"))
    private void onDeathMessage(PlayerList configurationManager, ITextComponent deathMsg, DamageSource damageSource) {
        MC_Player killer = damageSource.getEntity() instanceof MC_Player ? (MC_Player) damageSource.getEntity() : null;
        Hooks.onPlayerDeath(this, killer, PluginHelper.wrap(damageSource), deathMsg.getUnformattedText());
        configurationManager.sendMessage(deathMsg);
    }

    @Redirect(method = "onDeath", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/GameRules;getBoolean(Ljava/lang/String;)Z"))
    private boolean onDeath(GameRules gameRules, String key) {
        return "keepInventory".equals(key) && _DiwUtils.OpsKeepInventory && isOp() || gameRules.getBoolean(key);
    }

    @Inject(method = "changeDimension", at = @At("HEAD"), cancellable = true)
    private void handleChangeDimension(int newDimension, CallbackInfoReturnable<Entity> callbackInfo) {
        MC_EventInfo ei = new MC_EventInfo();
        Hooks.onAttemptPlayerChangeDimension(this, newDimension, ei);
        if (ei.isCancelled) {
            callbackInfo.setReturnValue((Entity) (Object) this);
        }
    }

    @Inject(method = "attackTargetEntityWithCurrentItem", at = @At("HEAD"), cancellable = true)
    private void onAttackEntity(Entity target, CallbackInfo callbackInfo) {
        if (interactionManager.getGameType() != GameType.SPECTATOR) {
            MC_EventInfo ei = new MC_EventInfo();
            Hooks.onAttemptAttackEntity(this, (MC_Entity) target, ei);
            if (ei.isCancelled) {
                callbackInfo.cancel();
            }
        }
    }

    @Inject(method = "displayGUIChest", at = @At("HEAD"))
    private void onContainerOpen(IInventory var1, CallbackInfo callbackInfo) {
        List<MC_ItemStack> items = new ArrayList<>();
        for (int i = 0; i < var1.getSizeInventory(); i++) {
            ItemStack stack = var1.getStackInSlot(i);
            items.add((MC_ItemStack) (Object) stack);
        }
        Hooks.onContainerOpen(this, items, var1.getName());
        for (int i = 0; i < var1.getSizeInventory(); i++) {
            MC_ItemStack stack = items.get(i);
            var1.setInventorySlotContents(i, PluginHelper.getItemStack(stack));
        }
    }

    @Inject(method = "closeScreen", at = @At("HEAD"))
    private void onContainerClosed(CallbackInfo callbackInfo) {
        MC_ContainerType containerType = MC_ContainerType.UNSPECIFIED;

        try {
            if (this.openContainer instanceof ContainerFurnace) {
                containerType = MC_ContainerType.FURNACE;
            } else if (this.openContainer instanceof ContainerHopper) {
                containerType = MC_ContainerType.HOPPER;
                if (((IMixinContainerHopper) this.openContainer).isMinecart()) {
                    containerType = MC_ContainerType.MINECART_HOPPER;
                }
            } else if (this.openContainer instanceof ContainerPlayer) {
                containerType = MC_ContainerType.PLAYER_INVENTORY;
            } else if (this.openContainer instanceof ContainerEnchantment) {
                containerType = MC_ContainerType.ENCHANTING_TABLE;
            } else if (this.openContainer instanceof ContainerRepair) {
                containerType = MC_ContainerType.ANVIL;
            } else if (this.openContainer instanceof ContainerBrewingStand) {
                containerType = MC_ContainerType.BREWING_STAND;
            } else if (this.openContainer instanceof ContainerMerchant) {
                containerType = MC_ContainerType.VILLAGER;
            } else if (this.openContainer instanceof ContainerWorkbench) {
                containerType = MC_ContainerType.CRAFTING_TABLE;
            } else if (this.openContainer instanceof ContainerBeacon) {
                containerType = MC_ContainerType.BEACON;
            } else if (this.openContainer instanceof ContainerChest) {
                ContainerChest chest = (ContainerChest) this.openContainer;
                if (chest.getLowerChestInventory() != null) {
                    if (chest.getLowerChestInventory() instanceof EntityMinecartContainer) {
                        containerType = MC_ContainerType.MINECART_CHEST;
                    } else if (chest.getLowerChestInventory() instanceof InventoryLargeChest) {
                        containerType = MC_ContainerType.CHEST_DOUBLE;
                    } else if (chest.getLowerChestInventory() instanceof InventoryEnderChest) {
                        containerType = MC_ContainerType.CHEST_ENDER;
                    } else if (chest.getLowerChestInventory() instanceof ContainerHorseChest) {
                        containerType = MC_ContainerType.CHEST_HORSE;
                    } else if (chest.getLowerChestInventory() instanceof _Backpack) {
                        containerType = MC_ContainerType.BACKPACK;
                    } else if (chest.getLowerChestInventory() instanceof TileEntityChest) {
                        TileEntityChest te = (TileEntityChest) chest.getLowerChestInventory();
                        int subType = te.getBlockMetadata();

                        containerType = MC_ContainerType.CHEST_SINGLE;
                        if (subType == 1) {
                            containerType = MC_ContainerType.CHEST_TRAPPED;
                        }
                    }
                }
            } else if (this.openContainer instanceof ContainerDispenser) {
                containerType = MC_ContainerType.DISPENSER;
                if (((IMixinContainerDispenser) this.openContainer).isDropper()) {
                    containerType = MC_ContainerType.DROPPER;
                }
            } else if (this.openContainer instanceof ContainerHorseInventory) {
                containerType = MC_ContainerType.CHEST_HORSE;
            }
        } catch (Exception var7) {
            var7.printStackTrace();
        }

        Hooks.onContainerClosed(this, containerType);
    }

    @Inject(method = "trySleep", at = @At(value = "INVOKE", target = "net.minecraft.entity.player.EntityPlayerMP.getServerWorld()Lnet/minecraft/world/WorldServer;"))
    private void startSleeping(BlockPos bed, CallbackInfoReturnable callbackInfo) {
        Hooks.onPlayerBedEnter(this, new BlockWrapper(world.getBlockState(bed)), new MC_Location(bed.getX(), bed.getY(), bed.getZ(), dimension));
    }

    @Inject(method = "wakeUpPlayer", at = @At(value = "INVOKE", target = "net.minecraft.entity.player.EntityPlayerMP.getServerWorld()Lnet/minecraft/world/WorldServer;"))
    private void finishSleeping(boolean a, boolean b, boolean c, CallbackInfo callbackInfo) {
        Hooks.onPlayerBedLeave(this, new BlockWrapper(world.getBlockState(bedLocation)), new MC_Location(bedLocation.getX(), bedLocation.getY(), bedLocation.getZ(), dimension));
    }

    @Inject(method = "setSpectatingEntity", at = @At("HEAD"), cancellable = true)
    private void onSetSpectatingEntity(Entity var1, CallbackInfo callbackInfo) {
        MC_EventInfo ei = new MC_EventInfo();
        Hooks.onAttemptSpectateEntity(this, (MC_Entity) var1, ei);
        if (ei.isCancelled) {
            callbackInfo.cancel();
        }
    }

    @Inject(method = "clonePlayer", at = @At("HEAD"))
    private void clone(EntityPlayerMP entityPlayer, boolean b, CallbackInfo callbackInfo) {
        this.backpack = ((IMixinEntityPlayerMP) entityPlayer).getBackpack();
        this.compassTarget = ((MC_Player) entityPlayer).getCompassTarget();
    }

    @Overwrite
    @Nullable
    public ITextComponent getTabListDisplayName() {
        return _DiwUtils.UpdateNameColorOnTab ? ((EntityPlayerMP) (Object) this).getDisplayName() : null;
    }

    @Override
    public _Backpack getBackpack() {
        return backpack;
    }

    public File GetBackpackFile() {
        File directory = new File("Backpacks");
        File file = new File(directory, _DiwUtils.GetUsableFilenameFromUUID(this.getUniqueID()) + ".dat");
        if (!file.exists()) {
            // try Offline UUID
            File offlineModeBackPack = new File(directory, _DiwUtils.GetUsableFilenameFromUUID(UUID.nameUUIDFromBytes(("OfflinePlayer:" + getName()).getBytes(Charset.forName("UTF-8")))) + ".dat");
            if (offlineModeBackPack.exists()) {
                try {
                    Files.move(offlineModeBackPack, file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                // try file by name (Rainbow 1.8)
                File namedBackPack = new File(directory, getName() + ".dat");
                if (namedBackPack.exists()) {
                    try {
                        Files.move(namedBackPack, file);
                    } catch (IOException e) {
                        e.printStackTrace();

                    }
                }
            }
        }
        return file;
    }

    @Override
    public UUID getUUID() {
        return getUniqueID();
    }

    @Override
    public String getIPAddress() {
        return connection.getNetworkManager().getRemoteAddress().toString();
    }

    @Override
    public void sendMessage(String var1) {
        connection.sendPacket(new SPacketChat(new TextComponentString(var1)));
    }

    @Override
    public void teleport(MC_Location var1, boolean safe) {
        teleport(_DiwUtils.getMinecraftServer().worldServerForDimension(var1.dimension), var1.x, var1.y, var1.z, var1.yaw, var1.pitch, safe);
    }

    @Override
    public void executeCommand(String var1) {
        _DiwUtils.getMinecraftServer().getCommandManager().executeCommand((ICommandSender) this, var1);
    }

    @Override
    public boolean isOp() {
        return _DiwUtils.getMinecraftServer().getPlayerList().canSendCommands(getGameProfile());
    }

    @Override
    public MC_GameMode getGameMode() {
        return PluginHelper.gamemodeMap.get(interactionManager.getGameType());
    }

    @Override
    public void setGameMode(MC_GameMode var1) {
        setGameType(PluginHelper.gamemodeMap.inverse().get(var1));
    }

    @Override
    public int getFoodLevel() {
        return foodStats.getFoodLevel();
    }

    @Override
    public void setFoodLevel(int var1) {
        foodStats.setFoodLevel(var1);
    }

    @Override
    public double getEconomyBalance() {
        return _EconomyManager.GetBalance(getUUID());
    }

    @Override
    public void setEconomyBalance(double var1) {
        _EconomyManager.SetBalance(getUUID(), var1);

    }

    @Override
    public MC_ItemStack getItemInHand() {
        return (MC_ItemStack) (Object) inventory.getCurrentItem();
    }

    @Override
    public void setItemInHand(MC_ItemStack var1) {
        inventory.mainInventory.set(inventory.currentItem, PluginHelper.getItemStack(var1));
    }

    @Override
    public MC_ItemStack getItemInOffHand() {
        return (MC_ItemStack) (Object) inventory.offHandInventory.get(0);
    }

    @Override
    public void setItemInOffHand(MC_ItemStack item) {
        inventory.offHandInventory.set(0, PluginHelper.getItemStack(item));
    }

    @Override
    public List<MC_ItemStack> getInventory() {
        return PluginHelper.copyInvList(inventory.mainInventory);
    }

    @Override
    public void setInventory(List<MC_ItemStack> items) {
        PluginHelper.updateInv(inventory.mainInventory, items);
    }

    @Override
    public void updateInventory() {
        sendContainerToPlayer(openContainer);
    }

    @Override
    public boolean isInvulnerable() {
        return isEntityInvulnerable(DamageSource.MAGIC);
    }

    @Override
    public boolean isSleeping() {
        return isPlayerSleeping();
    }

    @Override
    public boolean isAllowedFlight() {
        return capabilities.allowFlying;
    }

    @Override
    public boolean isFlying() {
        return capabilities.isFlying;
    }

    @Override
    public float getFlySpeed() {
        return capabilities.getFlySpeed();
    }

    @Override
    public float getWalkSpeed() {
        return capabilities.getWalkSpeed();
    }

    @Override
    public void setFlySpeed(float var1) {
        ((IMixinPlayerCapabilities) capabilities).setFlySpeed(var1);
        sendPlayerAbilities();
    }

    @Override
    public void setWalkSpeed(float var1) {
        ((IMixinPlayerCapabilities) capabilities).setWalkSpeed(var1);
        sendPlayerAbilities();
    }

    @Override
    public void setInvulnerable(boolean var1) {
        super.setInvulnerable(var1);
    }

    @Override
    public void setAllowFlight(boolean var1) {
        capabilities.allowFlying = var1;
        sendPlayerAbilities();
    }

    @Override
    public void setFlying(boolean var1) {
        capabilities.isFlying = var1;
        sendPlayerAbilities();
    }

    @Override
    public void giveExp(int var1) {
        addExperience(var1);
    }

    @Override
    public void giveExpLevels(int var1) {
        addExperienceLevel(var1);
    }

    @Override
    public float getExp() {
        return experience;
    }

    @Override
    public void setExp(float var1) {
        experience = var1;
        lastExperience = -1;
    }

    @Override
    public int getLevel() {
        return experienceLevel;
    }

    @Override
    public void setLevel(int var1) {
        experienceLevel = var1;
        lastExperience = -1;

    }

    @Override
    public int getTotalExperience() {
        return experienceTotal;
    }

    @Override
    public void setTotalExperience(int var1) {
        experience = 0;
        experienceLevel = 0;
        experienceTotal = 0;
        addExperience(var1);
    }

    @Override
    public boolean hasPermission(String perm) {
        return isOp() || _PermMgr.hasPermission(getUUID(), perm);
    }

    @Override
    public void setCompassTarget(MC_Location var1) {
        SPacketSpawnPosition packet = new SPacketSpawnPosition(new BlockPos(var1.getBlockX(), var1.getBlockY(), var1.getBlockZ()));
        connection.sendPacket(packet);
        compassTarget = var1;
    }

    @Override
    public MC_Location getCompassTarget() {
        return MoreObjects.firstNonNull(compassTarget, getBedRespawnLocation());
    }

    @Override
    public MC_Location getBedRespawnLocation() {
        BlockPos loc = getBedLocation();
        if (loc == null) {
            return getWorld().getSpawnLocation();
        }
        return new MC_Location(dimension, loc.getX(), loc.getY(), loc.getZ());
    }

    @Override
    public void setBedRespawnLocation(MC_Location var1, boolean var2) {
        setSpawnPoint(new BlockPos(var1.getBlockX(), var1.getBlockY(), var1.getBlockZ()), var2);
    }

    @Override
    public void playSound(String var1, float var2, float var3) {
        SoundEvent object = SoundEvent.REGISTRY.getObject(new ResourceLocation(var1));
        if (object == null) {
            System.err.println("Sound " + var1 + " does not exist.");
            return;
        }
        connection.sendPacket(new SPacketSoundEffect(object, SoundCategory.AMBIENT, posX, posY, posZ, var2, var3));
    }

    @Override
    public float getFoodRegenAmount() {
        return naturalHealthRegenAmount;
    }

    @Override
    public void setFoodRegenAmount(float var1) {
        naturalHealthRegenAmount = var1;
    }

    @Override
    public void kick(String var1) {
        connection.disconnect(new TextComponentString(var1));
    }

    @Override
    public SocketAddress getSocketAddress() {
        return connection.getNetworkManager().getRemoteAddress();
    }

    @Override
    public MC_Entity getEntitySpectated() {
        return (MC_Entity) getSpectatingEntity();
    }

    @Override
    public void spectateEntity(MC_Entity var1) {
        setSpectatingEntity((Entity) var1);
    }

    @Override
    public MC_Server getServer() {
        return ServerWrapper.getInstance();
    }

    @Override
    public void onCompassTargetUpdated(MC_Location location) {
        compassTarget = location;
    }

    @Override
    public byte[] serialize() {
        throw new IllegalStateException("Player entities cannot be serialized.");
    }

    @Override
    public void setPlayerListHeaderFooter(String header, String footer) {
        SPacketPlayerListHeaderFooter packet = new SPacketPlayerListHeaderFooter();
        ((IMixinSPacketPlayerListHeaderFooter) packet).setHeader(new TextComponentString(header));
        ((IMixinSPacketPlayerListHeaderFooter) packet).setFooter(new TextComponentString(footer));
        connection.sendPacket(packet);
    }

    @Override
    public void displayInventoryGUI(MC_InventoryGUI gui) {
        ((EntityPlayerMP) (Object) this).displayGUIChest(((GUIInventory) gui));
    }

    @Override
    public void closeInventory() {
        ((EntityPlayerMP) (Object) this).closeScreen();
    }

    @Override
    public void sendJsonMessage(String json) {
        connection.sendPacket(new SPacketChat(TextComponentString.Serializer.jsonToComponent(json)));
    }

    @Override
    public void sendMessage(BaseComponent... msg) {
        sendJsonMessage(ComponentSerializer.toString(msg));
    }

    @Override
    public boolean hasPlayedBefore() {
        _JOT_OnlineTimeEntry entry = _JOT_OnlineTimeUtils.Data.playerData.get(getUUID().toString());
        return entry != null && entry.msTotal > 0;
    }

    @Override
    public long getOnlineTime() {
        _JOT_OnlineTimeEntry entry = _JOT_OnlineTimeUtils.Data.playerData.get(getUUID().toString());
        return entry == null ? 0 : entry.msTotal + System.currentTimeMillis() - entry.msLastLogin;
    }

    public boolean hasCustomName() {
        String customName = _CmdNameColor.ColorNameDict.get(getUUID().toString());

        return customName != null && customName.equalsIgnoreCase(getName());
    }

    public void setCustomName(String newName) {
        String key = getUUID().toString();

        if (newName != null && newName.length() > 0) {
            _CmdNameColor.ColorNameDict.put(key, newName);
        } else {
            _CmdNameColor.ColorNameDict.remove(key);
        }

        _CmdNameColor.updateNameColorOnTab((EntityPlayerMP) (Object) this);
    }

    public String getCustomName() {
        String key = getUUID().toString();
        String customName = _CmdNameColor.ColorNameDict.get(key);

        return customName == null ? getName() : customName;
    }

    @Override
    public MC_Location getHome() {
        _SerializableLocation sloc = _HomeUtils.getHome(getUUID());
        return new MC_Location(sloc.x, sloc.y, sloc.z, sloc.dimension, sloc.yaw, sloc.pitch);
    }

    @Override
    public void setHome(MC_Location location) {
        _HomeUtils.setHome(getUUID(), new _SerializableLocation(
                location.x, location.y, location.z,
                location.dimension, location.yaw, location.pitch
        ));
    }

    @Override
    public MC_Location getHome2() {
        _SerializableLocation sloc = _HomeUtils.getHome2(getUUID());
        return new MC_Location(sloc.x, sloc.y, sloc.z, sloc.dimension, sloc.yaw, sloc.pitch);
    }

    @Override
    public void setHome2(MC_Location location) {
        _HomeUtils.setHome2(getUUID(), new _SerializableLocation(
                location.x, location.y, location.z,
                location.dimension, location.yaw, location.pitch
        ));
    }
}
