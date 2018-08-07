package PluginExample.commands;

import PluginExample.CmdBase;
import PluginReference.ChatColor;
import PluginReference.MC_ItemStack;
import PluginReference.MC_Player;

public class CmdClearItemName extends CmdBase {
    public CmdClearItemName() {
        super("clearitemname", "Remove the custom name from an item!");
    }

    @Override
    protected void execute(MC_Player plr, String[] args) {
        MC_ItemStack is = plr.getItemInHand();
        if (is.getCount() == 0) {
            plr.sendMessage(ChatColor.RED + "You must be holding something!");
            return;
        }
        if (!is.hasCustomName()) {
            plr.sendMessage(ChatColor.GREEN + "This item has no custom name.");
        } else {
            is.removeCustomName();
            plr.sendMessage(ChatColor.GREEN + "Custom Item Name Cleared.");
        }
    }
}
