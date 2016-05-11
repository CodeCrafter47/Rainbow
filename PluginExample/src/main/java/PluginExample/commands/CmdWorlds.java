package PluginExample.commands;

import PluginExample.CmdBase;
import PluginReference.ChatColor;
import PluginReference.MC_Player;
import PluginReference.MC_World;

import static PluginExample.MyPlugin.server;

public class CmdWorlds extends CmdBase {
    public CmdWorlds() {
        super("worlds", "List all worlds!");
    }

    @Override
    protected void execute(MC_Player plr, String[] args) {
        for (MC_World world : server.getWorlds()) {
            plr.sendMessage(ChatColor.WHITE + world.getDimension() + ": " + ChatColor.GOLD + world.getName());
        }
    }
}
