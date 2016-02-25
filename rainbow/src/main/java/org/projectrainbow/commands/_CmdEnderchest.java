package org.projectrainbow.commands;


import PluginReference.ChatColor;
import PluginReference.MC_Command;
import PluginReference.MC_Player;
import net.minecraft.src.EntityPlayerMP;
import net.minecraft.src.InventoryEnderChest;

import java.util.Collections;
import java.util.List;


public class _CmdEnderchest implements MC_Command {

    public List<String> getAliases() {
        return Collections.singletonList("enderchest");
    }

    @Override
    public String getHelpLine(MC_Player plr) {
        return ChatColor.AQUA + "/ec " + ChatColor.WHITE
                + " --- Access enderchest";
    }

    @Override
    public void handleCommand(MC_Player plr, String[] args) {
        if (plr == null) {
            System.out.println("--- Only for players!");
            return;
        }

        EntityPlayerMP p = (EntityPlayerMP) plr;

        InventoryEnderChest ender = p.getInventoryEnderChest();

        p.a(ender);
    }

    @Override
    public boolean hasPermissionToUse(MC_Player plr) {
        return plr == null || plr.hasPermission("rainbow.ec");
    }

    @Override
    public List<String> getTabCompletionList(MC_Player plr, String[] args) {
        return null;
    }

    public String getCommandName() {
        return "ec";
    }
}
