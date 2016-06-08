package org.projectrainbow;


import PluginReference.MC_AttributeModifier;
import PluginReference.MC_Block;
import PluginReference.MC_Command;
import PluginReference.MC_CommandSenderInfo;
import PluginReference.MC_ItemStack;
import PluginReference.MC_Player;
import PluginReference.MC_PlayerPacketListener;
import PluginReference.MC_Server;
import PluginReference.MC_ServerPacketListener;
import PluginReference.MC_World;
import PluginReference.MC_WorldSettings;
import PluginReference.PluginInfo;
import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.command.ServerCommandManager;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.nbt.NBTTagCompound;
import org.projectrainbow.commands._CmdPerm;
import org.projectrainbow.interfaces.IMixinMinecraftServer;
import org.projectrainbow.interfaces.IMixinNBTBase;
import org.projectrainbow.plugins.PluginCommand;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;


public class ServerWrapper implements MC_Server {
    private static ServerWrapper instance = new ServerWrapper();

    public static String serverIconFileName = "server-icon.png";
    public static Deque<MC_CommandSenderInfo> commandSenderInfo = new LinkedList<MC_CommandSenderInfo>();

    public static ServerWrapper getInstance() {
        return instance;
    }

    private ServerWrapper() {
    }

    public void broadcastMessage(String msg) {
        _DiwUtils.MessageAllPlayers(msg);
    }

    public List<MC_Player> getPlayers() {
        return (List<MC_Player>) (Object) new ArrayList<EntityPlayerMP>(_DiwUtils.getMinecraftServer().getPlayerList().getPlayerList());
    }

    public void executeCommand(String cmd) {
        try {
            _DiwUtils.getMinecraftServer().getCommandManager().executeCommand(_DiwUtils.getMinecraftServer(), cmd);
        } catch (Exception var3) {
            ;
        }
    }

    public List<PluginInfo> getPlugins() {
        return _DiwUtils.pluginManager.plugins;
    }

    public List<MC_Player> getOfflinePlayers() {
        ArrayList list = new ArrayList();
        Iterator var3 = _JOT_OnlineTimeUtils.Data.playerData.keySet().iterator();

        while (var3.hasNext()) {
            String name = (String) var3.next();

            list.add(new OfflinePlayerWrapper(name));
        }

        return list;
    }

    public void setCustomShutdownMessage(String arg0) {
        _DiwUtils.CustomShutdownMessage = arg0;
    }

    public void givePermission(String key, String perm) {
        UUID uuid = null;
        if (!key.equals("*")) {
            try {
                uuid = UUID.fromString(key);
            } catch (Throwable th) {
                uuid = _UUIDMapper.getUUID(key);
            }
            if (uuid == null) {
                return;
            }
        }
        _PermMgr.givePermission(uuid, perm);
    }

    public List<String> getPermissions(String key) {
        UUID uuid = null;
        if (!key.equals("*")) {
            try {
                uuid = UUID.fromString(key);
            } catch (Throwable th) {
                uuid = _UUIDMapper.getUUID(key);
            }
            if (uuid == null) {
                return Collections.emptyList();
            }
        }
        return _PermMgr.getPermissions(uuid);
    }

    public void takePermission(String key, String perm) {
        UUID uuid = null;
        if (!key.equals("*")) {
            try {
                uuid = UUID.fromString(key);
            } catch (Throwable th) {
                uuid = _UUIDMapper.getUUID(key);
            }
            if (uuid == null) {
                return;
            }
        }
        _PermMgr.takePermission(uuid, perm);
    }

    public boolean hasPermission(String key, String perm) {
        UUID uuid = null;
        if (!key.equals("*")) {
            try {
                uuid = UUID.fromString(key);
            } catch (Throwable th) {
                uuid = _UUIDMapper.getUUID(key);
            }
            if (uuid == null) {
                return false;
            }
        }
        boolean res = _PermMgr.hasPermission(uuid, perm);

        if (_CmdPerm.DebugMode) {
            System.out.println(String.format("=== Perm Debug: Testing if '%s'(%s) has permission '%s'. Result: %s", key, String.valueOf(uuid), perm, res));
        }

        return res;
    }

    public MC_World getWorld(int idxDimension) {
        return (MC_World) _DiwUtils.getMinecraftServer().worldServerForDimension(idxDimension);
    }

