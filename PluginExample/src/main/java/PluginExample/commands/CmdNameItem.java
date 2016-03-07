package PluginExample.commands;

import PluginExample.CmdBase;
import PluginReference.ChatColor;
import PluginReference.MC_ItemStack;
import PluginReference.MC_Location;
import PluginReference.MC_Player;
import PluginReference.RainbowUtils;

public class CmdNameItem extends CmdBase {
    public CmdNameItem() {
        super("nameitem", "Set custom name of an item!");
    }

    @Override
    protected void execute(MC_Player plr, String[] args) {
        MC_ItemStack is = plr.getItemInHand();
        if (is.getId() == 0) {
            plr.sendMessage(ChatColor.RED + "You must be holding something!");
            return;
        }

        if (args.length == 0) {
            plr.sendMessage(ChatColor.RED + "Usage: /nameitem NewName");
            return;
        }
        args[0] = RainbowUtils.RainbowString(args[0], "r");
        is.setCustomName(args[0]);
        plr.sendMessage(ChatColor.GREEN + "Set Item Name To: " + ChatColor.WHITE + args[0]);
    }
}
