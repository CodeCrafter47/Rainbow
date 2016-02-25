package org.projectrainbow.commands;

import PluginReference.MC_Player;
import com.google.common.io.Files;
import net.minecraft.server.MinecraftServer;
import net.minecraft.src.CommandBase;
import net.minecraft.src.CommandException;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ICommandSender;
import org.projectrainbow._ColorHelper;
import org.projectrainbow._DiwUtils;
import org.projectrainbow.interfaces.IMixinICommandSender;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.concurrent.ConcurrentHashMap;

public class _CmdNameColor extends CommandBase{
    public static ConcurrentHashMap<String, String> ColorNameDict;
    public static String m_DataFilename;

    static {
        _CmdNameColor.ColorNameDict = new ConcurrentHashMap<String, String>();
        _CmdNameColor.m_DataFilename = "NameColors.dat";
    }

    @Override
    public boolean canCommandSenderUseCommand(MinecraftServer minecraftServer, ICommandSender iCommandSender) {
        return (!(iCommandSender instanceof MC_Player)) || ((MC_Player) iCommandSender).hasPermission("rainbow.namecolor");
    }

    @Override
    public String getCommandName() {
        return "namecolor";
    }

    @Override
    public String getCommandUsage(final ICommandSender vParam) {
        return String.valueOf(_ColorHelper.AQUA) + "/namecolor" + _ColorHelper.WHITE + " --- Change your name color";
    }

    public void SendUsage(final EntityPlayer p) {
        final String pName = p.getName();
        final int mid = pName.length() / 2;
        final String left = pName.substring(0, mid);
        final String right = pName.substring(mid);
        ((IMixinICommandSender)p).sendMessage(String.valueOf(_ColorHelper.RED) + "Try Example: " + _ColorHelper.DARK_AQUA + "/namecolor " + _ColorHelper.WHITE + "&b" + left + "&d" + right);
        ((IMixinICommandSender)p).sendMessage(String.valueOf(_ColorHelper.RED) + "That would make your name: " + _ColorHelper.AQUA + left + _ColorHelper.LIGHT_PURPLE + right);
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
        }
        catch (Throwable exc) {
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
            _CmdNameColor.ColorNameDict = (ConcurrentHashMap<String, String>)s.readObject();
            s.close();
            final long msEnd = System.currentTimeMillis();
            _DiwUtils.ConsoleMsg(String.valueOf(_ColorHelper.YELLOW) + String.format("%-20s: %5d players.  Took %3d ms", "Name Color Load", _CmdNameColor.ColorNameDict.size(), msEnd - msStart));
        }
        catch (Throwable exc) {
            _DiwUtils.ConsoleMsg("Loading NameColors: Starting new file: " + _CmdNameColor.m_DataFilename);
            _CmdNameColor.ColorNameDict = new ConcurrentHashMap<String, String>();
        }
    }

    @Override
    public void processCommand(MinecraftServer server, final ICommandSender cs, final String[] args) throws CommandException {
        EntityPlayer p = null;
        if (!(cs instanceof EntityPlayer)) {
            System.out.println("--- Only for players!");
            return;
        }
        p = (EntityPlayer)cs;
        if (args.length <= 0) {
            this.SendUsage(p);
            return;
        }
        final String pName = p.getName();
        final boolean isOp = _DiwUtils.IsOp(cs);
        String newName = args[0];
        if (isOp) {
            newName = _DiwUtils.ConcatArgs(args, 0);
            newName = _DiwUtils.FullTranslate(newName);
        }
        else {
            newName = _DiwUtils.TranslateChatString(newName, false);
            if (!pName.equalsIgnoreCase(_ColorHelper.stripColor(newName))) {
                ((IMixinICommandSender)p).sendMessage(String.valueOf(_ColorHelper.RED) + "Name must match your own. You can just add color!");
                this.SendUsage(p);
                return;
            }
        }
        final String key = pName.toLowerCase();
        final String existing = _CmdNameColor.ColorNameDict.get(key);
        if (existing != null && newName.equalsIgnoreCase("off")) {
            _CmdNameColor.ColorNameDict.remove(key);
            ((IMixinICommandSender)p).sendMessage(String.valueOf(_ColorHelper.GREEN) + "You remove your colored name: " + _ColorHelper.YELLOW + existing);
            return;
        }
        _CmdNameColor.ColorNameDict.put(key, newName);
        ((IMixinICommandSender)p).sendMessage(String.valueOf(_ColorHelper.GREEN) + "Your colored name is now: " + _ColorHelper.YELLOW + newName);
    }
}
