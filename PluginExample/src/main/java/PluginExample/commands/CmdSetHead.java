package PluginExample.commands;

import PluginExample.CmdBase;
import PluginReference.ChatColor;
import PluginReference.MC_ItemStack;
import PluginReference.MC_Player;

public class CmdSetHead extends CmdBase {
    public CmdSetHead() {
        super("sethead", "Chagne owner of a head");
    }

    @Override
    protected void execute(MC_Player plr, String[] args) {
        MC_ItemStack is = plr.getItemInHand();
        if (is.getId() == 0) {
            plr.sendMessage(ChatColor.RED + "You must be holding something!");
            return;
        }

        if (args.length == 0) {
            plr.sendMessage(ChatColor.RED + "Usage: /sethead NewName");
            return;
        }
        is.setSkullOwner(args[0]);
        plr.sendMessage(ChatColor.GREEN + "Set to: " + ChatColor.YELLOW + args[0]);
    }
}
