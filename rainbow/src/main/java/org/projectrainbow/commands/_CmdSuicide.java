package org.projectrainbow.commands;

import PluginReference.MC_Command;
import PluginReference.MC_Player;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import org.projectrainbow._ColorHelper;
import org.projectrainbow._DiwUtils;

import java.util.Collections;
import java.util.List;

public class _CmdSuicide implements MC_Command {
    @Override
    public String getCommandName() {
        return "suicide";
    }

    @Override
    public List<String> getAliases() {
        return Collections.emptyList();
    }

    @Override
    public boolean hasPermissionToUse(MC_Player player) {
        return player == null || player.hasPermission("rainbow.suicide");
    }

    @Override
    public List<String> getTabCompletionList(MC_Player plr, String[] args) {
        return Collections.emptyList();
    }

    @Override
    public String getHelpLine(MC_Player player) {
        return String.valueOf(_ColorHelper.AQUA) + "/suicide" + _ColorHelper.WHITE + " --- Don't do it!";
    }

    @Override
    public void handleCommand(MC_Player player, String[] args) {
        if (player == null) {
            System.out.println("--- Only for players!");
            return;
        }
        if (_DiwUtils.TooSoon((ICommandSender) player, "Suicide", 120)) {
            return;
        }
        player.sendMessage(String.valueOf(_ColorHelper.AQUA) + "You give up on the world!");
        ((EntityPlayerMP) player).onKillCommand();
    }
}
