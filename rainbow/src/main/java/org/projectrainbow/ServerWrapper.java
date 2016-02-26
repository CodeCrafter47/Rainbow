package org.projectrainbow;


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
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.ServerCommandManager;
import org.projectrainbow.commands._CmdPerm;
import org.projectrainbow.interfaces.IMixinMinecraftServer;
import org.projectrainbow.interfaces.IMixinNBTBase;
import org.projectrainbow.plugins.PluginCommand;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.util.ArrayList;
import java.util.Collections;
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
        return (List<MC_Player>) (Object) new ArrayList<net.minecraft.src.EntityPlayerMP>(_DiwUtils.getMinecraftServer().getConfigurationManager().getPlayers());
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
                for (Object oPlayer : _DiwUtils.getMinecraftServer().getConfigurationManager().getPlayers()) {
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
        Iterator var4 = _DiwUtils.getMinecraftServer().getConfigurationManager().getPlayers().iterator();

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
        // todo suggest removal
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
        // todo suggest removal
    }

    public MC_Block getBlockFromName(String blockName) {
        return PluginHelper.getBlockFromName(blockName);
    }

    public void log(String msg) {
        _DiwUtils.getMinecraftServer().logInfo(msg);
    }

    public boolean unregisterWorld(String worldName) {
        // todo suggest removal
        return false;
    }

    public int registerWorld(String worldName, MC_WorldSettings settings) {
        // todo suggest removal
        return 0;
    }

    public List<MC_World> getWorlds() {
        return (List<MC_World>) (Object) Lists.newArrayList(_DiwUtils.getMinecraftServer().worldServers);
    }
}
