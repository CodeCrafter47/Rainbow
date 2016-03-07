package PluginExample.commands;

import PluginExample.CmdBase;
import PluginReference.ChatColor;
import PluginReference.MC_Location;
import PluginReference.MC_Player;

public class CmdFly extends CmdBase {
    public CmdFly() {
        super("fly", "Allows flying!");
    }

    @Override
    protected void execute(MC_Player plr, String[] args) {
        boolean newState = !plr.isAllowedFlight();
        plr.setAllowFlight(newState);
        plr.sendMessage(ChatColor.AQUA + "Allow Flying set to: " + ChatColor.WHITE + newState);
    }
}
