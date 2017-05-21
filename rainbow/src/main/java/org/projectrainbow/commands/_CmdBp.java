package org.projectrainbow.commands;

import PluginReference.MC_Command;
import PluginReference.MC_Player;
import net.minecraft.entity.player.EntityPlayer;
import org.projectrainbow._ColorHelper;
import org.projectrainbow.interfaces.IMixinEntityPlayerMP;

import java.util.Collections;
import java.util.List;

public class _CmdBp implements MC_Command {
    @Override
    public String getCommandName() {
        return "bp";
    }

    @Override
    public List<String> getAliases() {
        return Collections.singletonList("backpack");
    }

    @Override
    public boolean hasPermissionToUse(MC_Player player) {
        return player == null || player.hasPermission("rainbow.bp");
    }

    @Override
    public List<String> getTabCompletionList(MC_Player plr, String[] args) {
        return Collections.emptyList();
    }

    @Override
    public String getHelpLine(MC_Player player) {
        return _ColorHelper.AQUA + "/bp" + _ColorHelper.WHITE + " --- Backpack!";
    }

    @Override
    public void handleCommand(MC_Player player, String[] strings) {
        if (player != null) {
            ((EntityPlayer) player).displayGUIChest(((IMixinEntityPlayerMP) player).getBackpack());
        } else {
            System.out.println("--- Only for players!");
        }
    }
}
