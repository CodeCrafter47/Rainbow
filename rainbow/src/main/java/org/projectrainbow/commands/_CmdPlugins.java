package org.projectrainbow.commands;


import PluginReference.ChatColor;
import PluginReference.MC_Command;
import PluginReference.MC_Player;
import PluginReference.PluginInfo;
import org.projectrainbow._DiwUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;


public class _CmdPlugins implements MC_Command {

    public String getCommandName() {
        return "plugins";
    }

    @Override
    public List<String> getAliases() {
        return null;
    }

    @Override
    public String getHelpLine(MC_Player plr) {
        return ChatColor.AQUA + "/plugins" + ChatColor.WHITE
                + " --- List plugins loaded";
    }

    @Override
    public void handleCommand(MC_Player plr, String[] args) {
        ArrayList pluginNames = new ArrayList();
        Iterator nextColor = _DiwUtils.pluginManager.plugins.iterator();

        while (nextColor.hasNext()) {
            PluginInfo strPlugins = (PluginInfo) nextColor.next();

            pluginNames.add(strPlugins.name);
        }

        Collections.sort(pluginNames);
        String var8 = ChatColor.RED + "None";

        if (pluginNames.size() > 0) {
            String var9 = ChatColor.YELLOW;
            StringBuilder sb = new StringBuilder();

            for (int i = 0; i < pluginNames.size(); ++i) {
                if (sb.length() > 0) {
                    sb.append(ChatColor.WHITE).append(", ");
                }

                sb.append(var9).append((String) pluginNames.get(i));
                if (var9.equals(ChatColor.GOLD)) {
                    var9 = ChatColor.YELLOW;
                } else if (var9.equals(ChatColor.YELLOW)) {
                    var9 = ChatColor.GREEN;
                } else if (var9.equals(ChatColor.GREEN)) {
                    var9 = ChatColor.AQUA;
                } else if (var9.equals(ChatColor.AQUA)) {
                    var9 = ChatColor.LIGHT_PURPLE;
                } else if (var9.equals(ChatColor.LIGHT_PURPLE)) {
                    var9 = ChatColor.RED;
                } else if (var9.equals(ChatColor.RED)) {
                    var9 = ChatColor.GOLD;
                }
            }

            var8 = sb.toString();
        }

        _DiwUtils.reply(plr,
                _DiwUtils.RainbowString("Plugins: ") + ChatColor.WHITE + var8);
    }

    @Override
    public boolean hasPermissionToUse(MC_Player plr) {
        return plr == null || plr.hasPermission("rainbow.plugins");
    }

    @Override
    public List<String> getTabCompletionList(MC_Player plr, String[] args) {
        return null;
    }
}
