package org.projectrainbow.commands;

import PluginReference.MC_Player;
import net.minecraft.server.MinecraftServer;
import net.minecraft.src.CommandBase;
import net.minecraft.src.CommandException;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EntityPlayerMP;
import net.minecraft.src.ICommandSender;
import org.projectrainbow._ColorHelper;
import org.projectrainbow._HomeUtils;
import joebkt._SerializableLocation;
import org.projectrainbow.interfaces.IMixinEntityPlayerMP;
import org.projectrainbow.interfaces.IMixinICommandSender;

public class _CmdHome extends CommandBase
{
    @Override
    public String getCommandName() {
        return "home";
    }

    @Override
    public boolean canCommandSenderUseCommand(MinecraftServer minecraftServer, ICommandSender iCommandSender) {
        return (!(iCommandSender instanceof MC_Player)) || ((MC_Player) iCommandSender).hasPermission("rainbow.home");
    }

    @Override
    public String getCommandUsage(ICommandSender iCommandSender) {
        return String.valueOf(_ColorHelper.AQUA) + "/home" + _ColorHelper.WHITE + " --- Travel home";
    }

    @Override
    public void processCommand(MinecraftServer minecraftServer, ICommandSender cs, String[] args) throws CommandException {
        if (!(cs instanceof EntityPlayer)) {
            System.out.println("Only for players.");
            return;
        }
        final EntityPlayerMP p = (EntityPlayerMP)cs;
        if (p.dimension != 0) {
            ((IMixinICommandSender)p).sendMessage(String.valueOf(_ColorHelper.RED) + "Home not allowed in this world!");
            return;
        }
        final String pName = p.getName();
        _SerializableLocation sloc = _HomeUtils.getHome(p.getUniqueID());
        if (sloc == null) {
            ((IMixinICommandSender)p).sendMessage(String.valueOf(_ColorHelper.RED) + "You don't have a home set. Try first: " + _ColorHelper.GOLD + "/sethome");
            return;
        }
        try {
            p.stopRiding();
            for (Entity entity : p.getPassengers()) {
                entity.stopRiding();
            }
        }
        catch (Exception exc) {
            System.out.println("Home Step 1 SetVehicle EXC: " + exc.getMessage());
        }
        ((IMixinEntityPlayerMP)p).teleport(p.getServerForPlayer(), sloc.x, sloc.y, sloc.z, sloc.yaw, sloc.pitch);
        ((IMixinICommandSender)p).sendMessage(String.valueOf(_ColorHelper.GREEN) + "You teleport home to " + _ColorHelper.WHITE + sloc.toString());
    }
}
