package PluginExample.commands;

import PluginExample.CmdBase;
import PluginReference.ChatColor;
import PluginReference.MC_Player;

public class CmdSetAbsorb extends CmdBase {
    public CmdSetAbsorb() {
        super("setabsorb", "Set absorbtion amount!");
    }

    @Override
    protected void execute(MC_Player plr, String[] args) {
        int num = 0;
        try {
            num = Integer.valueOf(args[0]);
        } catch (Throwable ignored) {
        }

        plr.setAbsorptionAmount(num);
        plr.sendMessage(ChatColor.GREEN + "Absorption Set to: " + ChatColor.AQUA + num);
    }
}
