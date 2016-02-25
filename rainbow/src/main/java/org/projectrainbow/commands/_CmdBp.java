package org.projectrainbow.commands;

import PluginReference.MC_Player;
import net.minecraft.server.MinecraftServer;
import net.minecraft.src.CommandBase;
import net.minecraft.src.CommandException;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ICommandSender;
import org.projectrainbow._ColorHelper;
import org.projectrainbow.interfaces.IMixinEntityPlayerMP;

import java.util.Collections;
import java.util.List;

public class _CmdBp extends CommandBase {
    @Override
    public String getCommandName() {
        return "bp";
    }

    @Override
    public List<String> getCommandAliases() {
        return Collections.singletonList("backpack");
    }

    @Override
    public boolean canCommandSenderUseCommand(MinecraftServer minecraftServer, ICommandSender iCommandSender) {
        return (!(iCommandSender instanceof MC_Player)) || ((MC_Player) iCommandSender).hasPermission("rainbow.bp");
    }

    @Override
    public String getCommandUsage(ICommandSender iCommandSender) {
        return _ColorHelper.AQUA + "/bp" + _ColorHelper.WHITE + " --- Backpack!";
    }

    @Override
    public void processCommand(MinecraftServer minecraftServer, ICommandSender cs, String[] strings) throws CommandException {
        EntityPlayer p = null;
        if (cs instanceof EntityPlayer) {
            p = (EntityPlayer) cs;
            p.a(((IMixinEntityPlayerMP) p).getBackpack());
        } else {
            System.out.println("--- Only for players!");
        }
    }
}
