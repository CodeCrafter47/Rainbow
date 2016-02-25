package org.projectrainbow.commands;

import PluginReference.MC_Player;
import net.minecraft.server.MinecraftServer;
import net.minecraft.src.BlockPos;
import net.minecraft.src.CommandBase;
import net.minecraft.src.CommandException;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EntityPlayerMP;
import net.minecraft.src.ICommandSender;
import net.minecraft.src.WorldServer;
import org.projectrainbow._ColorHelper;
import org.projectrainbow.interfaces.IMixinEntityPlayerMP;
import org.projectrainbow.interfaces.IMixinICommandSender;

public class _CmdSpawn extends CommandBase {
    @Override
    public String getCommandName() {
        return "spawn";
    }

    @Override
    public boolean canCommandSenderUseCommand(MinecraftServer minecraftServer, ICommandSender iCommandSender) {
        return (!(iCommandSender instanceof MC_Player)) || ((MC_Player) iCommandSender).hasPermission("rainbow.spawn");
    }

    @Override
    public String getCommandUsage(ICommandSender iCommandSender) {
        return String.valueOf(_ColorHelper.AQUA) + "/spawn" + _ColorHelper.WHITE + " --- Go to Spawn";
    }

    @Override
    public void processCommand(MinecraftServer minecraftServer, ICommandSender cs, String[] args) throws CommandException {
        EntityPlayer p;
        if (cs instanceof EntityPlayerMP) {
            p = (EntityPlayerMP) cs;
            final WorldServer world = minecraftServer.worldServerForDimension(0);
            final BlockPos coords = world.getSpawnPoint();
            final int x = coords.getX();
            final int y = coords.getY();
            final int z = coords.getZ();
            ((IMixinEntityPlayerMP) p).teleport(world, x, y, z, 0.0f, 0.0f);
            ((IMixinICommandSender) p).sendMessage(String.valueOf(_ColorHelper.GREEN) + "You travel to spawn!");
            return;
        }
        System.out.println("--- Only for players!");
    }
}
