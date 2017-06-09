package org.projectrainbow.commands;

import PluginReference.MC_Command;
import PluginReference.MC_Player;
import com.google.common.io.Files;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.SPacketPlayerListItem;
import org.projectrainbow._ColorHelper;
import org.projectrainbow._DiwUtils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class _CmdNameColor implements MC_Command {
    public static ConcurrentHashMap<String, String> ColorNameDict;
    public static String m_DataFilename;

    static {
        _CmdNameColor.ColorNameDict = new ConcurrentHashMap<>();
        _CmdNameColor.m_DataFilename = "NameColors.dat";
    }

    @Override
    public boolean hasPermissionToUse(MC_Player player) {
        return player == null || player.hasPermission("rainbow.namecolor");
    }

    @Override
    public List<String> getTabCompletionList(MC_Player plr, String[] args) {
        return Collections.emptyList();
    }

    @Override
    public String getCommandName() {
        return "namecolor";
    }

    @Override
    public List<String> getAliases() {
        return Collections.emptyList();
    }

    @Override
    public String getHelpLine(MC_Player player) {
        return String.valueOf(_ColorHelper.AQUA) + "/namecolor" + _ColorHelper.WHITE + " --- Change your name color";
    }

    public void SendUsage(MC_Player p) {
        final String pName = p.getName();
        final int mid = pName.length() / 2;
        final String left = pName.substring(0, mid);
        final String right = pName.substring(mid);
        p.sendMessage(String.valueOf(_ColorHelper.RED) + "Try Example: " + _ColorHelper.DARK_AQUA + "/namecolor " + _ColorHelper.WHITE + "&b" + left + "&d" + right);
        p.sendMessage(String.valueOf(_ColorHelper.RED) + "That would make your name: " + _ColorHelper.AQUA + left + _ColorHelper.LIGHT_PURPLE + right);
    }

    public static void SaveData() {
        try {
            final long msStart = System.currentTimeMillis();
            final File file = new File(String.valueOf(_DiwUtils.RainbowDataDirectory) + _CmdNameColor.m_DataFilename);
            final FileOutputStream f = new FileOutputStream(file);
            final ObjectOutputStream s = new ObjectOutputStream(new BufferedOutputStream(f));
            s.writeObject(_CmdNameColor.ColorNameDict);
            s.close();
            final long msEnd = System.currentTimeMillis();
            final String msg = String.valueOf(_ColorHelper.YELLOW) + String.format("%-20s: %5d players.     Took %3d ms", "Name Color Save", _CmdNameColor.ColorNameDict.size(), msEnd - msStart);
            _DiwUtils.ConsoleMsg(msg);
        } catch (Throwable exc) {
            System.out.println("**********************************************");
            System.out.println("Saving NameColors: " + exc.toString());
            System.out.println("**********************************************");
        }
    }

    public static void LoadData() {
        try {
            final long msStart = System.currentTimeMillis();
            final File file = new File(String.valueOf(_DiwUtils.RainbowDataDirectory) + _CmdNameColor.m_DataFilename);
            final File oldFile = new File(_CmdNameColor.m_DataFilename);
            if (oldFile.exists()) {
                Files.move(oldFile, file);
            }
            final FileInputStream f = new FileInputStream(file);
            final ObjectInputStream s = new ObjectInputStream(new BufferedInputStream(f));
            _CmdNameColor.ColorNameDict = (ConcurrentHashMap<String, String>) s.readObject();
            s.close();
            final long msEnd = System.currentTimeMillis();
            _DiwUtils.ConsoleMsg(String.valueOf(_ColorHelper.YELLOW) + String.format("%-20s: %5d players.  Took %3d ms", "Name Color Load", _CmdNameColor.ColorNameDict.size(), msEnd - msStart));
        } catch (Throwable exc) {
            _DiwUtils.ConsoleMsg("Loading NameColors: Starting new file: " + _CmdNameColor.m_DataFilename);
            _CmdNameColor.ColorNameDict = new ConcurrentHashMap<>();
        }
    }

    public static void updateNameColorOnTab(EntityPlayerMP player) {
        if (_DiwUtils.UpdateNameColorOnTab) {
            SPacketPlayerListItem packet = new SPacketPlayerListItem(SPacketPlayerListItem.Action.UPDATE_DISPLAY_NAME, player);
            _DiwUtils.getMinecraftServer().getPlayerList().sendPacketToAllPlayers(packet);
        }
    }

    @Override
    public void handleCommand(MC_Player player, String[] args) {
        if (player == null) {
            System.out.println("--- Only for players!");
            return;
        }
        if (args.length <= 0) {
            this.SendUsage(player);
            return;
        }
        final String pName = player.getName();
        final boolean isOp = player.isOp();
        String newName = args[0];
        if (isOp) {
            newName = _DiwUtils.ConcatArgs(args, 0);
            newName = _DiwUtils.FullTranslate(newName);
        } else {
            newName = _DiwUtils.TranslateChatString(newName, false);
            if (!pName.equalsIgnoreCase(_ColorHelper.stripColor(newName))) {
                player.sendMessage(String.valueOf(_ColorHelper.RED) + "Name must match your own. You can just add color!");
                this.SendUsage(player);
                return;
            }
        }
        final String key = player.getUUID().toString();
        String existing = _CmdNameColor.ColorNameDict.get(key);
        if (existing == null) existing = _CmdNameColor.ColorNameDict.get(pName.toLowerCase());
        if (existing != null && newName.equalsIgnoreCase("off")) {
            _CmdNameColor.ColorNameDict.remove(key);
            player.sendMessage(String.valueOf(_ColorHelper.GREEN) + "You remove your colored name: " + _ColorHelper.YELLOW + existing);
        } else {
            _CmdNameColor.ColorNameDict.put(key, newName);
            player.sendMessage(String.valueOf(_ColorHelper.GREEN) + "Your colored name is now: " + _ColorHelper.YELLOW + newName);
        }
        updateNameColorOnTab((EntityPlayerMP) player);
    }
}
