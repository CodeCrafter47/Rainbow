package PluginExample.commands;

import PluginExample.CmdBase;
import PluginReference.ChatColor;
import PluginReference.MC_ItemStack;
import PluginReference.MC_Location;
import PluginReference.MC_Player;

public class CmdClearLore extends CmdBase {
    public CmdClearLore() {
        super("clearlore", "Remove an items lore");
    }

    @Override
    protected void execute(MC_Player plr, String[] args) {
        MC_ItemStack is = plr.getItemInHand();
        if (is.getId() == 0) {
            plr.sendMessage(ChatColor.RED + "You must be holding something!");
            return;
        }

        is.setLore(null);
        plr.sendMessage(ChatColor.GREEN + "Lore Cleared!");
    }
}
