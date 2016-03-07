package PluginExample.commands;

import PluginExample.CmdBase;
import PluginReference.ChatColor;
import PluginReference.MC_Location;
import PluginReference.MC_Player;

public class CmdGetBurn extends CmdBase {
    public CmdGetBurn() {
        super("getburn", "Get remaining fire ticks.");
    }

    @Override
    protected void execute(MC_Player plr, String[] args) {
        plr.sendMessage(ChatColor.AQUA + "Fire Ticks: " + ChatColor.WHITE + plr.getFireTicks());
    }
}
