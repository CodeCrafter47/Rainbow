package PluginExample.commands;

import PluginExample.CmdBase;
import PluginReference.ChatColor;
import PluginReference.MC_Location;
import PluginReference.MC_Player;

public class CmdArrows extends CmdBase {
    public CmdArrows() {
        super("arrows", "OMG Arrows!");
    }

    @Override
    protected void execute(MC_Player plr, String[] args) {
        plr.setNumberOfArrowsHitWith(127);
        plr.sendMessage(ChatColor.GREEN + "OMG! Arrows!");
    }
}
