package org.projectrainbow.commands;

import PluginReference.MC_Player;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import org.projectrainbow._ColorHelper;
import org.projectrainbow._DiwUtils;
import org.projectrainbow.interfaces.IMixinICommandSender;

public class _CmdSuicide extends CommandBase {
    @Override
    public String getCommandName() {
        return "suicide";
    }

    @Override
    public boolean checkPermission(MinecraftServer minecraftServer, ICommandSender iCommandSender) {
        return (!(iCommandSender instanceof MC_Player)) || ((MC_Player) iCommandSender).hasPermission("rainbow.suicide");
    }

    @Override
    public String getCommandUsage(ICommandSender iCommandSender) {
        return String.valueOf(_ColorHelper.AQUA) + "/suicide" + _ColorHelper.WHITE + " --- Don't do it!";
    }

    @Override
    public void execute(MinecraftServer minecraftServer, ICommandSender cs, String[] strings) throws CommandException {
        EntityPlayer p = null;
        if (!(cs instanceof EntityPlayer)) {
            System.out.println("--- Only for players!");
            return;
        }
        p = (EntityPlayer) cs;
        if (_DiwUtils.TooSoon(cs, "Suicide", 120)) {
            return;
        }
        ((IMixinICommandSender) p).sendMessage(String.valueOf(_ColorHelper.AQUA) + "You give up on the world!");
        p.onKillCommand();
    }
}
