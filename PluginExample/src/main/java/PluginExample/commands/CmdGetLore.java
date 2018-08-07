package PluginExample.commands;

import PluginExample.CmdBase;
import PluginReference.ChatColor;
import PluginReference.MC_ItemStack;
import PluginReference.MC_Player;

import java.util.List;

public class CmdGetLore extends CmdBase {
    public CmdGetLore() {
        super("getlore", "Get lore of an item!");
    }

    @Override
    protected void execute(MC_Player plr, String[] args) {

        MC_ItemStack is = plr.getItemInHand();
        if (is.getCount() == 0) {
            plr.sendMessage(ChatColor.RED + "You must be holding something!");
            return;
        }
        List<String> lore = is.getLore();
        if (lore == null) {
            plr.sendMessage(ChatColor.GREEN + "No Lore Set");
        } else {
            int nLore = lore.size();
            plr.sendMessage(ChatColor.WHITE + "Num Lore: " + nLore);
            for (int i = 0; i < nLore; i++)
                plr.sendMessage(ChatColor.AQUA + "Lore: " + ChatColor.LIGHT_PURPLE + lore.get(i));
        }
    }
}
