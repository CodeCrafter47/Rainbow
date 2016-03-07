package PluginExample.commands;

import PluginExample.CmdBase;
import PluginReference.ChatColor;
import PluginReference.MC_Location;
import PluginReference.MC_Player;

public class CmdNether extends CmdBase {
    public CmdNether() {
        super("nether", "Sends you to the nether");
    }

    @Override
    protected void execute(MC_Player plr, String[] args) {
        MC_Location loc = new MC_Location(-50, 68, 50, -1, 0, 0);
        plr.teleport(loc);
        plr.sendMessage(ChatColor.GREEN + "Ok. Sending you to nether, hope you don't land in a wall!");
    }
}
