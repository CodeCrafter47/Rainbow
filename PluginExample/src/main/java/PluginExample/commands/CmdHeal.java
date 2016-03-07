package PluginExample.commands;

import PluginExample.CmdBase;
import PluginReference.ChatColor;
import PluginReference.MC_Location;
import PluginReference.MC_Player;

public class CmdHeal extends CmdBase {
    public CmdHeal() {
        super("heal", "Heal yourself!");
    }

    @Override
    protected void execute(MC_Player plr, String[] args) {
        plr.setHealth(20);
        plr.sendMessage(ChatColor.GREEN + "Health set to 20");
    }
}
