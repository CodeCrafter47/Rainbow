package PluginExample.commands;

import PluginExample.CmdBase;
import PluginReference.ChatColor;
import PluginReference.MC_Location;
import PluginReference.MC_Player;

public class CmdRemEffects extends CmdBase {
    public CmdRemEffects() {
        super("remeffects", "Removes your potion effects!");
    }

    @Override
    protected void execute(MC_Player plr, String[] args) {
        plr.setPotionEffects(null);
        plr.sendMessage(ChatColor.AQUA + "Removed your potion effects.");
    }
}
