package PluginExample.commands;

import PluginExample.CmdBase;
import PluginReference.ChatColor;
import PluginReference.MC_ItemStack;
import PluginReference.MC_Location;
import PluginReference.MC_Player;

import java.util.List;

public class CmdHat extends CmdBase {
    public CmdHat() {
        super("hat", "This is awesome!");
    }

    @Override
    protected void execute(MC_Player plr, String[] args) {
        MC_ItemStack isToWear = plr.getItemInHand();
        if (isToWear.getId() == 0) {
            plr.sendMessage(ChatColor.RED + "You must be holding something!");
            return;
        }
        // Set head slot to what's in hand
        List armor = plr.getArmor();
        armor.set(3, isToWear); // 3=hat, 2=chest, 1=legs, 0=boots
        plr.setArmor(armor);

        // Set hand to empty
        plr.setItemInHand(null);
        plr.updateInventory();

        plr.sendMessage(ChatColor.GREEN + "You are now wearing: " + ChatColor.AQUA + isToWear.getFriendlyName());
    }
}
