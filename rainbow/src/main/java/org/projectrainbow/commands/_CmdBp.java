package org.projectrainbow.commands;

import PluginReference.MC_Player;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
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
    public boolean checkPermission(MinecraftServer minecraftServer, ICommandSender iCommandSender) {
        return (!(iCommandSender instanceof MC_Player)) || ((MC_Player) iCommandSender).hasPermission("rainbow.bp");
    }

    @Override
    public String getCommandUsage(ICommandSender iCommandSender) {
        return _ColorHelper.AQUA + "/bp" + _ColorHelper.WHITE + " --- Backpack!";
    }

    @Override
    public void execute(MinecraftServer minecraftServer, ICommandSender cs, String[] strings) throws CommandException {
        EntityPlayer p = null;
        if (cs instanceof EntityPlayer) {
            p = (EntityPlayer) cs;
            p.displayGUIChest(((IMixinEntityPlayerMP) p).getBackpack());
        } else {
            System.out.println("--- Only for players!");
        }
    }
}
