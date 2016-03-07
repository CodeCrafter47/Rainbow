package PluginExample.commands;

import PluginExample.CmdBase;
import PluginExample.MyPlugin;
import PluginReference.ChatColor;
import PluginReference.MC_Entity;
import PluginReference.MC_ItemEntity;
import PluginReference.MC_ItemStack;
import PluginReference.MC_ItemType;
import PluginReference.MC_Location;
import PluginReference.MC_Player;

public class CmdItemsGold extends CmdBase {
    public CmdItemsGold() {
        super("itemsgold", "Turns dropped items into gold!");
    }

    @Override
    protected void execute(MC_Player plr, String[] args) {
        MC_ItemStack isGold = MyPlugin.server.createItemStack(MC_ItemType.GOLD_BLOCK, 1, 0);
        int nItems = 0;
        for (MC_Entity ent : plr.getWorld().getEntities()) {
            if (!(ent instanceof MC_ItemEntity)) continue;
            MC_ItemEntity item = (MC_ItemEntity) ent;
            MC_ItemStack is = item.getItemStack();
            System.out.println("Found " + is.getFriendlyName() + " @ " + ent.getLocation().toString());
            item.setItemStack(isGold.getDuplicate());
            nItems++;
        }
        plr.sendMessage(ChatColor.GREEN + "Turned " + nItems + " dropped items into gold blocks.");
    }
}
