package org.projectrainbow.commands;

import PluginReference.MC_Location;
import PluginReference.MC_Player;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
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
    public boolean checkPermission(MinecraftServer minecraftServer, ICommandSender iCommandSender) {
        return (!(iCommandSender instanceof MC_Player)) || ((MC_Player) iCommandSender).hasPermission("rainbow.home");
    }

    @Override
    public String getCommandUsage(ICommandSender iCommandSender) {
        return String.valueOf(_ColorHelper.AQUA) + "/home" + _ColorHelper.WHITE + " --- Travel home";
    }

    @Override
    public void execute(MinecraftServer minecraftServer, ICommandSender cs, String[] args) throws CommandException {
        if (!(cs instanceof EntityPlayer)) {
            System.out.println("Only for players.");
            return;
        }
        final EntityPlayerMP p = (EntityPlayerMP)cs;
        _SerializableLocation sloc = _HomeUtils.getHome(p.getUniqueID());
        if (sloc == null) {
            ((IMixinICommandSender)p).sendMessage(String.valueOf(_ColorHelper.RED) + "You don't have a home set. Try first: " + _ColorHelper.GOLD + "/sethome");
            return;
        }
        try {
            p.dismountRidingEntity();
            for (Entity entity : p.getPassengers()) {
                entity.dismountRidingEntity();
            }
        }
        catch (Exception exc) {
            System.out.println("Home Step 1 SetVehicle EXC: " + exc.getMessage());
        }
        ((MC_Player)p).teleport(new MC_Location(sloc.x, sloc.y, sloc.z, sloc.dimension, sloc.yaw, sloc.pitch));
        ((IMixinICommandSender)p).sendMessage(String.valueOf(_ColorHelper.GREEN) + "You teleport home to " + _ColorHelper.WHITE + sloc.toString());
    }
}
