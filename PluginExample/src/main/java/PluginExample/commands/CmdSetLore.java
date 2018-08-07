package PluginExample.commands;

import PluginExample.CmdBase;
import PluginReference.ChatColor;
import PluginReference.MC_ItemStack;
import PluginReference.MC_Player;

import java.util.ArrayList;

public class CmdSetLore extends CmdBase {
    public CmdSetLore() {
        super("setlore", "Chagne lore of an item");
    }

    @Override
    protected void execute(MC_Player plr, String[] args) {
        MC_ItemStack is = plr.getItemInHand();
        if (is.getCount() == 0) {
            plr.sendMessage(ChatColor.RED + "You must be holding something!");
            return;
        }

        if (args.length == 0) {
            plr.sendMessage(ChatColor.RED + "Usage: /setlore NewName");
            return;
        }
        ArrayList<String> lore = new ArrayList<String>();
        for (int i = 1; i <= 4; i++) lore.add(args[0] + " " + i);
        is.setLore(lore);
        plr.sendMessage(ChatColor.GREEN + "Lore Set!");
    }
}