    public int getSpawnProtectionRadius() {
        return _DiwUtils.getMinecraftServer().getSpawnProtectionSize();
    }

    public int getServerPort() {
        return _DiwUtils.getMinecraftServer().getServerPort();
    }

    public int getReconnectDelaySeconds() {
        return _DiwUtils.ReconnectDelaySeconds;
    }

    public void setReconnectDelaySeconds(int secs) {
        _DiwUtils.ReconnectDelaySeconds = secs;
    }

    public MC_ItemStack createItemStack(int id, int count, int dmg) {
        Item item = Item.getItemById(id);
        return (MC_ItemStack) (Object) new ItemStack(item, count, dmg);
    }

    public String getPlayerExactName(String plrName) {
        return plrName == null
                ? null
                : _JOT_OnlineTimeUtils.GetPlayerExactName(plrName);
    }

    public String getPlayerUUIDFromName(String pName) {
        if (pName == null) return null;
        UUID uuid = _UUIDMapper.getUUID(pName);
        return uuid == null ? null : uuid.toString();
    }

    public List<String> getPlayerNamesFromUUID(String uid) {
        return _UUIDMapper.getNameHistory(UUID.fromString(uid));
    }

    public String getLastKnownPlayerNameFromUUID(String uid) {
        return _UUIDMapper.getName(UUID.fromString(uid));
    }

    public int getMaxBuildHeight() {
        return _DiwUtils.getMinecraftServer().getBuildLimit();
    }

    public MC_Player getOnlinePlayerByName(String pName) {
        if (pName == null) {
            return null;
        } else {
            pName = pName.trim();
            if (pName.length() <= 0) {
                return null;
            } else {
                for (Object oPlayer : _DiwUtils.getMinecraftServer().getPlayerList().getPlayerList()) {
                    MC_Player player = (MC_Player) oPlayer;

                    if (pName.equalsIgnoreCase(player.getName())) {
                        return player;
                    }
                }

                return null;
            }
        }
    }

    public void registerCommand(MC_Command argCmd) {
        ((ServerCommandManager) _DiwUtils.getMinecraftServer().getCommandManager()).registerCommand(new PluginCommand(argCmd));
    }

    public List<String> getMatchingOnlinePlayerNames(String arg) {
        ArrayList matches = new ArrayList();

        if (arg == null) {
            arg = "";
        }

        arg = arg.toLowerCase().trim();
        Iterator var4 = _DiwUtils.getMinecraftServer().getPlayerList().getPlayerList().iterator();

        while (var4.hasNext()) {
            Object oPlayer = var4.next();
            EntityPlayer player = (EntityPlayer) oPlayer;
            String pName = player.getName().toLowerCase();

            if (pName.contains(arg)) {
                matches.add(player.getName());
            }
        }

        return matches;
    }

    public boolean getOnlineMode() {
        return _DiwUtils.getMinecraftServer().isServerInOnlineMode();
    }

    public double getRainbowVersion() {
        return _DiwUtils.DiwModVersionNumeric;
    }

    public void clearAllPermissions() {
        _PermMgr.clearAllPermissions();
    }

    public String getServerIconFilename() {
        return serverIconFileName;
    }

    public String getServerMOTD() {
        return _DiwUtils.getMinecraftServer().getMOTD();
    }

    public void setServerIconFilename(String newFilename) {
        serverIconFileName = newFilename;
        ((IMixinMinecraftServer) _DiwUtils.getMinecraftServer()).onServerIconUpdated();
    }

    public void setServerMOTD(String newMOTD) {
        _DiwUtils.getMinecraftServer().setMOTD(newMOTD);
        ((IMixinMinecraftServer) _DiwUtils.getMinecraftServer()).onMotdUpdated();
    }

    public void registerPlayerPacketListener(MC_PlayerPacketListener argListener) {
        System.err.println("packet listeners are no longer supported.");
    }

    public MC_ItemStack createItemStack(byte[] rawData) {
        try {
            if (rawData.length == 0) {
                return EmptyItemStack.getInstance();
            }
            NBTTagCompound exc = new NBTTagCompound();
            ByteArrayInputStream bis = new ByteArrayInputStream(rawData);
            DataInputStream dis = new DataInputStream(bis);

            ((IMixinNBTBase) exc).read1(dis);
            bis.close();
            ItemStack is = ItemStack.loadItemStackFromNBT(exc);

            return (MC_ItemStack) (Object) is;
        } catch (Exception var6) {
            var6.printStackTrace();
            return EmptyItemStack.getInstance();
        }
    }

