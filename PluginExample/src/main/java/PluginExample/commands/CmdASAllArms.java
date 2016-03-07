package PluginExample.commands;

import PluginExample.CmdBase;
import PluginReference.ChatColor;
import PluginReference.MC_ArmorStand;
import PluginReference.MC_Entity;
import PluginReference.MC_Player;

public class CmdASAllArms extends CmdBase {
    public CmdASAllArms() {
        super("asallarms", "Give arms to all armorstands!");
    }

    @Override
    protected void execute(MC_Player plr, String[] args) {
        int nStands = 0;
        for (MC_Entity ent : plr.getWorld().getEntities()) {
            if (!(ent instanceof MC_ArmorStand)) continue;
            MC_ArmorStand stand = (MC_ArmorStand) ent;
            stand.setHasArms(true);
            nStands++;
        }
        plr.sendMessage(ChatColor.GREEN + "OK. " + nStands + " Armor Stands have arms!");
    }
}
