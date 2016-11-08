package org.projectrainbow.commands;

import PluginReference.MC_Player;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import org.projectrainbow._ColorHelper;
import org.projectrainbow.interfaces.IMixinEntityPlayerMP;
import org.projectrainbow.interfaces.IMixinICommandSender;

public class _CmdSpawn extends CommandBase {
    @Override
    public String getCommandName() {
        return "spawn";
    }

    @Override
    public boolean checkPermission(MinecraftServer minecraftServer, ICommandSender iCommandSender) {
        return (!(iCommandSender instanceof MC_Player)) || ((MC_Player) iCommandSender).hasPermission("rainbow.spawn");
    }

    @Override
    public String getCommandUsage(ICommandSender iCommandSender) {
        return String.valueOf(_ColorHelper.AQUA) + "/spawn" + _ColorHelper.WHITE + " --- Go to Spawn";
    }

    @Override
    public void execute(MinecraftServer minecraftServer, ICommandSender cs, String[] args) throws CommandException {
        EntityPlayer p;
        if (cs instanceof EntityPlayerMP) {
            p = (EntityPlayerMP) cs;
            final WorldServer world = minecraftServer.worldServerForDimension(0);
            final BlockPos coords = world.getSpawnPoint();
            final int x = coords.getX() + 0.5;
            final int y = coords.getY();
            final int z = coords.getZ() + 0.5;
            ((IMixinEntityPlayerMP) p).teleport(world, x, y, z, 0.0f, 0.0f);
            ((IMixinICommandSender) p).sendMessage(String.valueOf(_ColorHelper.GREEN) + "You travel to spawn!");
            return;
        }
        System.out.println("--- Only for players!");
    }
}
