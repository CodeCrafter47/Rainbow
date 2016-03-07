package PluginExample.commands;

import PluginExample.CmdBase;
import PluginReference.ChatColor;
import PluginReference.MC_Enchantment;
import PluginReference.MC_ItemStack;
import PluginReference.MC_Location;
import PluginReference.MC_Player;

import java.util.List;

public class CmdEnchants extends CmdBase {
    public CmdEnchants() {
        super("enchants", "Print enchantments");
    }

    @Override
    protected void execute(MC_Player plr, String[] args) {
        MC_ItemStack isHand = plr.getItemInHand();
        if (isHand.getId() == 0) {
            plr.sendMessage(ChatColor.RED + "You must be holding something!");
            return;
        }
        List<MC_Enchantment> enchants = isHand.getEnchantments();
        if (enchants.size() > 0)
            plr.sendMessage(ChatColor.WHITE + "-------------------------");
        for (MC_Enchantment enchant : enchants) {
            plr.sendMessage(ChatColor.AQUA + enchant.type.toString() + ": " + ChatColor.WHITE + enchant.level);
        }
        plr.sendMessage(ChatColor.GREEN + "Listed " + enchants.size() + " enchantments.");
    }
}
