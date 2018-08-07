package org.projectrainbow.commands;

import PluginReference.MC_Command;
import PluginReference.MC_Player;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import org.projectrainbow._ColorHelper;
import org.projectrainbow._DiwUtils;
import org.projectrainbow.interfaces.IMixinEntityPlayerMP;

import java.util.Collections;
import java.util.List;

public class _CmdSpawn implements MC_Command {
    @Override
    public String getCommandName() {
        return "spawn";
    }

    @Override
    public List<String> getAliases() {
        return Collections.emptyList();
    }

    @Override
    public boolean hasPermissionToUse(MC_Player player) {
        return player == null || player.hasPermission("rainbow.spawn");
    }

    @Override
    public List<String> getTabCompletionList(MC_Player plr, String[] args) {
        return Collections.emptyList();
    }

    @Override
    public String getHelpLine(MC_Player player) {
        return String.valueOf(_ColorHelper.AQUA) + "/spawn" + _ColorHelper.WHITE + " --- Go to Spawn";
    }

    @Override
    public void handleCommand(MC_Player player, String[] args) {
        EntityPlayer p;
        if (player != null) {
            p = (EntityPlayerMP) player;
            final WorldServer world = _DiwUtils.getMinecraftServer().getWorld(0);
            final BlockPos coords = world.getSpawnPoint();
            final double x = coords.getX() + 0.5;
            final int y = coords.getY();
            final double z = coords.getZ() + 0.5;
            ((IMixinEntityPlayerMP) p).teleport(world, x, y, z, 0.0f, 0.0f);
            player.sendMessage(String.valueOf(_ColorHelper.GREEN) + "You travel to spawn!");
            return;
        }
        System.out.println("--- Only for players!");
    }
}
