package PluginExample.commands;

import PluginExample.CmdBase;
import PluginExample.MyPlugin;
import PluginReference.*;

public class CmdItemsGold extends CmdBase {
    public CmdItemsGold() {
        super("itemsgold", "Turns dropped items into gold!");
    }

    @Override
    protected void execute(MC_Player plr, String[] args) {
        MC_ItemStack isGold = MyPlugin.server.createItemStack("gold_block", 1);
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
