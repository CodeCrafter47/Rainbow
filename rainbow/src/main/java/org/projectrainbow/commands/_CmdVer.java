package org.projectrainbow.commands;

import PluginReference.MC_Command;
import PluginReference.MC_Player;
import org.projectrainbow.Updater;
import org.projectrainbow._ColorHelper;
import org.projectrainbow._DiwUtils;
import org.projectrainbow.launch.Bootstrap;

import java.util.Collections;
import java.util.List;

public class _CmdVer implements MC_Command {
    @Override
    public String getCommandName() {
        return "ver";
    }
    
    @Override
    public List<String> getAliases() {
        return Collections.singletonList("version");
    }

    @Override
    public boolean hasPermissionToUse(MC_Player player) {
        return player == null || player.hasPermission("rainbow.version");
    }

    @Override
    public List<String> getTabCompletionList(MC_Player plr, String[] args) {
        return Collections.emptyList();
    }

    @Override
    public String getHelpLine(MC_Player player) {
        return String.valueOf(_ColorHelper.AQUA) + "/ver" + _ColorHelper.WHITE + " --- Version Info";
    }

    @Override
    public void handleCommand(MC_Player player, String[] strings) {
        _DiwUtils.reply(player, String.valueOf(_ColorHelper.AQUA) + "Rainbow " + _ColorHelper.LIGHT_PURPLE + _DiwUtils.MC_VERSION_STRING + " b" + Bootstrap.buildNumber);
        _DiwUtils.reply(player, "Checking for update, please wait...");
        _DiwUtils.reply(player, _DiwUtils.updater.checkForUpdate());
    }
}