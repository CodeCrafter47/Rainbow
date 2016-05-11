package PluginExample.commands;

import PluginExample.CmdBase;
import PluginReference.ChatColor;
import PluginReference.MC_Player;

public class CmdWalkSpeed extends CmdBase {
    public CmdWalkSpeed() {
        super("walkspeed", "Change walk speed!");
    }

    @Override
    protected void execute(MC_Player plr, String[] args) {
        int num = 0;
        try {
            num = Integer.valueOf(args[0]);
        } catch (Throwable ignored) {
        }

        if (num <= 0) {
            plr.sendMessage(ChatColor.RED + "[ExamplePlugin] Usage: /walkspeed # " + ChatColor.WHITE + " - Use 1 for default");
        } else {
            // keep within reason...
            if (num > 10) num = 10;

            // Use multiple of default...
            float newSpeed = num * 0.1F;
            plr.setWalkSpeed(newSpeed);
            plr.sendMessage(ChatColor.GREEN + String.format("Set walk speed to %d (speed value=%.2f)", num, newSpeed));
        }
    }
}
