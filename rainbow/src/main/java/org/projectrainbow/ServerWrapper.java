package org.projectrainbow;


import PluginReference.MC_AttributeModifier;
import PluginReference.MC_Block;
import PluginReference.MC_Command;
import PluginReference.MC_CommandSenderInfo;
import PluginReference.MC_InventoryGUI;
import PluginReference.MC_ItemStack;
import PluginReference.MC_Player;
import PluginReference.MC_PlayerPacketListener;
import PluginReference.MC_Server;
import PluginReference.MC_ServerPacketListener;
import PluginReference.MC_World;
import PluginReference.MC_WorldSettings;
import PluginReference.PluginInfo;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.block.Block;
import net.minecraft.command.ICommand;
import net.minecraft.command.ServerCommandManager;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import org.projectrainbow.commands._CmdPerm;
import org.projectrainbow.interfaces.IMixinMinecraftServer;
import org.projectrainbow.interfaces.IMixinNBTBase;
import org.projectrainbow.plugins.PluginCommand;
import org.projectrainbow.util.WrappedMinecraftCommand;

import javax.annotation.Nullable;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;


public class ServerWrapper implements MC_Server {
    private static ServerWrapper instance = new ServerWrapper();

    public static String serverIconFileName = "server-icon.png";
    public static Deque<MC_CommandSenderInfo> commandSenderInfo = new LinkedList<>();

    public static ServerWrapper getInstance() {
        return instance;
    }

    private ServerWrapper() {
    }

    public void broadcastMessage(String msg) {
        _DiwUtils.MessageAllPlayers(msg);
    }

    public List<MC_Player> getPlayers() {
        return (List<MC_Player>) (Object) new ArrayList<>(_DiwUtils.getMinecraftServer().getPlayerList().getPlayers());
    }

    public void executeCommand(String cmd) {
        try {
            _DiwUtils.getMinecraftServer().getCommandManager().executeCommand(_DiwUtils.getMinecraftServer(), cmd);
        } catch (Exception ignored) {
        }
    }

    public List<PluginInfo> getPlugins() {
        return _DiwUtils.pluginManager.plugins;
    }