    public MC_CommandSenderInfo getExecutingCommandInfo() {
        return commandSenderInfo.getLast();
    }

    public void registerServerPacketListener(MC_ServerPacketListener argListener) {
        System.err.println("packet listeners are no longer supported.");
    }

    public MC_Block getBlockFromName(String blockName) {
        return PluginHelper.getBlockFromName(blockName);
    }

    public void log(String msg) {
        _DiwUtils.getMinecraftServer().logInfo(msg);
    }

    public boolean unregisterWorld(String worldName) {
        System.err.println("built-in multiworld support has been removed. Use a plugin.");
        return false;
    }

    public int registerWorld(String worldName, MC_WorldSettings settings) {
        System.err.println("built-in multiworld support has been removed. Use a plugin.");
        return 0;
    }

    public List<MC_World> getWorlds() {
        return (List<MC_World>) (Object) Lists.newArrayList(_DiwUtils.getMinecraftServer().worldServers);
    }

    @Override
    public PluginInfo getPluginInfo(String name) {
        PluginInfo info = null;
        for (PluginInfo pluginInfo : getPlugins()) {
            if (pluginInfo.name.equals(name)) {
                info = pluginInfo;
            }
        }
        return info;
    }

    @Override
    public MC_Block getBlock(int id) {
        return new BlockWrapper(Block.getBlockById(id).getDefaultState());
    }

    @Override
    public MC_Block getBlock(int id, int subtype) {
        return new BlockWrapper(Block.getBlockById(id).getStateFromMeta(subtype));
    }

    @Override
    public void addRecipe(MC_ItemStack result, Object... ingredients) {
        CraftingManager.getInstance().addRecipe((ItemStack) (Object) result, ingredients);
        // sort recipes
        Collections.sort(CraftingManager.getInstance().getRecipeList(), new Comparator<IRecipe>() {
            public int compare(IRecipe var1, IRecipe var2) {
                return var1 instanceof ShapelessRecipes && var2 instanceof ShapedRecipes ?1:(var2 instanceof ShapelessRecipes && var1 instanceof ShapedRecipes?-1:(var2.getRecipeSize() < var1.getRecipeSize()?-1:(var2.getRecipeSize() > var1.getRecipeSize()?1:0)));
            }
        });
    }

    @Override
    public void addShapelessRecipe(MC_ItemStack result, MC_ItemStack... ingredients) {
        CraftingManager.getInstance().addShapelessRecipe((ItemStack) (Object) result, (Object[]) ingredients);
        // sort recipes
        Collections.sort(CraftingManager.getInstance().getRecipeList(), new Comparator<IRecipe>() {
            public int compare(IRecipe var1, IRecipe var2) {
                return var1 instanceof ShapelessRecipes && var2 instanceof ShapedRecipes ?1:(var2 instanceof ShapelessRecipes && var1 instanceof ShapedRecipes?-1:(var2.getRecipeSize() < var1.getRecipeSize()?-1:(var2.getRecipeSize() > var1.getRecipeSize()?1:0)));
            }
        });
    }

    /**
     * Factory method to create an attribute modifier with a random unique uuid.
     *
     * @param name     the name of the attribute modifier
     * @param operator the operator to use
     * @param value    the value of the modifier
     * @return the created attribute modifier
     */
    @Override
    public MC_AttributeModifier createAttributeModifier(String name, MC_AttributeModifier.Operator operator, double value) {
        return createAttributeModifier(UUID.randomUUID(), name, operator, value);
    }

    /**
     * Factory method to create an attribute modifier using a given uuid.
     *
     * @param uuid     the uuid to use
     * @param name     the name of the attribute modifier
     * @param operator the operator to use
     * @param value    the value of the modifier
     * @return the created attribute modifier
     */
    @Override
    public MC_AttributeModifier createAttributeModifier(UUID uuid, String name, MC_AttributeModifier.Operator operator, double value) {
        return (MC_AttributeModifier) new AttributeModifier(uuid, name, value, PluginHelper.operatorMap.get(operator));
    }
}
