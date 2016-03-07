package PluginExample.commands;

import PluginExample.CmdBase;
import PluginReference.ChatColor;
import PluginReference.MC_Player;

public class CmdSetFly extends CmdBase {
    public CmdSetFly() {
        super("setfly", "Start flying!");
    }

    @Override
    protected void execute(MC_Player plr, String[] args) {
        boolean newState = !plr.isFlying();
        plr.setFlying(newState);
        plr.sendMessage(ChatColor.AQUA + "Flying set to: " + ChatColor.WHITE + newState);
        plr.sendMessage(ChatColor.DARK_AQUA + "- Try setting while falling as example.");
    }
}
