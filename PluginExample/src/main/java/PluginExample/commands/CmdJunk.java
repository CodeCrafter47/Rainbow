package PluginExample.commands;

import PluginExample.CmdBase;
import PluginExample.MiscUtils;
import PluginReference.ChatColor;
import PluginReference.MC_ItemStack;
import PluginReference.MC_Location;
import PluginReference.MC_Player;

import java.util.List;

public class CmdJunk extends CmdBase {
    public CmdJunk() {
        super("junk", "Fill your inventory with very useful items!");
    }

    @Override
    protected void execute(MC_Player plr, String[] args) {
        List<MC_ItemStack> items = plr.getInventory();
        for (int idx = 0; idx < items.size(); idx++) {
            MC_ItemStack is = items.get(idx);
            if ((is == null) || (is.getId() == 0)) {
                items.set(idx, MiscUtils.getRandomItem());
            }
        }
        plr.setInventory(items);
        plr.updateInventory();
        plr.sendMessage(ChatColor.GREEN + "Filled your inventory with junk");
    }
}
