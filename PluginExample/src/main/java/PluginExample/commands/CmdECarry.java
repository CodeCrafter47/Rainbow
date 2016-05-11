package PluginExample.commands;

import PluginExample.CmdBase;
import PluginExample.MyPlugin;
import PluginReference.ChatColor;
import PluginReference.MC_Block;
import PluginReference.MC_Enderman;
import PluginReference.MC_Entity;
import PluginReference.MC_Player;

public class CmdECarry extends CmdBase {
    public CmdECarry() {
        super("ecarry", "Change enderman blocks!");
    }

    @Override
    protected void execute(MC_Player plr, String[] args) {
        String remainder = args.length == 0 ? "" : args[0];
        MC_Block blk = MyPlugin.server.getBlockFromName(remainder);
        if (blk == null) {
            plr.sendMessage(ChatColor.RED + "Unknown block name: " + ChatColor.YELLOW + remainder);
            return;
        }
        for (MC_Entity ent : plr.getWorld().getEntities()) {
            if (!(ent instanceof MC_Enderman)) continue;
            MC_Enderman enderman = (MC_Enderman) ent;
            MC_Block blkCarry = enderman.getCarriedBlock();
            if ((blkCarry == null) || (blkCarry.getId() == 0)) {
                enderman.setCarriedBlock(blk);
                break;
            }
        }

        plr.sendMessage(ChatColor.GREEN + "An Enderman now carrying: BlkID=" + ChatColor.GOLD + blk.getId());
    }
}
