package PluginExample.commands;

import PluginExample.CmdBase;
import PluginReference.ChatColor;
import PluginReference.MC_Block;
import PluginReference.MC_Enderman;
import PluginReference.MC_Entity;
import PluginReference.MC_Location;
import PluginReference.MC_Player;

public class CmdEHead extends CmdBase {
    public CmdEHead() {
        super("ehead", "Places endermen carried blocks!");
    }

    @Override
    protected void execute(MC_Player plr, String[] args) {
        for (MC_Entity ent : plr.getWorld().getEntities()) {
            if (!(ent instanceof MC_Enderman)) continue;
            MC_Enderman enderman = (MC_Enderman) ent;
            MC_Block blk = enderman.getCarriedBlock();
            if (blk == null) continue;
            if (blk.getId() == 0) continue;
            MC_Location locAbove = enderman.getLocation().toBlockLocation();
            locAbove.y += 4;
            plr.getWorld().setBlockAt(locAbove, blk, blk.getSubtype());
        }

        plr.sendMessage(ChatColor.GREEN + "Carried blocks placed above Enderman heads");
    }
}
