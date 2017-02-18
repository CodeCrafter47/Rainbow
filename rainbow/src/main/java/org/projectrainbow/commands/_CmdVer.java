package org.projectrainbow.commands;

import PluginReference.MC_Player;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import org.projectrainbow._ColorHelper;
import org.projectrainbow._DiwUtils;
import org.projectrainbow.launch.Bootstrap;
import org.projectrainbow.Updater;

import java.util.Collections;
import java.util.List;

public class _CmdVer extends CommandBase {
    @Override
    public String getCommandName() {
        return "ver";
    }
    
    @Override
    public List<String> getCommandAliases() {
        return Collections.singletonList("version");
    }

    @Override
    public boolean checkPermission(MinecraftServer minecraftServer, ICommandSender sender) {
        return (!(sender instanceof MC_Player)) || ((MC_Player) sender).hasPermission("rainbow.version");
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return String.valueOf(_ColorHelper.AQUA) + "/ver" + _ColorHelper.WHITE + " --- Version Info";
    }

    @Override
    public void execute(MinecraftServer minecraftServer, ICommandSender cs, String[] strings) throws CommandException {
        _DiwUtils.reply(cs, String.valueOf(_ColorHelper.AQUA) + "Rainbow " + _ColorHelper.LIGHT_PURPLE + _DiwUtils.MC_VERSION_STRING + " b" + Bootstrap.buildNumber
        );
        _DiwUtils.reply(cs, "Checking for update, please wait...");
        String latest = Updater.getLatestVersion();
        if (!latest.startsWith("ERROR")) {
            if (Bootstrap.buildNumber == latest) {
                _DiwUtils.reply(cs, "You are running the latest version.");
            } else {
                int currentNum = Integer.valueOf(Bootstrap.buildNumber);
                int latestNum = Integer.valueOf(latest);
                _DiwUtils.reply(cs, "You are running " + (latestNum - currentNum) + " versions behind.");
            }
        } else {
            _DiwUtils.reply(cs, "ERROR: Update to at least Java 8"); //Outdated java should be the only error (atm).
        }
    }
}
