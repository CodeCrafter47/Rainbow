package PluginExample.commands;

import PluginExample.CmdBase;
import PluginExample.MyPlugin;
import PluginReference.ChatColor;
import PluginReference.MC_ItemStack;
import PluginReference.MC_ItemType;
import PluginReference.MC_Player;
import PluginReference.RainbowUtils;

import java.util.Arrays;
import java.util.List;

public class CmdHead extends CmdBase {

    public CmdHead() {
        super("head", "Get a player head!");
    }

    @Override
    public String getCommandName() {
        return "head";
    }

    @Override
    public List<String> getAliases() {
        // Allow users to use "/skull" as well as "/head" for this command...
        return Arrays.asList(new String[]{"skull"});
    }

    @Override
    public List<String> getTabCompletionList(MC_Player plr, String[] args) {
        String plrName = "?";
        if (plr != null) plrName = plr.getName();
        System.out.println(String.format("Tab: plr=%s.  args=%s", plrName, RainbowUtils.GetCommaList(args)));

        if (args.length >= 0)
            return MyPlugin.server.getMatchingOnlinePlayerNames(args[0]);
        return null;
    }

    @Override
    protected void execute(MC_Player plr, String[] args) {
        // If no argument, tell them usage...
        if (args.length <= 0) {
            plr.sendMessage(getHelpLine(plr));
            return;
        }

        String tgtName = args[0];

        // Be friendly to user.  If they entered partial name, lookup exact spelling...
        String exactName = MyPlugin.server.getPlayerExactName(tgtName);

        // If found a match, update target name.
        if (exactName != null) {
            tgtName = exactName;
        } else {
            // Warn if no exact match found...
            plr.sendMessage(ChatColor.LIGHT_PURPLE + "Warning: " + ChatColor.YELLOW + tgtName + ChatColor.LIGHT_PURPLE + " not known, will assume correct spelling...");
        }

        plr.sendMessage(ChatColor.GREEN + "Receiving player head: " + ChatColor.YELLOW + tgtName);

        // Issue command to get player skull...
        //String headCmd = String.format("/give @p skull 1 3 {SkullOwner:%s}", tgtName);
        //MyPlugin.server.executeCommand(headCmd);
        MC_ItemStack is = MyPlugin.server.createItemStack(MC_ItemType.SKELETON_SKULL, 1, 3);
        is.setSkullOwner(tgtName);
        plr.getWorld().dropItem(is, plr.getLocation(), plr.getName());
        //plr.setItemInHand(is);

        // As example of using "getOnlinePlayerByName", if target player is online send them a message
        MC_Player pTgt = MyPlugin.server.getOnlinePlayerByName(tgtName);
        if ((pTgt != null) && !tgtName.equalsIgnoreCase(plr.getName()))
            pTgt.sendMessage(ChatColor.LIGHT_PURPLE + "" + ChatColor.ITALIC + "Your head was stolen by " + ChatColor.RED + plr.getName());
    }
}
