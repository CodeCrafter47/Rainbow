package PluginExample;

import PluginReference.ChatColor;
import PluginReference.MC_Command;
import PluginReference.MC_CommandSenderInfo;
import PluginReference.MC_Player;
import PluginReference.RainbowUtils;

import java.util.List;

public abstract class CmdBase implements MC_Command {
    private final String name;
    private final String description;

    public CmdBase(String name, String description) {
        this.name = name;
        this.description = description;
    }

    @Override
    public String getHelpLine(MC_Player plr) {
        return ChatColor.LIGHT_PURPLE + "/" + name + ChatColor.WHITE + " --- " + RainbowUtils.RainbowString(description);
    }

    @Override
    public String getCommandName() {
        return name;
    }

    @Override
    public List<String> getAliases() {
        return null;
    }

    @Override
    public void handleCommand(MC_Player plr, String[] args) {
        MC_CommandSenderInfo info = MyPlugin.server.getExecutingCommandInfo();
        if (info != null) {
            System.out.println("-----------------------------------");
            System.out.println("Command Line: " + info.lastCommand);
            System.out.println("Sender Type: " + info.senderType);
            System.out.println("-----------------------------------");
        }

        // Check if running from console...
        if (plr == null) {
            // Tell Console this command is for players only...
            System.out.println("--- Only for players!");
            return;
        }
        execute(plr, args);
    }

    protected abstract void execute(MC_Player plr, String[] args);

    @Override
    public boolean hasPermissionToUse(MC_Player plr) {
        return plr == null || plr.hasPermission("pluginexample." + name);
    }

    @Override
    public List<String> getTabCompletionList(MC_Player plr, String[] args) {
        return null;
    }
}
