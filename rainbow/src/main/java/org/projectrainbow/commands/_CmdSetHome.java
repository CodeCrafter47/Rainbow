package org.projectrainbow.commands;

import PluginReference.MC_Command;
import PluginReference.MC_Player;
import joebkt._SerializableLocation;
import net.minecraft.entity.player.EntityPlayer;
import org.projectrainbow._ColorHelper;
import org.projectrainbow._HomeUtils;

import java.util.Collections;
import java.util.List;

public class _CmdSetHome implements MC_Command {
    @Override
    public String getCommandName() {
        return "sethome";
    }

    @Override
    public List<String> getAliases() {
        return Collections.emptyList();
    }

    @Override
    public boolean hasPermissionToUse(MC_Player player) {
        return player == null || player.hasPermission("rainbow.sethome");
    }

    @Override
    public List<String> getTabCompletionList(MC_Player plr, String[] args) {
        return Collections.emptyList();
    }

    @Override
    public String getHelpLine(MC_Player player) {
        return String.valueOf(_ColorHelper.AQUA) + "/sethome" + _ColorHelper.WHITE + " --- Set a home";
    }

    @Override
    public void handleCommand(MC_Player player, String[] args) {
        if (player == null) {
            System.out.println("Sethome is for Players only!");
            return;
        }
        final EntityPlayer p = (EntityPlayer) player;
        final String pName = player.getName();
        final _SerializableLocation sloc = new _SerializableLocation(p.posX, p.posY, p.posZ, p.dimension, p.rotationYaw, p.rotationPitch);
        _HomeUtils.setHome(p.getUniqueID(), sloc);
        _HomeUtils.SaveHomes();
        final String msg = String.format("Home Set for %s set to %s", pName, sloc.toString());
        System.out.println(msg);
        player.sendMessage(String.valueOf(_ColorHelper.GREEN) + "Home set to " + _ColorHelper.WHITE + sloc.toString());
    }
}
