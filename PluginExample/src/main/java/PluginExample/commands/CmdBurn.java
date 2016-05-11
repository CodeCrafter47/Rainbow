package PluginExample.commands;

import PluginExample.CmdBase;
import PluginReference.ChatColor;
import PluginReference.MC_Player;

public class CmdBurn extends CmdBase {
    public CmdBurn() {
        super("burn", "Burn");
    }

    @Override
    protected void execute(MC_Player plr, String[] args) {
        int num = 0;
        try {
            num = Integer.valueOf(args[0]);
        } catch (Throwable ignored) {
        }

        if (num <= 0) {
            plr.sendMessage(ChatColor.RED + "[ExamplePlugin] Usage: /burn # " + ChatColor.WHITE + " - Burn that many ticks");
        } else {
            plr.setFireTicks(num);
        }
    }
}