    public List<MC_Player> getOfflinePlayers() {
        ArrayList<MC_Player> list = new ArrayList<>();
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
        return (MC_World) _DiwUtils.getMinecraftServer().getWorld(idxDimension);
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
        if (item == null) {
            throw new IllegalArgumentException("An item with id " + id + " does not exist.");
        }
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
                return (MC_Player) _DiwUtils.getMinecraftServer().getPlayerList().getPlayerByUsername(pName);
            }
        }
    }

    @Override
    public MC_Player getOnlinePlayerByUUID(UUID uuid) {
        if (uuid == null) {
            return null;
        } else {
            return (MC_Player) _DiwUtils.getMinecraftServer().getPlayerList().getPlayerByUUID(uuid);
        }
    }

    public void registerCommand(MC_Command argCmd) {
        ((ServerCommandManager) _DiwUtils.getMinecraftServer().getCommandManager()).registerCommand(new PluginCommand(argCmd));
    }

    @Override
    public Map<String, MC_Command> getCommandMap() {
        return Maps.transformValues(_DiwUtils.getMinecraftServer().getCommandManager().getCommands(), new Function<ICommand, MC_Command>() {
            @Nullable
            @Override
            public MC_Command apply(@Nullable ICommand iCommand) {
                return iCommand instanceof PluginCommand ? ((PluginCommand) iCommand).delegate : new WrappedMinecraftCommand(iCommand);
            }
        });
    }

    public List<String> getMatchingOnlinePlayerNames(String arg) {
        ArrayList<String> matches = new ArrayList<>();

        if (arg == null) {
            arg = "";
        }

        arg = arg.toLowerCase().trim();
        Iterator var4 = _DiwUtils.getMinecraftServer().getPlayerList().getPlayers().iterator();

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
                return (MC_ItemStack) (Object) ItemStack.EMPTY;
            }
            NBTTagCompound exc = new NBTTagCompound();
            ByteArrayInputStream bis = new ByteArrayInputStream(rawData);
            DataInputStream dis = new DataInputStream(bis);

            ((IMixinNBTBase) exc).read1(dis);
            bis.close();
            ItemStack is = new ItemStack(exc);

            return (MC_ItemStack) (Object) is;
        } catch (Exception var6) {
            var6.printStackTrace();
            return (MC_ItemStack) (Object) ItemStack.EMPTY;
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
        _DiwUtils.getMinecraftServer().e(msg); // logInfo
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
        return (List<MC_World>) (Object) Lists.newArrayList(_DiwUtils.getMinecraftServer().worlds);
    }

    @Override
    public PluginInfo getPluginInfo(String name) {
        for (PluginInfo pluginInfo : getPlugins()) {
            if (pluginInfo.name.equals(name)) {
                return pluginInfo;
            }
        }
        return null;
    }

    @Override
    public MC_Block getBlock(int id) {
        return new BlockWrapper(Block.getBlockById(id).getDefaultState());
    }

    @Override
    public MC_Block getBlock(int id, int subtype) {
        return new BlockWrapper(Block.getBlockById(id).getStateFromMeta(subtype));
    }

    private int nextRecipeId = 0;

    @Override
    public void addRecipe(MC_ItemStack result, Object... ingredients) {

        List<String> pattern = new ArrayList<>();
        Map<Character, MC_ItemStack> symbols = new HashMap<>();
        symbols.put(' ', (MC_ItemStack) (Object) ItemStack.EMPTY);

        int i;
        for (i = 0; i < ingredients.length && ingredients[i] instanceof String; i++) {
            pattern.add((String) ingredients[i]);
        }

        if (pattern.size() == 0) {
            throw new IllegalArgumentException("Pattern empty");
        }

        if (pattern.size() > 3) {
            throw new IllegalArgumentException("Pattern too big");
        }

        int width = pattern.get(0).length();
        int height = pattern.size();

        if (width > 3) {
            throw new IllegalArgumentException("Pattern too big");
        }

        for (int j = 1; j < height; j++) {
            if (pattern.get(j).length() != width) {
                throw new IllegalArgumentException("Pattern width inconsistent");
            }
        }

        for (; i + 1 < ingredients.length && ingredients[i] instanceof Character && ingredients[i + 1] instanceof MC_ItemStack; i += 2) {
            symbols.put((Character) ingredients[i], (MC_ItemStack) ingredients[i + 1]);
        }

        if (i < ingredients.length) {
            throw new IllegalArgumentException("Arguments of wrong Type");
        }

        NonNullList<Ingredient> list = NonNullList.create();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                char symbol = pattern.get(y).charAt(x);
                MC_ItemStack itemStack = symbols.get(symbol);
                if (itemStack == null) {
                    throw new IllegalArgumentException("Undefined symbol in pattern: '" + symbol + "'");
                }
                list.add(Ingredient.fromStacks(PluginHelper.getItemStack(itemStack)));
            }
        }

        ShapedRecipes recipe = new ShapedRecipes("rainbow", width, height, list, PluginHelper.getItemStack(result));
        CraftingManager.register("rainbow:custom-" + Integer.toHexString(nextRecipeId++), recipe); // addRecipe
    }

    @Override
    public void addShapelessRecipe(MC_ItemStack result, MC_ItemStack... ingredients) {

        NonNullList<Ingredient> list = NonNullList.create();
        for (MC_ItemStack ingredient : ingredients) {
            list.add(Ingredient.fromStacks(PluginHelper.getItemStack(ingredient)));
        }


        ShapelessRecipes recipe = new ShapelessRecipes("rainbow", PluginHelper.getItemStack(result), list);
        CraftingManager.register("rainbow:custom-" + Integer.toHexString(nextRecipeId++), recipe); // addRecipe
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

    @Override
    public MC_InventoryGUI createInventoryGUI(int size, String title) {
        if (size < 0) {
            throw new IllegalArgumentException("size is negative");
        }
        if (size % 9 != 0) {
            throw new IllegalArgumentException("size must be a multiple of 9");
        }
        if (title == null) {
            throw new IllegalArgumentException("title is null");
        }
        return new GUIInventory(title, size);
    }

    @Override
    public MC_Player getOfflinePlayerByName(String name) {
        MC_Player onlinePlayer = getOnlinePlayerByName(name);
        if (onlinePlayer != null) {
            return onlinePlayer;
        }
        return _UUIDMapper.getUUID(name) != null ? new OfflinePlayerWrapper(name) : null;
    }

    @Override
    public MC_Player getOfflinePlayerByUUID(UUID uuid) {
        MC_Player onlinePlayer = getOnlinePlayerByUUID(uuid);
        if (onlinePlayer != null) {
            return onlinePlayer;
        }
        String name = _UUIDMapper.getName(uuid);
        return name != null ? new OfflinePlayerWrapper(name) : null;
    }

    @Override
    public int getMaxPlayerCount() {
        return _DiwUtils.getMinecraftServer().getPlayerList().getMaxPlayers();
    }

    @Override
    public String getMinecraftVersion() {
        return _DiwUtils.getMinecraftServer().G(); // getMinecraftVersion
    }
}
