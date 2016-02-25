package org.projectrainbow.mixins;

import PluginReference.MC_ContainerType;
import PluginReference.MC_Entity;
import PluginReference.MC_EventInfo;
import PluginReference.MC_GameMode;
import PluginReference.MC_ItemStack;
import PluginReference.MC_Location;
import PluginReference.MC_Player;
import PluginReference.MC_Server;
import PluginReference.MC_World;
import com.google.common.base.Objects;
import com.google.common.io.Files;
import net.minecraft.server.MinecraftServer;
import net.minecraft.src.AnimalChest;
import net.minecraft.src.BlockPos;
import net.minecraft.src.ChatComponentText;
import net.minecraft.src.Container;
import net.minecraft.src.ContainerBeacon;
import net.minecraft.src.ContainerBrewingStand;
import net.minecraft.src.ContainerChest;
import net.minecraft.src.ContainerDispenser;
import net.minecraft.src.ContainerEnchantment;
import net.minecraft.src.ContainerFurnace;
import net.minecraft.src.ContainerHopper;
import net.minecraft.src.ContainerHorseInventory;
import net.minecraft.src.ContainerMerchant;
import net.minecraft.src.ContainerPlayer;
import net.minecraft.src.ContainerRepair;
import net.minecraft.src.ContainerWorkbench;
import net.minecraft.src.DamageSource;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityMinecartContainer;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EntityPlayerMP;
import net.minecraft.src.EnumConnectionState;
import net.minecraft.src.IChatComponent;
import net.minecraft.src.ICommandSender;
import net.minecraft.src.InventoryEnderChest;
import net.minecraft.src.ItemInWorldManager;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.NBTTagList;
import net.minecraft.src.NetHandlerPlayServer;
import net.minecraft.src.OutboundPacketRespawn;
import net.minecraft.src.OutboundPacketSetExperience;
import net.minecraft.src.OutboundPacketSpawnPosition;
import net.minecraft.src.ResourceLocation;
import net.minecraft.src.ServerConfigurationManager;
import net.minecraft.src.TileEntityChest;
import net.minecraft.src.WorldServer;
import net.minecraft.src.WorldSettings;
import net.minecraft.src.fy;
import net.minecraft.src.nf;
import net.minecraft.src.qe;
import net.minecraft.src.qf;
import net.minecraft.src.qg;
import org.projectrainbow.BlockWrapper;
import org.projectrainbow.EmptyItemStack;
import org.projectrainbow.Hooks;
import org.projectrainbow.PluginHelper;
import org.projectrainbow.ServerWrapper;
import org.projectrainbow._Backpack;
import org.projectrainbow._DiwUtils;
import org.projectrainbow._EconomyManager;
import org.projectrainbow._PermMgr;
import org.projectrainbow.interfaces.IMixinEntityPlayerMP;
import org.projectrainbow.interfaces.IMixinNBTBase;
import org.projectrainbow.interfaces.IMixinPlayerCapabilities;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

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
    public NetHandlerPlayServer playerNetServerHandler;

    @Shadow
    @Final
    public ItemInWorldManager theItemInWorldManager;

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

    /*
     * add teleport helper methods
     */
    @Override
    public void teleport(WorldServer world, double x, double y, double z) {
        teleport(world, x, y, z, rotationYaw, rotationPitch);
    }

    @Override
    public void teleport(WorldServer world, double x, double y, double z, float yaw, float pitch) {
        stopRiding();
        for (Entity passenger : getPassengers()) {
            passenger.stopRiding();
        }
        // Close open containers
        if (openContainer != ((EntityPlayerMP) (Object) this).inventoryContainer) {
            ((EntityPlayerMP) (Object) this).closeContainer();
        }
        if (world != worldObj) {
            MinecraftServer mcServer = _DiwUtils.getMinecraftServer();
            final WorldServer fromWorld = (WorldServer) worldObj;
            final WorldServer toWorld = world;
            fromWorld.getEntityTracker().removePlayerFromTrackers((EntityPlayerMP) (Object) this);
            fromWorld.getPlayerManager().removePlayer((EntityPlayerMP) (Object) this);
            mcServer.getConfigurationManager().getPlayers().remove(this);
            fromWorld.getEntityTracker().untrackEntity((EntityPlayerMP) (Object) this);

            fromWorld.removePlayerEntityDangerously((EntityPlayerMP) (Object) this);
            int currentDim = dimension;
            dimension = ((MC_World) toWorld).getDimension();
            setPositionAndRotation(x, y, z, yaw, pitch);

            toWorld.z().loadChunk((int) posX >> 4, (int) posZ >> 4);

            EntityPlayerMP entityplayermp1 = (EntityPlayerMP) (Object) this;

            // Support vanilla clients going into custom dimensions
            int clientDimension = dimension;
            // Force vanilla client to refresh their chunk cache if same dimension
            if (false) {
                entityplayermp1.playerNetServerHandler.sendPacket(
                        new OutboundPacketRespawn((byte) (clientDimension >= 0 ? -1 : 0), toWorld.getDifficulty(), toWorld.getWorldInfo().getTerrainType(),
                                entityplayermp1.theItemInWorldManager.getGameType()));
            }

            entityplayermp1.playerNetServerHandler.sendPacket(
                    new OutboundPacketRespawn(clientDimension, toWorld.getDifficulty(), toWorld.getWorldInfo().getTerrainType(),
                            entityplayermp1.theItemInWorldManager.getGameType()));
            setWorld(toWorld);
            isDead = false;
            entityplayermp1.playerNetServerHandler.setPlayerLocation(entityplayermp1.posX, entityplayermp1.posY, entityplayermp1.posZ,
                    entityplayermp1.rotationYaw, entityplayermp1.rotationPitch);
            entityplayermp1.setSneaking(false);
            entityplayermp1.playerNetServerHandler.sendPacket(new OutboundPacketSpawnPosition(new BlockPos(compassTarget.getBlockX(), compassTarget.getBlockY(), compassTarget.getBlockZ())));
            entityplayermp1.playerNetServerHandler.sendPacket(new OutboundPacketSetExperience(entityplayermp1.experience, entityplayermp1.experienceTotal, entityplayermp1.experienceLevel));
            mcServer.getConfigurationManager().updateTimeAndWeatherForPlayer(entityplayermp1, toWorld);
            toWorld.getPlayerManager().addPlayer(entityplayermp1);
            toWorld.spawnEntityInWorld(entityplayermp1);
            mcServer.getConfigurationManager().getPlayers().add(entityplayermp1);
            entityplayermp1.theItemInWorldManager.setWorld(toWorld);
            entityplayermp1.sendContainerToPlayer(openContainer);
            entityplayermp1.setHealth(entityplayermp1.getHealth());

            fromWorld.m(); // resetUpdateEntityTick
            toWorld.m();
        } else {
            playerNetServerHandler.setPlayerLocation(x, y, z, yaw, pitch);
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
                int nActualItems = 0;
                int nPackSize = this.backpack.getSizeInventory();

                for (int i = 0; i < nPackSize; ++i) {
                    ItemStack is = this.backpack.getStackInSlot(i);
                    if (is != null) {
                        if (is.stackSize <= 0) {
                            System.out.println("Backpack " + this.getName() + " had " + is.stackSize + " of Item ID " + is.getItem() + " -- removing");
                            is.setItemDamage(0);
                            this.backpack.setInventorySlotContents(i, null);
                        }

                        ++nActualItems;
                    }
                }
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

    @Redirect(method = "onDeath", at = @At(value = "INVOKE", target = "net.minecraft.src.ServerConfigurationManager.sendChatMsg(Lnet/minecraft/src/IChatComponent;)V"))
    private void onDeath(ServerConfigurationManager configurationManager, IChatComponent deathMsg, DamageSource damageSource) {
        MC_Player killer = damageSource.getEntity() instanceof MC_Player ? (MC_Player) damageSource.getEntity() : null;
        Hooks.onPlayerDeath(this, killer, PluginHelper.wrap(damageSource), deathMsg.getUnformattedTextForChat());
        configurationManager.sendChatMsg(deathMsg);
    }

    @Inject(method = "c(I)Lnet/minecraft/src/Entity;", at = @At("HEAD"), cancellable = true)
    private void handleChangeDimension(int newDimension, CallbackInfoReturnable<Entity> callbackInfo) {
        MC_EventInfo ei = new MC_EventInfo();
        Hooks.onAttemptPlayerChangeDimension(this, newDimension, ei);
        if (ei.isCancelled) {
            callbackInfo.setReturnValue((Entity) (Object) this);
        }
    }

    @Inject(method = "attackTargetEntityWithCurrentItem", at = @At("HEAD"), cancellable = true)
    private void onAttackEntity(Entity target, CallbackInfo callbackInfo) {
        if (theItemInWorldManager.getGameType() != WorldSettings.GameType.SPECTATOR) {
            MC_EventInfo ei = new MC_EventInfo();
            Hooks.onAttemptAttackEntity(this, (MC_Entity) target, ei);
            if (ei.isCancelled) {
                callbackInfo.cancel();
            }
        }
    }

    @Inject(method = "a(Lnet/minecraft/src/qg;)V", at = @At("HEAD"))
    private void onContainerOpen(qg var1, CallbackInfo callbackInfo) {
        List<MC_ItemStack> items = new ArrayList<MC_ItemStack>();
        for (int i = 0; i < var1.getSizeInventory(); i++) {
            ItemStack stack = var1.getStackInSlot(i);
            items.add(stack == null ? EmptyItemStack.getInstance() : (MC_ItemStack) (Object) stack);
        }
        Hooks.onContainerOpen(this, items, var1.getName());
        for (int i = 0; i < var1.getSizeInventory(); i++) {
            MC_ItemStack stack = items.get(i);
            var1.setInventorySlotContents(i, stack == null || stack.getCount() == 0 ? null : (ItemStack) (Object) stack);
        }
    }

    @Inject(method = "closeContainer", at = @At("HEAD"))
    private void onContainerClosed(CallbackInfo callbackInfo) {
        MC_ContainerType containerType = MC_ContainerType.UNSPECIFIED;

        try {
            if (this.openContainer instanceof ContainerFurnace) {
                containerType = MC_ContainerType.FURNACE;
            } else if (this.openContainer instanceof ContainerHopper) {
                containerType = MC_ContainerType.HOPPER;
                // todo check whether is minecart
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
                if (chest.e() != null) {
                    if (chest.e() instanceof EntityMinecartContainer) {
                        containerType = MC_ContainerType.MINECART_CHEST;
                    } else if (chest.e() instanceof qe) {
                        containerType = MC_ContainerType.CHEST_DOUBLE;
                    } else if (chest.e() instanceof InventoryEnderChest) {
                        containerType = MC_ContainerType.CHEST_ENDER;
                    } else if (chest.e() instanceof AnimalChest) {
                        containerType = MC_ContainerType.CHEST_HORSE;
                    } else if (chest.e() instanceof _Backpack) {
                        containerType = MC_ContainerType.BACKPACK;
                    } else if (chest.e() instanceof TileEntityChest) {
                        TileEntityChest te = (TileEntityChest) chest.e();
                        int subType = te.getBlockMetadata();

                        containerType = MC_ContainerType.CHEST_SINGLE;
                        if (subType == 1) {
                            containerType = MC_ContainerType.CHEST_TRAPPED;
                        }
                    }
                }
            } else if (this.openContainer instanceof ContainerDispenser) {
                containerType = MC_ContainerType.DISPENSER;
                // todo check for dropper
            } else if (this.openContainer instanceof ContainerHorseInventory) {
                containerType = MC_ContainerType.CHEST_HORSE;
            }
        } catch (Exception var7) {
            var7.printStackTrace();
        }

        Hooks.onContainerClosed(this, containerType);
    }

    @Inject(method = "trySleep", at = @At(value = "INVOKE", target = "net.minecraft.src.EntityPlayerMP.getServerForPlayer()Lnet/minecraft/src/WorldServer;"))
    private void startSleeping(BlockPos bed, CallbackInfoReturnable callbackInfo) {
        Hooks.onPlayerBedEnter(this, new BlockWrapper(worldObj.getBlockState(bed)), new MC_Location(bed.getX(), bed.getY(), bed.getZ(), dimension));
    }

    @Inject(method = "wakeUpPlayer", at = @At(value = "INVOKE", target = "net.minecraft.src.EntityPlayerMP.getServerForPlayer()Lnet/minecraft/src/WorldServer;"))
    private void finishSleeping(boolean a, boolean b, boolean c, CallbackInfo callbackInfo) {
        Hooks.onPlayerBedLeave(this, new BlockWrapper(worldObj.getBlockState(playerLocation)), new MC_Location(playerLocation.getX(), playerLocation.getY(), playerLocation.getZ(), dimension));
    }

    @Inject(method = "setSpectatingEntity", at = @At("HEAD"), cancellable = true)
    private void onSetSpectatingEntity(Entity var1, CallbackInfo callbackInfo) {
        MC_EventInfo ei = new MC_EventInfo();
        Hooks.onAttemptSpectateEntity(this, (MC_Entity) var1, ei);
        if (ei.isCancelled) {
            callbackInfo.cancel();
        }
    }

    @Override
    public void clonePlayer(EntityPlayer entityPlayer, boolean b) {
        this.backpack = ((IMixinEntityPlayerMP) entityPlayer).getBackpack();
        super.clonePlayer(entityPlayer, b);
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
        return playerNetServerHandler.getNetworkManager().getRemoteAddress().toString();
    }

    @Override
    public void sendMessage(String var1) {
        playerNetServerHandler.sendPacket(new fy(new ChatComponentText(var1)));
    }

    @Override
    public void teleport(MC_Location var1) {
        teleport(_DiwUtils.getMinecraftServer().worldServerForDimension(var1.dimension), var1.x, var1.y, var1.z, var1.yaw, var1.pitch);
    }

    @Override
    public void executeCommand(String var1) {
        _DiwUtils.getMinecraftServer().getCommandManager().executeCommand((ICommandSender) this, var1);
    }

    @Override
    public boolean isOp() {
        return _DiwUtils.getMinecraftServer().getConfigurationManager().canSendCommands(getGameProfile());
    }

    @Override
    public MC_GameMode getGameMode() {
        return PluginHelper.gamemodeMap.get(theItemInWorldManager.getGameType());
    }

    @Override
    public void setGameMode(MC_GameMode var1) {
        theItemInWorldManager.setGameType(PluginHelper.gamemodeMap.inverse().get(var1));
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
        return _EconomyManager.GetBalance(getName());
    }

    @Override
    public void setEconomyBalance(double var1) {
        _EconomyManager.SetBalance(getName(), var1);

    }

    @Override
    public MC_ItemStack getItemInHand() {
        return Objects.firstNonNull((MC_ItemStack) (Object) inventory.getCurrentItem(), EmptyItemStack.getInstance());
    }

    @Override
    public void setItemInHand(MC_ItemStack var1) {
        inventory.mainInventory[inventory.currentItem] = (ItemStack) (Object) (var1 instanceof EmptyItemStack ? null : var1);
    }

    @Override
    public MC_ItemStack getItemInOffHand() {
        return Objects.firstNonNull((MC_ItemStack) (Object) inventory.c[0], EmptyItemStack.getInstance());
    }

    @Override
    public void setItemInOffHand(MC_ItemStack item) {
        inventory.c[0] = item instanceof EmptyItemStack ? null : (ItemStack) (Object) item;
    }

    @Override
    public List<MC_ItemStack> getInventory() {
        return PluginHelper.invArrayToList(inventory.mainInventory);
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
        return isEntityInvulnerable(DamageSource.magic);
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
        if (isOp()) {
            return true;
        } else {
            boolean res = _PermMgr.hasPermission(getName(), perm, true);

            if (!res) {
                res = _PermMgr.hasPermission(getUUID().toString(), perm,
                        false);
            }

            return res;
        }
    }

    @Override
    public void setCompassTarget(MC_Location var1) {
        OutboundPacketSpawnPosition packet = new OutboundPacketSpawnPosition(new BlockPos(var1.getBlockX(), var1.getBlockY(), var1.getBlockZ()));
        playerNetServerHandler.sendPacket(packet);
        compassTarget = var1;
    }

    @Override
    public MC_Location getCompassTarget() {
        return Objects.firstNonNull(compassTarget, getBedRespawnLocation());
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
        nf object = nf.a.getObject(new ResourceLocation(var1));
        if (object == null) {
            throw new IllegalArgumentException("Sound " + var1 + " does not exist.");
        }
        ((Entity) (Object) this).a(object, var2, var3);
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
        playerNetServerHandler.kickPlayerFromServer(var1);
    }

    @Override
    public SocketAddress getSocketAddress() {
        return playerNetServerHandler.getNetworkManager().getRemoteAddress();
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
}
