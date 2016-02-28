package PluginExample;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


import PluginReference.*;

public class CmdHead implements MC_Command
{

	@Override
	public String getCommandName()
	{
		return "head";
	}
	@Override
	public List<String> getAliases()
	{
		// Allow users to use "/skull" as well as "/head" for this command...
		return Arrays.asList(new String[]{"skull"});
	}

	@Override
	public String getHelpLine(MC_Player plr)
	{
	    return ChatColor.AQUA + "/head" + ChatColor.YELLOW + " PlayerName " + ChatColor.WHITE + " - " + RainbowUtils.RainbowString("Get a player head!");
	}

	@Override
	public List<String> getTabCompletionList(MC_Player plr, String[] args)
	{
		String plrName = "?";
		if(plr != null) plrName = plr.getName();
		System.out.println(String.format("Tab: plr=%s.  args=%s", plrName, RainbowUtils.GetCommaList(args)));

		if(args.length >= 0) return MyPlugin.server.getMatchingOnlinePlayerNames(args[0]);
		return null;
	}

	@Override
	public void handleCommand(MC_Player plr, String[] args)
	{
		MC_CommandSenderInfo info = MyPlugin.server.getExecutingCommandInfo();
		if(info != null)
		{
			System.out.println("-----------------------------------");
			System.out.println("Command Line: " + info.lastCommand);
			System.out.println("Sender Type: " + info.senderType);
			System.out.println("Thread name: " + Thread.currentThread().getName());
			System.out.println("-----------------------------------");
		}
				
		// Check if running from console...
		if(plr == null)
		{
			// Tell Console this command is for players only...
			System.out.println("This command is for players only.");
			return;
		}
		
		// If no argument, tell them usage...
		if(args.length <= 0)
		{
			plr.sendMessage(getHelpLine(plr));
			return;
		}
		
		String tgtName = args[0];
		
		// Be friendly to user.  If they entered partial name, lookup exact spelling...
		String exactName = MyPlugin.server.getPlayerExactName(tgtName);
		
		// If found a match, update target name.
		if(exactName != null) 
		{
			tgtName = exactName;
		}
		else
		{
			// Warn if no exact match found...
			plr.sendMessage(ChatColor.LIGHT_PURPLE + "Warning: " + ChatColor.YELLOW + tgtName + ChatColor.LIGHT_PURPLE + " not known, will assume correct spelling...");
		}

		plr.sendMessage(ChatColor.GREEN + "Receiving player head: " + ChatColor.YELLOW + tgtName);

		// Issue command to get player skull...
		//String headCmd = String.format("/give @p skull 1 3 {SkullOwner:%s}", tgtName);
		//MyPlugin.server.executeCommand(headCmd);
		MC_ItemStack is = MyPlugin.server.createItemStack(MC_ItemType.SKELETON_SKULL, 1, 3);
		is.setSkullOwner(tgtName);
		plr.getWorld().dropItem(is,  plr.getLocation(), plr.getName());
		//plr.setItemInHand(is);

		// As example of using "getOnlinePlayerByName", if target player is online send them a message
		MC_Player pTgt = MyPlugin.server.getOnlinePlayerByName(tgtName);
		if((pTgt != null) && !tgtName.equalsIgnoreCase(plr.getName())) pTgt.sendMessage(ChatColor.LIGHT_PURPLE + "" + ChatColor.ITALIC + "Your head was stolen by " + ChatColor.RED + plr.getName());
	}

	@Override
	public boolean hasPermissionToUse(MC_Player plr)
	{
		if(plr == null) return true;
		return plr.hasPermission("head.create");
	}


}
