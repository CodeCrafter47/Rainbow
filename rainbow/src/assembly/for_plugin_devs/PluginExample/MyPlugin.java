package PluginExample;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import PluginReference.*;

public class MyPlugin extends PluginBase
{
	public static MC_Server server = null;
	
	// Following map used for /enchantcopy /enchantpaste sample
	public static ConcurrentHashMap<String, List<MC_Enchantment>> hashEnchantStore = new ConcurrentHashMap<String, List<MC_Enchantment>>();
	public static ConcurrentHashMap<String, MC_Location> hashCompassTarget = new ConcurrentHashMap<String, MC_Location>();
	
	public void onStartup(MC_Server argServer)
	{
		System.out.println("======= ExamplePlugin --- Startup! =======");
		server = argServer;
		
		// Set a custom shutdown message. 
		server.setCustomShutdownMessage("Custom Message from PluginExample");
		server.registerCommand(new CmdHead());
		
		/*System.out.println("- Creating Sample World...");
		MC_WorldSettings worldSettings = new MC_WorldSettings();
		worldSettings.biomeType = MC_WorldBiomeType.ROOFED_FOREST;
		worldSettings.levelType = MC_WorldLevelType.LARGE_BIOMES;
		worldSettings.generateStructures = true;
		worldSettings.seed = 85490543201L;
		int idxWorld = server.registerWorld("PluginExampleWorld", worldSettings);
		System.out.println("- Sample World IDX = " + idxWorld);*/
		
	}
	
	public void onShutdown()
	{
		System.out.println("======= ExamplePlugin --- Shutdown! =======");
	}

	public void onServerFullyLoaded() 
	{
		server.setServerMOTD("Hello from PluginExample");
		server.setServerIconFilename("icon2.png");
	} 
	
	public PluginInfo getPluginInfo() 
	{ 
		PluginInfo info = new PluginInfo();
		info.description = "Rainbow Plugin Sample";
		info.eventSortOrder = 1000.0f; // call me much later on than default.

		// As example of controlling order based on another plugin.
		// This tells Rainbow to put us before 'RainbowZones' if it was found
		info.pluginNamesINeedToGetEventsBefore = new ArrayList<String>();
		info.pluginNamesINeedToGetEventsBefore.add("RainbowZones");
		
		return info;
	}

	
	public static String GetAfterSpace(String msg)
	{
		int idx = msg.indexOf(' ');
		if(idx >= 0) return msg.substring(idx+1);
		return "";
	}
	
	
	public void onPlayerInput(MC_Player plr, String msg, MC_EventInfo ei)
	{
		if(ei.isCancelled) return;
		
		// Output some FYI to console...
		System.out.println("======= PluginExample, inspecting input: " + String.format("Player '%s': %s", plr.getName(), msg));
		
		// Check for /test command
		if(msg.startsWith("/test1"))
		{
			plr.sendMessage(ChatColor.AQUA + "[ExamplePlugin] " + ChatColor.GREEN + "Hello from Plugin!");

			MC_Location loc = plr.getLocation();
			plr.sendMessage(ChatColor.AQUA + "[ExamplePlugin] " + ChatColor.LIGHT_PURPLE + "Your Location: " + ChatColor.WHITE + loc.toString());
			
			//plr.getServer().broadcastMessage(ChatColor.GOLD + "-- Hey Everyone, I'm " + ChatColor.YELLOW + plr.getName());

			ei.isCancelled = true;
			return;
		}
		if(msg.equalsIgnoreCase("/nether") && plr.isOp())
		{
			ei.isCancelled = true;
			
			MC_Location loc = new MC_Location(-50, 68, 50, -1, 0, 0);
			plr.teleport(loc);
			plr.sendMessage(ChatColor.GREEN + "Ok. Sending you to nether, hope you don't land in a wall!");
			
			return;
		}

		if(msg.equalsIgnoreCase("/nicktest") && plr.isOp())
		{
			ei.isCancelled = true;
			plr.setCustomName(RainbowUtils.RainbowString("SampleName"));
			plr.sendMessage(ChatColor.GREEN + "Ok. Your name is now: " + ChatColor.WHITE + plr.getCustomName());
			return;
		}
		if(msg.equalsIgnoreCase("/nickclear") && plr.isOp())
		{
			ei.isCancelled = true;
			plr.setCustomName(null);
			plr.sendMessage(ChatColor.GREEN + "Ok. Your name is now: " + ChatColor.YELLOW + plr.getCustomName());
			return;
		}
		if(msg.equalsIgnoreCase("/arrows") && plr.isOp())
		{
			ei.isCancelled = true;
			plr.setNumberOfArrowsHitWith(127);
			plr.sendMessage(ChatColor.GREEN + "OMG! Arrows!");
			return;
		}
		if(msg.equalsIgnoreCase("/biome"))
		{
			ei.isCancelled = true;
			MC_Location loc = plr.getLocation();
			MC_WorldBiomeType biome = plr.getWorld().getBiomeTypeAt(loc.getBlockX(), loc.getBlockZ());
			plr.sendMessage(ChatColor.GREEN + "Biome where you are: " + ChatColor.WHITE + biome.toString());
			return;
		}
		if(msg.startsWith("/setbiome") && plr.isOp())
		{
			ei.isCancelled = true;
			String remainder = GetAfterSpace(msg);
			MC_WorldBiomeType biomeType = null;
			for(MC_WorldBiomeType biomeIter :  MC_WorldBiomeType.values())
			{
				if(biomeIter.toString().equalsIgnoreCase(remainder))
				{
					biomeType = biomeIter;
				}
			}
			if(biomeType == null)
			{
				plr.sendMessage(ChatColor.RED + "Unknown biome type: " + ChatColor.WHITE + remainder);
				return;
			}
			MC_Location loc = plr.getLocation();
			plr.getWorld().setBiomeTypeAt(loc.getBlockX(), loc.getBlockZ(), biomeType);
			plr.sendMessage(ChatColor.GREEN + "Biome set to: " + ChatColor.WHITE + biomeType.toString());
			return;
		}
		
		if(msg.equalsIgnoreCase("/junk") && plr.isOp())
		{
			ei.isCancelled= true;
			
			List<MC_ItemStack> items = plr.getInventory();
			for(int idx=0; idx<items.size(); idx++)
			{				
				MC_ItemStack is = items.get(idx);
				if((is == null) || (is.getId() == 0))
				{
					items.set(idx, MiscUtils.getRandomItem());
				}
			}
			plr.setInventory(items);
			plr.updateInventory();
			plr.sendMessage(ChatColor.GREEN + "Filled your inventory with junk");
			return;
		}
		if(msg.equalsIgnoreCase("/chunktest1"))
		{
			ei.isCancelled= true;

			plr.sendMessage(ChatColor.GREEN + "Chunk at cx=1000, cz=1000. IsLoaded=" + ChatColor.AQUA + plr.getWorld().isChunkLoaded(1000, 1000));
			return;
		}
		if(msg.equalsIgnoreCase("/chunktest2"))
		{
			ei.isCancelled= true;

			plr.getWorld().loadChunk(1000, 1000);
			plr.sendMessage(ChatColor.GREEN + "Loaded Chunk at cx=1000, cz=1000");
			return;
		}
		if(msg.equalsIgnoreCase("/chunktest3"))
		{
			ei.isCancelled= true;

			List<MC_Chunk> loadedChunks = plr.getWorld().getLoadedChunks();
			for(int i=0; (i<loadedChunks.size()); i++)
			{
				MC_Chunk thisChunk = loadedChunks.get(i);
				
					
				String outMsg = String.format("Chunk %d: cx=%d, cz=%d", i+1, thisChunk.getCX(), thisChunk.getCZ());
				if(i < 20) plr.sendMessage(ChatColor.AQUA + outMsg);
				else if(i == 20) plr.sendMessage(ChatColor.WHITE + "...");
				System.out.println(outMsg);
			}
			String outMsg2 = ChatColor.LIGHT_PURPLE + "Loaded chunks for this world: " + ChatColor.GREEN + loadedChunks.size();
			plr.sendMessage(outMsg2);
			return;
		}

		if(msg.equalsIgnoreCase("/worlds"))
		{
			ei.isCancelled= true;
			for(MC_World world : server.getWorlds())
			{
				plr.sendMessage(ChatColor.WHITE + world.getDimension() + ": " + ChatColor.GOLD + world.getName());
			}
			//plr.sendMessage(ChatColor.GREEN + "Filled your inventory with junk");
			return;
		}
		
		// ItemEntity test
		if(msg.startsWith("/itemsgold") && plr.isOp())
		{
			ei.isCancelled = true;
			MC_ItemStack isGold = MyPlugin.server.createItemStack(MC_ItemType.GOLD_BLOCK, 1, 0);
			int nItems = 0;
			for(MC_Entity ent : plr.getWorld().getEntities())
			{
				if(!(ent instanceof MC_ItemEntity)) continue;
				MC_ItemEntity item = (MC_ItemEntity)ent;
				MC_ItemStack is = item.getItemStack();
				System.out.println("Found " + is.getFriendlyName() + " @ " + ent.getLocation().toString());
				item.setItemStack(isGold.getDuplicate());
				nItems++;
			}
			plr.sendMessage(ChatColor.GREEN + "Turned " + nItems + " dropped items into gold blocks.");
			return;
		}		
		
		// Enderman tests
		// ==============================================
		if(msg.startsWith("/ecarry") && plr.isOp())
		{
			ei.isCancelled = true;
			String remainder = GetAfterSpace(msg);
			MC_Block blk = plr.getWorld().getBlockFromName(remainder);
			if(blk == null)
			{
				plr.sendMessage(ChatColor.RED + "Unknown block name: " + ChatColor.YELLOW + remainder);
				return;
			}
			for(MC_Entity ent : plr.getWorld().getEntities())
			{
				if(!(ent instanceof MC_Enderman)) continue;
				MC_Enderman enderman = (MC_Enderman)ent;
				MC_Block blkCarry = enderman.getCarriedBlock();
				if((blkCarry == null) || (blkCarry.getId() == 0))
				{
					enderman.setCarriedBlock(blk);
					break;
				}
			}
			
			plr.sendMessage(ChatColor.GREEN + "An Enderman now carrying: BlkID=" + ChatColor.GOLD + blk.getId());
			return;
		}
		if(msg.startsWith("/ehead") && plr.isOp())
		{
			ei.isCancelled = true;
			for(MC_Entity ent : plr.getWorld().getEntities())
			{
				if(!(ent instanceof MC_Enderman)) continue;
				MC_Enderman enderman = (MC_Enderman)ent;
				MC_Block blk = enderman.getCarriedBlock();
				if(blk == null) continue;
				if(blk.getId() == 0) continue;
				MC_Location locAbove = enderman.getLocation().toBlockLocation();
				locAbove.y+=4;
				plr.getWorld().setBlockAt(locAbove, blk, blk.getSubtype());
			}
			
			plr.sendMessage(ChatColor.GREEN + "Carried blocks placed above Enderman heads");
			return;
		}
		// ==============================================
		
		if(msg.startsWith("/egg") && plr.isOp())
		{
			ei.isCancelled = true;
			int num = RainbowUtils.GetIntegerArgument(msg, 0);
			if(num <= 0)
			{
				plr.sendMessage(ChatColor.RED + "[ExamplePlugin] Usage: /egg # " + ChatColor.WHITE + " - Use 92 for a cow egg");
			}
			else
			{
				MC_ItemStack egg = server.createItemStack(383, 1, num);
				plr.setItemInHand(egg);
				plr.sendMessage(ChatColor.AQUA + "You have egg with sub-id: " + num);
			}
			return;
		}
		if(msg.startsWith("/setabsorb") && plr.isOp())
		{
			ei.isCancelled = true;
			int num = RainbowUtils.GetIntegerArgument(msg, 0);
			plr.setAbsorptionAmount(num);
			plr.sendMessage(ChatColor.GREEN + "Absorption Set to: " + ChatColor.AQUA + num);
			return;
		}

		if(msg.startsWith("/burn") && plr.isOp())
		{
			ei.isCancelled = true;
			int num = RainbowUtils.GetIntegerArgument(msg, 0);
			if(num <= 0)
			{
				plr.sendMessage(ChatColor.RED + "[ExamplePlugin] Usage: /burn # " + ChatColor.WHITE + " - Burn that many ticks");
			}
			else
			{
				plr.setFireTicks(num);
			}
			return;
		}

		if(msg.startsWith("/aspose") && plr.isOp())
		{
			ei.isCancelled = true;
			String[] args = msg.split("\\s+");
			int slotIdx = 0;
			float val1 = 0;
			float val2 = 0;
			float val3 = 0;
			try
			{
				slotIdx = Integer.parseInt(args[1]);
				val1 = Float.parseFloat(args[2]);
				val2 = Float.parseFloat(args[3]);
				val3 = Float.parseFloat(args[4]);
			}
			catch(Exception exc)
			{
				plr.sendMessage(ChatColor.RED + "Usage: /aspose Slot#(1-6) Value1 Value2 Value3");
				return;
			}
			slotIdx--;
			if((slotIdx < 0) || (slotIdx > 5)) {
				plr.sendMessage(ChatColor.RED + "Slot# must be between 1-6");
				return;
			}
			plr.sendMessage(ChatColor.AQUA + String.format("Using Slot %d, x=%.1f, y=%.1f, z=%.1f", slotIdx+1, val1, val2, val3));
			
			int nStands = 0;
			for(MC_Entity ent : plr.getWorld().getEntities())
			{
				if(!(ent instanceof MC_ArmorStand)) continue;
				MC_ArmorStand stand = (MC_ArmorStand)ent;
				List<MC_FloatTriplet> pose = stand.getPose();
				MC_FloatTriplet entry = pose.get(slotIdx);
				entry.x = val1;
				entry.y = val2;
				entry.z = val3;
				pose.set(slotIdx, entry);
				stand.setPose(pose);
				nStands++;
			}
			plr.sendMessage(ChatColor.GREEN + "Modified " + nStands + " Armor Stands!");
			return;
		}

		if(msg.equalsIgnoreCase("/asbaseonly") && plr.isOp())
		{
			ei.isCancelled = true;
			int nStands = 0;
			for(MC_Entity ent : plr.getWorld().getEntities())
			{
				if(!(ent instanceof MC_ArmorStand)) continue;
				MC_ArmorStand stand = (MC_ArmorStand)ent;
				if(stand.hasBase()) continue;
				stand.kill();
				nStands++;
			}
			plr.sendMessage(ChatColor.GREEN + "Killed " + nStands + " Armor Stands with no base!");
			return;
		}
		if(msg.equalsIgnoreCase("/asArmsOnly") && plr.isOp())
		{
			ei.isCancelled = true;
			int nStands = 0;
			for(MC_Entity ent : plr.getWorld().getEntities())
			{
				if(!(ent instanceof MC_ArmorStand)) continue;
				MC_ArmorStand stand = (MC_ArmorStand)ent;
				if(stand.hasArms()) continue;
				stand.kill();
				nStands++;
			}
			plr.sendMessage(ChatColor.GREEN + "Killed " + nStands + " Armor Stands with no arms!");
			return;
		}
		if(msg.equalsIgnoreCase("/asAllArms") && plr.isOp())
		{
			ei.isCancelled = true;
			int nStands = 0;
			for(MC_Entity ent : plr.getWorld().getEntities())
			{
				if(!(ent instanceof MC_ArmorStand)) continue;
				MC_ArmorStand stand = (MC_ArmorStand)ent;
				stand.setHasArms(true);
				nStands++;
			}
			plr.sendMessage(ChatColor.GREEN + "OK. " + nStands + " Armor Stands have arms!");
			return;
		}
		if(msg.equalsIgnoreCase("/asNoArms") && plr.isOp())
		{
			ei.isCancelled = true;
			int nStands = 0;
			for(MC_Entity ent : plr.getWorld().getEntities())
			{
				if(!(ent instanceof MC_ArmorStand)) continue;
				MC_ArmorStand stand = (MC_ArmorStand)ent;
				stand.setHasArms(false);
				nStands++;
			}
			plr.sendMessage(ChatColor.GREEN + "OK. " + nStands + " Armor Stands have no arms!");
			return;
		}
		if(msg.equalsIgnoreCase("/asAllBase") && plr.isOp())
		{
			ei.isCancelled = true;
			int nStands = 0;
			for(MC_Entity ent : plr.getWorld().getEntities())
			{
				if(!(ent instanceof MC_ArmorStand)) continue;
				MC_ArmorStand stand = (MC_ArmorStand)ent;
				stand.setHasBase(true);
				nStands++;
			}
			plr.sendMessage(ChatColor.GREEN + "OK. " + nStands + " Armor Stands have base plate!");
			return;
		}
		if(msg.equalsIgnoreCase("/asNoBase") && plr.isOp())
		{
			ei.isCancelled = true;
			int nStands = 0;
			for(MC_Entity ent : plr.getWorld().getEntities())
			{
				if(!(ent instanceof MC_ArmorStand)) continue;
				MC_ArmorStand stand = (MC_ArmorStand)ent;
				stand.setHasBase(false);
				nStands++;
			}
			plr.sendMessage(ChatColor.GREEN + "OK. " + nStands + " Armor Stands have no base plate!");
			return;
		}
		
		if(msg.startsWith("/shake") && plr.isOp())
		{
			ei.isCancelled = true;
			int num = RainbowUtils.GetIntegerArgument(msg, 0);
			if(num <= 0) num = 2;
			MC_MotionData data = plr.getMotionData();
			//data.xMotion = 5 + Math.random()*num;
			//data.yMotion = 10.0f; //Math.random() * num;
			//data.zMotion = 5 + Math.random()*num;
			data.xMotion = -num + Math.random()*(2*num);
			data.yMotion = 1.0f; //Math.random() * num;
			data.zMotion = -num + Math.random()*(num*2);
			
			plr.setMotionData(data);
			plr.sendMessage(ChatColor.AQUA + "You're all shook up!");
			return;
		}
		
		if(msg.equalsIgnoreCase("/getburn"))
		{
			ei.isCancelled = true;
			plr.sendMessage(ChatColor.AQUA + "Fire Ticks: " + ChatColor.WHITE + plr.getFireTicks());
			return;
		}
		if(msg.equalsIgnoreCase("/drop1"))
		{
			ei.isCancelled = true;
			MC_ItemStack is = server.createItemStack(2, 2, 0);
			plr.getWorld().dropItem(is, plr.getLocation(), plr.getName());
			plr.sendMessage(ChatColor.GREEN + "Ok - 2 grass");
			return;
		}
		if(msg.equalsIgnoreCase("/drop2"))
		{
			ei.isCancelled = true;
			MC_ItemStack is = server.createItemStack(1, 4, 1);
			plr.getWorld().dropItem(is, plr.getLocation(), null);
			plr.sendMessage(ChatColor.GREEN + "Ok. 4 stone");
			return;
		}

		if(msg.equalsIgnoreCase("/RemEffects") && plr.isOp())
		{
			ei.isCancelled = true;
			plr.setPotionEffects(null);
			plr.sendMessage(ChatColor.AQUA + "Removed your potion effects.");
			return;
		}
		if(msg.equalsIgnoreCase("/TestEffects") && plr.isOp())
		{
			ei.isCancelled = true;
			List<MC_PotionEffect> effects = plr.getPotionEffects();
			effects.add(new MC_PotionEffect(MC_PotionEffectType.NIGHT_VISION, 41*20, 2));
			effects.add(new MC_PotionEffect(MC_PotionEffectType.HASTE, 51*20, 2));
			plr.setPotionEffects(effects);
			
			plr.sendMessage(ChatColor.AQUA + "Gave you night vision!");
			return;
		}
		
		if(msg.equalsIgnoreCase("/fly") && plr.isOp())
		{
			ei.isCancelled = true;
			boolean newState = !plr.isAllowedFlight();
			plr.setAllowFlight(newState);
			plr.sendMessage(ChatColor.AQUA + "Allow Flying set to: " + ChatColor.WHITE + newState);
			return;
		}
		if(msg.equalsIgnoreCase("/setfly") && plr.isOp())
		{
			ei.isCancelled = true;
			boolean newState = !plr.isFlying();
			plr.setFlying(newState);
			plr.sendMessage(ChatColor.AQUA + "Flying set to: " + ChatColor.WHITE + newState);
			plr.sendMessage(ChatColor.DARK_AQUA + "- Try setting while falling as example.");
			return;
		}
		
		if(msg.startsWith("/flyspeed") && plr.isOp())
		{
			ei.isCancelled = true;
			int num = RainbowUtils.GetIntegerArgument(msg, 0);
			if(num <= 0)
			{
				plr.sendMessage(ChatColor.RED + "[ExamplePlugin] Usage: /flyspeed # " + ChatColor.WHITE + " - Use 1 for default");
			}
			else
			{
				// keep within reason...
				if(num > 10) num = 10;

				// Use multiple of default...
				float newSpeed = num * 0.05f;
				plr.setFlySpeed(newSpeed);
				plr.sendMessage(ChatColor.GREEN + String.format("Set fly speed to %d (speed value=%.2f)", num, newSpeed)); 
			}
			return;
		}
		if(msg.startsWith("/walkspeed") && plr.isOp())
		{
			ei.isCancelled = true;
			int num = RainbowUtils.GetIntegerArgument(msg, 0);
			if(num <= 0)
			{
				plr.sendMessage(ChatColor.RED + "[ExamplePlugin] Usage: /walkspeed # " + ChatColor.WHITE + " - Use 1 for default");
			}
			else
			{
				// keep within reason...
				if(num > 10) num = 10;
				
				// Use multiple of default...
				float newSpeed = num * 0.1F;
				plr.setWalkSpeed(newSpeed);
				plr.sendMessage(ChatColor.GREEN + String.format("Set walk speed to %d (speed value=%.2f)", num, newSpeed)); 
			}
			return;
		}

		if(msg.startsWith("/sethead"))
		{
			ei.isCancelled = true;
			
			MC_ItemStack is = plr.getItemInHand();
			if(is.getId() == 0)
			{
				plr.sendMessage(ChatColor.RED + "You must be holding something!");
				return;
			}
			
			String newName = "";
			int idxSpace = msg.indexOf(' ');
			if(idxSpace > 0) newName = msg.substring(idxSpace+1).trim();
			 
			if(newName.length() <= 0)
			{
				plr.sendMessage(ChatColor.RED + "Usage: /sethead NewName");
				return;
			}
			is.setSkullOwner(newName);
			plr.sendMessage(ChatColor.GREEN + "Set to: " + ChatColor.YELLOW + newName);
			return;
		}
		
		if(msg.startsWith("/setlore"))
		{
			ei.isCancelled = true;
			
			MC_ItemStack is = plr.getItemInHand();
			if(is.getId() == 0)
			{
				plr.sendMessage(ChatColor.RED + "You must be holding something!");
				return;
			}
			
			String newName = "";
			int idxSpace = msg.indexOf(' ');
			if(idxSpace > 0) newName = msg.substring(idxSpace+1).trim();
			 
			if(newName.length() <= 0)
			{
				plr.sendMessage(ChatColor.RED + "Usage: /setlore NewName");
				return;
			}
			ArrayList<String> lore = new ArrayList<String>();
			for(int i=1; i<=4; i++) lore.add(newName + " " + i);
			is.setLore(lore);
			plr.sendMessage(ChatColor.GREEN + "Lore Set!");
			return;
		}
		if(msg.equalsIgnoreCase("/clearlore"))
		{
			ei.isCancelled = true;
			
			MC_ItemStack is = plr.getItemInHand();
			if(is.getId() == 0)
			{
				plr.sendMessage(ChatColor.RED + "You must be holding something!");
				return;
			}

			is.setLore(null);
			plr.sendMessage(ChatColor.GREEN + "Lore Cleared!");
			return;
		}
		if(msg.equalsIgnoreCase("/getlore"))
		{
			ei.isCancelled = true;
			
			MC_ItemStack is = plr.getItemInHand();
			if(is.getId() == 0)
			{
				plr.sendMessage(ChatColor.RED + "You must be holding something!");
				return;
			}
			List<String> lore = is.getLore();
			if(lore == null) plr.sendMessage(ChatColor.GREEN + "No Lore Set");
			else
			{
				int nLore = lore.size();
				plr.sendMessage(ChatColor.WHITE+ "Num Lore: " + nLore);
				for(int i=0; i<nLore; i++) plr.sendMessage(ChatColor.AQUA + "Lore: " + ChatColor.LIGHT_PURPLE + lore.get(i));
			}
			return;
		}
		
		
		if(msg.startsWith("/nameitem"))
		{
			ei.isCancelled = true;
			
			MC_ItemStack is = plr.getItemInHand();
			if(is.getId() == 0)
			{
				plr.sendMessage(ChatColor.RED + "You must be holding something!");
				return;
			}
			
			String newName = "";
			int idxSpace = msg.indexOf(' ');
			if(idxSpace > 0) newName = msg.substring(idxSpace+1).trim();
			 
			if(newName.length() <= 0)
			{
				plr.sendMessage(ChatColor.RED + "Usage: /nameitem NewName");
				return;
			}
			newName = RainbowUtils.RainbowString(newName, "r");
			is.setCustomName(newName);
			plr.sendMessage(ChatColor.GREEN + "Set Item Name To: " + ChatColor.WHITE + newName);
			
			return;
		}
		if(msg.equalsIgnoreCase("/clearname"))
		{
			ei.isCancelled = true;
			
			MC_ItemStack is = plr.getItemInHand();
			if(is.getId() == 0)
			{
				plr.sendMessage(ChatColor.RED + "You must be holding something!");
				return;
			}
			if(!is.hasCustomName())
			{
				plr.sendMessage(ChatColor.GREEN + "This item has no custom name.");
			}
			else
			{
				is.removeCustomName();
				plr.sendMessage(ChatColor.GREEN + "Custom Item Name Cleared.");
			}
			return;
		}
		if(msg.startsWith("/carry"))
		{
			ei.isCancelled = true;
			
			int idxSpace = msg.indexOf(' ');
			if(idxSpace < 0)
			{
				plr.sendMessage(ChatColor.RED + "Usage: /carry PlayerName");
				return;
			}
			String plrName = msg.substring(idxSpace+1);
			MC_Player tgtPlr = server.getOnlinePlayerByName(plrName);
			if(tgtPlr == null)
			{
				plr.sendMessage(ChatColor.RED + "Player not found: " + ChatColor.YELLOW + plrName);
				return;
			}
			plr.setRider(tgtPlr);
			plr.sendMessage(ChatColor.GREEN + "You now have rider: " + ChatColor.YELLOW + tgtPlr.getName());
			return;
		}
		if(msg.startsWith("/ride"))
		{
			ei.isCancelled = true;
			
			int idxSpace = msg.indexOf(' ');
			if(idxSpace < 0)
			{
				plr.sendMessage(ChatColor.RED + "Usage: /ride PlayerName");
				return;
			}
			String plrName = msg.substring(idxSpace+1);
			MC_Player tgtPlr = server.getOnlinePlayerByName(plrName);
			if(tgtPlr == null)
			{
				plr.sendMessage(ChatColor.RED + "Player not found: " + ChatColor.YELLOW + plrName);
				return;
			}
			plr.setVehicle(tgtPlr);
			plr.sendMessage(ChatColor.GREEN + "You now have vehicle: " + ChatColor.YELLOW + tgtPlr.getName());
			return;
		}
		
		
		if(msg.startsWith("/testspawn"))
		{
			ei.isCancelled = true;
			String[] args = msg.split("\\s+");
			if(args.length == 1)
			{
				plr.sendMessage(ChatColor.AQUA + "Usage: /testspawn EntType [CustomName]");
				return;
			}
			String customName = null;
			if(args.length > 2) customName = RainbowUtils.ConcatArgs(args, 2);
			
			boolean didSpawn = false;
		    for (MC_EntityType entType : MC_EntityType.values()) 
		    {
		    	if(entType.toString().equalsIgnoreCase(args[1]))
		    	{
		    		didSpawn = true;
		    		plr.getWorld().spawnEntity(entType, plr.getLocation(), customName);
		    		break;
		    	}
		    }
			
		    if(didSpawn)
		    {
		    	String plrMsg = ChatColor.AQUA + "Spawning " + ChatColor.GOLD + "Type";
		    	if(customName != null) plrMsg += ChatColor.WHITE + " named " + ChatColor.YELLOW + customName;
		    	plr.sendMessage(plrMsg);
		    }
		    else
		    {
		    	plr.sendMessage(ChatColor.RED + "Unknown EntityType: " + ChatColor.AQUA + args[1]);
		    }
			
			return;
		}
		
		if(msg.equalsIgnoreCase("/gmc") && plr.isOp())
		{
			ei.isCancelled = true;
			plr.setGameMode(MC_GameMode.CREATIVE);
			return;
		}
		if(msg.equalsIgnoreCase("/gms"))
		{
			ei.isCancelled = true;
			plr.setGameMode(MC_GameMode.SURVIVAL);
			return;
		}
		
		if(msg.equalsIgnoreCase("/hat"))
		{
			ei.isCancelled = true;
			MC_ItemStack isToWear = plr.getItemInHand();
			if(isToWear.getId() == 0)
			{
				plr.sendMessage(ChatColor.RED + "You must be holding something!");
				return;
			}
			// Set head slot to what's in hand
			List armor = plr.getArmor();
			armor.set(3, isToWear); // 3=hat, 2=chest, 1=legs, 0=boots
			plr.setArmor(armor);
			
			// Set hand to empty
			plr.setItemInHand(null);
			plr.updateInventory();
			
			plr.sendMessage(ChatColor.GREEN + "You are now wearing: " + ChatColor.AQUA + isToWear.getFriendlyName());
			
			return;
		}
		
		if(msg.startsWith("/cat "))
		{
			int num = RainbowUtils.GetIntegerArgument(msg, 0);
			if(num < 0) num = 0;
			
    		MC_Ocelot cat = (MC_Ocelot)plr.getWorld().spawnEntity(MC_EntityType.OCELOT, plr.getLocation(), RainbowUtils.RainbowString("Spawn Kitty"));
    		cat.setTamed(true);
    		if(num == 1) cat.setCatType(MC_OcelotType.BLACK);
    		if(num == 2) cat.setCatType(MC_OcelotType.RED);
    		if(num == 3) cat.setCatType(MC_OcelotType.SIAMESE);
    		
    		cat.setOwner(plr);
    		cat.showLoveHateEffect(true);
    		
			ei.isCancelled = true;
			return;
		}
		
		
		if(msg.startsWith("/blockname"))
		{
			int num = RainbowUtils.GetIntegerArgument(msg, 0);
			if(num <= 0)
			{
				plr.sendMessage(ChatColor.RED + "[ExamplePlugin] Usage: /blockname #");
			}
			else
			{			
				String name = BlockHelper.getBlockName(num);
				plr.sendMessage(ChatColor.AQUA + "BlockName for ID " + num + ": " + ChatColor.GOLD + name);
			}
			
			ei.isCancelled = true;
			return;
		}

		if(msg.startsWith("/nearvis"))
		{
			ei.isCancelled = true;

			int radius = RainbowUtils.GetIntegerArgument(msg, 10);
			plr.sendMessage(ChatColor.AQUA + "Using Radius: " + ChatColor.GOLD + radius);

			MC_World world = plr.getWorld();
			MC_Location loc = plr.getLocation();
			for(MC_Entity ent : world.getEntities())
			{
				if(ent.getLocation().distanceTo(loc) <= radius)
				{
					plr.sendMessage(ChatColor.AQUA + "Making Visible: " + ChatColor.WHITE + ent.getType() + ": " + ChatColor.YELLOW + ent.getName());
					ent.setInvisible(false);
				}
			}
			return;
		}

		if(msg.startsWith("/nearinvis"))
		{
			ei.isCancelled = true;

			int radius = RainbowUtils.GetIntegerArgument(msg, 10);
			plr.sendMessage(ChatColor.AQUA + "Using Radius: " + ChatColor.GOLD + radius);

			MC_World world = plr.getWorld();
			MC_Location loc = plr.getLocation();
			for(MC_Entity ent : world.getEntities())
			{
				if(ent.getLocation().distanceTo(loc) <= radius)
				{
					plr.sendMessage(ChatColor.AQUA + "Making Invisible: " + ChatColor.WHITE + ent.getType() + ": " + ChatColor.YELLOW + ent.getName());
					ent.setInvisible(true);
				}
			}
			return;
		}
		
		if(msg.equalsIgnoreCase("/enchants") )
		{
			ei.isCancelled = true;
			MC_ItemStack isHand = plr.getItemInHand();
			if(isHand.getId() == 0)
			{
				plr.sendMessage(ChatColor.RED + "You must be holding something!");
				return;
			}
			List<MC_Enchantment> enchants = isHand.getEnchantments();
			if(enchants.size() > 0) plr.sendMessage(ChatColor.WHITE + "-------------------------");
			for(MC_Enchantment enchant : enchants)
			{
				plr.sendMessage(ChatColor.AQUA + enchant.type.toString() + ": " + ChatColor.WHITE + enchant.level);
			}
			plr.sendMessage(ChatColor.GREEN + "Listed " + enchants.size() + " enchantments."); 
			
			return;
		}
		if(msg.equalsIgnoreCase("/enchantcopy") && plr.isOp())
		{
			ei.isCancelled = true;
			MC_ItemStack isHand = plr.getItemInHand();
			if(isHand.getId() == 0)
			{
				plr.sendMessage(ChatColor.RED + "You must be holding something!");
				return;
			}
			List<MC_Enchantment> enchants = isHand.getEnchantments();
			
			hashEnchantStore.put(plr.getName(), enchants);
			plr.sendMessage(ChatColor.GREEN + "Copied " + enchants.size() + " enchantments to clipboard."); 
			return;
		}
		if(msg.equalsIgnoreCase("/enchantpaste") && plr.isOp())
		{
			ei.isCancelled = true;
			MC_ItemStack isHand = plr.getItemInHand();
			if(isHand.getId() == 0)
			{
				plr.sendMessage(ChatColor.RED + "You must be holding something!");
				return;
			}
			List<MC_Enchantment> enchants = hashEnchantStore.get(plr.getName());
			if(enchants == null)
			{
				plr.sendMessage(ChatColor.RED + "You haven't used /enchantcopy yet.");
				return;
			}

			isHand.setEnchantments(enchants);
			plr.updateInventory();
			plr.sendMessage(ChatColor.GREEN + "Pasted " + enchants.size() + " enchantments to clipboard."); 
			return;
		}
		if(msg.equalsIgnoreCase("/enchant10") && plr.isOp())
		{
			ei.isCancelled = true;
			MC_ItemStack isHand = plr.getItemInHand();
			if(isHand.getId() == 0)
			{
				plr.sendMessage(ChatColor.RED + "You must be holding something!");
				return;
			}
			isHand.setEnchantmentLevel(MC_EnchantmentType.SHARPNESS, 10);
			plr.updateInventory();
			plr.sendMessage(ChatColor.GREEN + "Set an enchantment!");
			return;
		}
		
		
		// Check for /jump
		if(msg.startsWith("/jump") && plr.isOp())
		{
			ei.isCancelled = true;
			int num = RainbowUtils.GetIntegerArgument(msg, 0);
			if(num <= 0)
			{
				plr.sendMessage(ChatColor.RED + "[ExamplePlugin] Usage: /jump #");
			}
			else
			{			
				MC_Location loc = plr.getLocation();
				loc.y += num;
				plr.teleport(loc);

				plr.sendMessage(ChatColor.AQUA + "[ExamplePlugin] " + ChatColor.GREEN + "Jumped " + num + " blocks!");
			}
			return;
		}
		// Check for /jump
		if(msg.startsWith("/hungerbonus") && plr.isOp())
		{
			ei.isCancelled = true;
			int num = RainbowUtils.GetIntegerArgument(msg, 0);
			plr.setFoodRegenAmount(num);
			plr.sendMessage(ChatColor.AQUA + "[ExamplePlugin] food Bonus Set To: " + num);
			return;
		}

		if(msg.startsWith("/nearents") && plr.isOp())
		{
			ei.isCancelled = true;
			int radius = RainbowUtils.GetIntegerArgument(msg, 0);
			List<MC_Entity> ents = plr.getNearbyEntities(radius);
			int maxIter = ents.size();
			if(maxIter > 10) maxIter = 10;
			for(int i=0; i<ents.size(); i++)
			{
				MC_Entity ent = ents.get(i);
				plr.sendMessage(ChatColor.GOLD + "Found: " + ChatColor.YELLOW + ent.getName() + ChatColor.WHITE + " @ " + ent.getLocation().toString());
			}
			plr.sendMessage(ChatColor.GREEN + "Done. Listed Nearby Entities, radius: " + radius);
			return;
		}


		if(msg.startsWith("/logmsg") && plr.isOp())
		{
			ei.isCancelled = true;
			server.log("--- Log Example --- : " + msg);
			plr.sendMessage(ChatColor.GREEN + "Logged: " + ChatColor.AQUA + msg);
			return;
		}
		
		
		// Other miscellaneous examples...
		if(msg.equalsIgnoreCase("/kit food"))
		{
			ei.isCancelled = true;
			
			if(MiscUtils.TooSoon(plr, "KitFood", 60*5)) return;
			
			plr.getServer().executeCommand("/give " + plr.getName() + " apple 4");
			plr.sendMessage(ChatColor.AQUA + "[ExamplePlugin] " + ChatColor.GREEN + "Received Kit Food");
			return;
		}
		if(msg.equalsIgnoreCase("/iteminfo"))
		{
			ei.isCancelled = true;
			MC_ItemStack is = plr.getItemInHand();
			if((is == null) || (is.getId() == 0))
			{
				plr.sendMessage(ChatColor.RED + "Nothing in your hand");
				return;
			}
			int len = 16;
			plr.sendMessage(ChatColor.DARK_GRAY + "--------------------------");
			plr.sendMessage(ChatColor.AQUA + RainbowUtils.TextLabel("ID", len)  + ChatColor.WHITE + String.format("%d", is.getId()));
			plr.sendMessage(ChatColor.AQUA + RainbowUtils.TextLabel("Dmg/Subtype", len)  + ChatColor.WHITE + String.format("%d", is.getDamage()));
			plr.sendMessage(ChatColor.AQUA + RainbowUtils.TextLabel("Lore", len)  + ChatColor.WHITE + RainbowUtils.GetCommaList(is.getLore()));
			plr.sendMessage(ChatColor.AQUA + RainbowUtils.TextLabel("Max Stack", len)  + ChatColor.WHITE + is.getMaxStackSize());
			plr.sendMessage(ChatColor.AQUA + RainbowUtils.TextLabel("Friendly Name", len)  + ChatColor.WHITE + is.getFriendlyName());
			plr.sendMessage(ChatColor.AQUA + RainbowUtils.TextLabel("Official Name", len)  + ChatColor.WHITE + is.getOfficialName());
			String customName = is.getCustomizedName();
			if(customName == null) customName = ChatColor.RED + "Not Customized";
			plr.sendMessage(ChatColor.AQUA + RainbowUtils.TextLabel("Custom Name", len)  + ChatColor.WHITE + customName);
			return;
		}		
		if(msg.equalsIgnoreCase("/info"))
		{
			ei.isCancelled = true;
			int len = 16;
			plr.sendMessage(ChatColor.AQUA + RainbowUtils.TextLabel("Health", len)  + ChatColor.WHITE + String.format("%.2f", plr.getHealth()));
			plr.sendMessage(ChatColor.AQUA + RainbowUtils.TextLabel("Location", len) + ChatColor.WHITE + plr.getLocation().toString());
			plr.sendMessage(ChatColor.AQUA + RainbowUtils.TextLabel("Balance", len) + ChatColor.WHITE + String.format("%.2f", plr.getEconomyBalance()));
			plr.sendMessage(ChatColor.AQUA + RainbowUtils.TextLabel("IP", len) + ChatColor.WHITE + plr.getIPAddress());
			plr.sendMessage(ChatColor.AQUA + RainbowUtils.TextLabel("Server Port", len) + ChatColor.WHITE + server.getServerPort());
			plr.sendMessage(ChatColor.AQUA + RainbowUtils.TextLabel("Spawn Radius", len) + ChatColor.WHITE + server.getSpawnProtectionRadius());
			MC_ItemStack is = plr.getItemInHand();
			plr.sendMessage(ChatColor.AQUA + RainbowUtils.TextLabel("Item In Hand", len) + ChatColor.WHITE + "ID=" + is.getId() + " - " + ChatColor.YELLOW + is.getFriendlyName());
			plr.sendMessage(ChatColor.AQUA + RainbowUtils.TextLabel("Motion Data", len) + ChatColor.WHITE + plr.getMotionData().toString());
			plr.sendMessage(ChatColor.AQUA + RainbowUtils.TextLabel("EXP", len) + ChatColor.WHITE + String.format("XP=%.2f, LVL=%d, TOT=%d", plr.getExp(), plr.getLevel(), plr.getTotalExperience()));
		
			plr.sendMessage(ChatColor.AQUA + RainbowUtils.TextLabel("Absorbtion", len) + ChatColor.WHITE + String.format("%.2f", plr.getAbsorptionAmount()));
			plr.sendMessage(ChatColor.AQUA + RainbowUtils.TextLabel("50 Dmg (After Armor)", len) + ChatColor.WHITE + String.format("%.2f", plr.getArmorAdjustedDamage(MC_DamageType.GENERIC, 50)));
			plr.sendMessage(ChatColor.AQUA + RainbowUtils.TextLabel("50 Dmg (Simulation)", len) + ChatColor.WHITE + String.format("%.2f", plr.getTotalAdjustedDamage(MC_DamageType.GENERIC, 50)));
			plr.sendMessage(ChatColor.AQUA + RainbowUtils.TextLabel("Base Armor Value", len) + ChatColor.WHITE + String.format("%d", plr.getBaseArmorScore()));
			
			
			return;
		}
		if(msg.equalsIgnoreCase("/infodmg"))
		{
			ei.isCancelled = true;
			
			int len = 16;
			plr.sendMessage(ChatColor.LIGHT_PURPLE + "50 Damage: Armor Adjustment Results");
			for(MC_DamageType dmgType : MC_DamageType.values())
			{
				plr.sendMessage(ChatColor.AQUA + RainbowUtils.TextLabel(dmgType.name(), len) + ChatColor.WHITE + String.format("JustArmor=%.2f, Total=%.2f", plr.getArmorAdjustedDamage(dmgType, 50), plr.getTotalAdjustedDamage(dmgType, 50)));
			}
			
			return;
		}
		
		// Test XP functions...
		if(msg.equalsIgnoreCase("/xp100"))
		{
			ei.isCancelled = true;
			plr.giveExp(100);
			plr.sendMessage(ChatColor.GREEN + "OK!");
			return;
		}
		if(msg.equalsIgnoreCase("/xpset"))
		{
			ei.isCancelled = true;
			plr.setExp(0.5f);
			plr.sendMessage(ChatColor.GREEN + "OK!");
			return;
		}
		if(msg.equalsIgnoreCase("/xptotal1000"))
		{
			ei.isCancelled = true;
			plr.setTotalExperience(1000);
			plr.sendMessage(ChatColor.GREEN + "OK!");
			return;
		}
		
		
		if(msg.equalsIgnoreCase("/hero") && plr.isOp())
		{
			plr.setInvulnerable(!plr.isInvulnerable());
			if(plr.isInvulnerable()) plr.sendMessage(ChatColor.GREEN + "You are now invulnerable.");
			else plr.sendMessage(ChatColor.DARK_GREEN + "You are no longer invulnerable.");
			
			ei.isCancelled = true;
			return;
		}		
		
		if(msg.equalsIgnoreCase("/hurt"))
		{
			ei.isCancelled = true;
			plr.setHealth(plr.getHealth()/2);
			plr.sendMessage(ChatColor.GREEN + "Health cut in half!");
			return;
		}
		if(msg.equalsIgnoreCase("/heal") && plr.isOp())
		{
			ei.isCancelled = true;
			plr.setHealth(20);
			plr.sendMessage(ChatColor.GREEN + "Health set to 20");
			return;
		}
		if(msg.equalsIgnoreCase("/hungry"))
		{
			plr.getServer().executeCommand("/effect " + plr.getName() + " 17 10 10");
			plr.sendMessage(ChatColor.GREEN + "Giving Hunger (if not at spawn). You'll be hungry soon!");
			ei.isCancelled = true;
			return;
		}
		if(msg.equalsIgnoreCase("/hungry2"))
		{
			ei.isCancelled = true;
			plr.setFoodLevel(plr.getFoodLevel()/2);
			plr.sendMessage(ChatColor.GREEN + "Food Level cut in half!");
			return;
		}
		if(msg.equalsIgnoreCase("/feed") && plr.isOp())
		{
			ei.isCancelled = true;
			plr.setFoodLevel(20);
			plr.sendMessage(ChatColor.GREEN + "You are fed!");
			return;
		}
		if(msg.equalsIgnoreCase("/stalkme") && plr.isOp())
		{
			ei.isCancelled = true;
			MC_Location srcLoc = plr.getLocation();
			for(MC_Player tgt : server.getPlayers())
			{
				MC_Location tgtLoc = tgt.getLocation();
				
				tgtLoc.yaw = RainbowUtils.YawToFaceLocation(tgtLoc, srcLoc);
				tgtLoc.pitch = RainbowUtils.PitchToFaceLocation(tgtLoc, srcLoc);
				
				tgt.teleport(tgtLoc);
				tgt.sendMessage(ChatColor.LIGHT_PURPLE + "You turn to face " + ChatColor.RED + plr.getName());
			}
			plr.sendMessage(ChatColor.GREEN + "Everyone was forced to look at you!");
			return;
		}
		
		if(msg.equalsIgnoreCase("/kit op"))
		{
			if(plr.isOp())
			{
				plr.executeCommand("/give " + plr.getName() + " diamond_boots 1 0 {ench:[{id:0,lvl:4},{id:1,lvl:4},{id:2,lvl:4},{id:3,lvl:4}]}");
				plr.executeCommand("/give " + plr.getName() + " diamond_leggings 1 0 {ench:[{id:0,lvl:4},{id:1,lvl:4},{id:2,lvl:4},{id:3,lvl:4}]}");
				plr.executeCommand("/give " + plr.getName() + " diamond_sword 1 0 {ench:[{id:16,lvl:5},{id:20,lvl:2}]}");
				plr.executeCommand("/give " + plr.getName() + " diamond_helmet 1 0 {ench:[{id:0,lvl:4},{id:1,lvl:4},{id:3,lvl:4},{id:4,lvl:4},{id:5,lvl:3},{id:6,lvl:1}]}");
				plr.executeCommand("/give " + plr.getName() + " diamond_chestplate 1 0 {ench:[{id:0,lvl:4},{id:1,lvl:4},{id:3,lvl:4},{id:4,lvl:4}]}");
				plr.executeCommand("/give " + plr.getName() + " bow 1 0 {ench:[{id:51,lvl:1},{id:50,lvl:1},{id:49,lvl:2},{id:48,lvl:5}]}");
				plr.executeCommand("/give " + plr.getName() + " arrow 64");
				plr.sendMessage(ChatColor.AQUA + "[ExamplePlugin] " + ChatColor.GREEN + "Received Kit 1");
			}
			else
			{
				plr.sendMessage(ChatColor.RED + "[ExamplePlugin] Op Only!");
			}
			
			ei.isCancelled = true;
			return;
		}

		
		if(msg.equalsIgnoreCase("/items"))
		{
			List<? extends MC_ItemStack> items = plr.getInventory();
			for(int idx=0; idx<items.size(); idx++)
			{
				MC_ItemStack is = items.get(idx);
				plr.sendMessage( String.format("Slot %2d, ID %3d, DMG %3d, CNT %2d: %s", idx, is.getId(), is.getDamage(), is.getCount(), is.getFriendlyName()) );
			}
			ei.isCancelled = true;
			return;
		}
		if(msg.equalsIgnoreCase("/more") && plr.isOp())
		{
			plr.getItemInHand().setCount(64);
			plr.sendMessage(ChatColor.GREEN + "Ok! Item in hand is now stack of 64");
			ei.isCancelled = true;
			return;
		}
		if(msg.equalsIgnoreCase("/moreall") && plr.isOp())
		{
			List<? extends MC_ItemStack> items = plr.getInventory();
			for(int idx=0; idx<items.size(); idx++)
			{
				MC_ItemStack is = items.get(idx);
				is.setCount(64);
			}
			plr.updateInventory();
			plr.sendMessage(ChatColor.GREEN + "Ok! You have 64 of everything now!");
			ei.isCancelled = true;
			return;
		}
		if(msg.equalsIgnoreCase("/shuffle"))
		{
			// Get inventory
			List items = plr.getInventory();
			// Shuffle it...
			for(int idx=0; idx<items.size(); idx++)
			{
				int idx2 = (int)(Math.random() * items.size());
				if(idx2 == idx) continue;
				Object is1 = items.get(idx);
				items.set(idx, items.get(idx2));
				items.set(idx2, is1);
			}
			// Set back
			plr.setInventory(items);
			// Refresh
			plr.updateInventory();
			
			plr.sendMessage(ChatColor.GREEN + "Ok! Your Inventory shuffled!");
			ei.isCancelled = true;
			return;
		}
		if(msg.startsWith("/sedit") && plr.isOp())
		{
			ei.isCancelled = true;
			if(msg.equalsIgnoreCase("/sedit test"))
			{
				MC_World world = plr.getWorld();
				
				MC_Location loc = plr.getLocation();
				int x = loc.getBlockX();
				int y = loc.getBlockY();
				int z = loc.getBlockZ();
				
				MC_Block blk = world.getBlockFromName("wool");
				for(int i=0; i<16; i++) 
				{
					world.setBlockAt(x+2,  y+i, z+2, blk, i);
					world.setBlockAt(x+2,  y+i, z-2, blk, i);
					world.setBlockAt(x-2,  y+i, z-2, blk, i);
					world.setBlockAt(x-2,  y+i, z+2, blk, i);
				}
				
				plr.sendMessage(ChatColor.GREEN + "Ok! Set wool blocks near you");
				return;
			}
			if(msg.equalsIgnoreCase("/sedit crazy"))
			{
				MC_World world = plr.getWorld();
				
				MC_Location loc = plr.getLocation();
				int x = loc.getBlockX()+1;
				int y = loc.getBlockY();
				int z = loc.getBlockZ()+1;
				
				int dx = -10;
				int dy = 0;
				for(Integer key : BlockHelper.mapBlockNames.keySet())
				{
					String blkName = BlockHelper.mapBlockNames.get(key);
					MC_Block blk = world.getBlockFromName(blkName);
					if(!blk.isSolid()) continue;
					
					Integer numSubs = BlockHelper.mapNumSubtypes.get(key);
					if((numSubs == null) || (numSubs < 1))  numSubs = 1;
					for(int i=0; i<numSubs; i++)
					{
						world.setBlockAt(x+dx,  y+dy, z, blk, i);
						if(dx < 10) dx++;
						else
						{
							dx = -10;
							dy++;
						}
					}
				}
				plr.sendMessage(ChatColor.GREEN + "Ok! Set one of everything!");
				return;
			}
			
			//mapBlockNames			
			if(msg.equalsIgnoreCase("/sedit pos1"))
			{
				WorldEditSample.SetPosition1(plr, plr.getLocation());
				plr.sendMessage(ChatColor.GREEN + "Set Position 1 To: " + ChatColor.WHITE + plr.getLocation().toString());
				return;
			}
			if(msg.equalsIgnoreCase("/sedit pos2"))
			{
				WorldEditSample.SetPosition2(plr, plr.getLocation());
				plr.sendMessage(ChatColor.GREEN + "Set Position 2 To: " + ChatColor.WHITE + plr.getLocation().toString());
				return;
			}
			if(msg.startsWith("/sedit set "))
			{
				String blockName = msg.substring(10);
				WorldEditSample.SetToBlock(plr, blockName);
				return;
			}
			if(msg.startsWith("/sedit walls "))
			{
				String blockName = msg.substring(12);
				WorldEditSample.WallsBlock(plr, blockName);
				return;
			}
			
			return;
		} // end of "/edit" command check
		
		if(msg.startsWith("/boom") && plr.isOp())
		{
			ei.isCancelled = true;
			int numBlocks = RainbowUtils.GetIntegerArgument(msg, 0);
					
			if(numBlocks <= 0) numBlocks = 1;
			if(numBlocks >= 10) numBlocks = 10;
			
			MC_World world = plr.getWorld();
			MC_Location loc = plr.getLocation();
			int x = loc.getBlockX();
			int y = loc.getBlockY();
			int z = loc.getBlockZ();
			for(int dx=-numBlocks; dx<numBlocks; dx++)
			{
				for(int dy=-numBlocks; dy<numBlocks; dy++)
				{
					for(int dz=-numBlocks; dz<numBlocks; dz++)
					{
						world.breakNaturallyAt(x+dx, y+dy, z+dz, plr.getItemInHand());
					}
				}
			}
			plr.sendMessage(ChatColor.GREEN + "Explosion Complete! "  + ChatColor.AQUA + "Radius: " + numBlocks);
			return;
		}
		
		
	} // end of method: onPlayerInput()
	

	public void onInteracted(MC_Player plr, MC_Location loc, MC_ItemStack isHandItem)
	{
		if(isHandItem.getId() == 280)
		{
			plr.setCompassTarget(loc);
		}
	}
	
	// Event handler: onTick...
	static long lastProcessedMS = 0;
	public ConcurrentHashMap<String, String> lastInfoMap = new ConcurrentHashMap<String, String>(); 
	public void onTick(int tickNumber)
	{
		long ms = System.currentTimeMillis();
		if(ms - lastProcessedMS < 500) return;
		lastProcessedMS = ms;
		
		/*
		// Armor stand test...
		// -----------------------------------------------------------
		for(MC_Player plr : server.getPlayers())
		{
			List<MC_Entity> near = plr.getNearbyEntities(10.0f);
			
			for(MC_Entity ent : near)
			{
				if(!(ent instanceof MC_ArmorStand)) continue;
				MC_ArmorStand stand = (MC_ArmorStand)ent;
				List<MC_FloatTriplet> pose = stand.getPose();
				
				MC_Location locPlr = plr.getLocation();
				MC_Location locEnt = stand.getLocation();
				MC_FloatTriplet entry = pose.get(0);
				entry.y = RainbowUtils.YawToFaceLocation(locEnt, locPlr) - 45;
				pose.set(0, entry);
				stand.setPose(pose);
			}
		}
		// -----------------------------------------------------------
		*/
		
		
		// Check each player...
		for(MC_Player plr : server.getPlayers())
		{
			// Are they on a sponge?
			MC_World world = plr.getWorld();
			MC_Location loc = plr.getLocation();
			
			int x = (int)Math.floor(loc.x); //loc.getBlockX();
			int y = loc.getBlockY()-1;
			int z = (int)Math.floor(loc.z); //loc.getBlockZ();
			
			MC_Block blk = world.getBlockAt(x,  y, z);

			int id = blk.getId();
			if(id == MC_BlockType.SPONGE)
			{
				// Let them know...
				plr.sendMessage(ChatColor.GREEN + "You're on a sponge!");
			}
			else
			{
				//plr.sendMessage(ChatColor.DARK_AQUA + String.format("Block Under You: " + ChatColor.WHITE + "ID=%d, Subtype=%d", blk.getId(), blk.getSubtype()));
			}
			
			// Show player status info...
			ArrayList<String> info = new ArrayList<String>(); 
			if(plr.isDead()) info.add("Dead");
			if(plr.isSleeping()) info.add("Sleeping");
			if(plr.isSneaking()) info.add("Sneaking");
			if(plr.isSprinting()) info.add("Sprinting");
			if(plr.isInvisible()) info.add("Invisible");
			if(plr.isInvulnerable()) info.add("Invulnerable");
			MC_Entity rider = plr.getRider();
			if(rider != null) info.add("Carrying " + rider.getType() + ": " + rider.getName());
			MC_Entity vehicle = plr.getVehicle();
			if(vehicle != null) info.add("Vehicle " + vehicle.getType() + ": " + vehicle.getName());
			info.add("Gamemode: " + plr.getGameMode());
			if(info.size() > 0)
			{
				String infoMsg = RainbowUtils.GetCommaList(info);
				String lastMsg = lastInfoMap.get(plr.getName());
				if((lastMsg == null) || !lastMsg.equalsIgnoreCase(infoMsg))
				{
					plr.sendMessage(ChatColor.DARK_AQUA + "You are: " + ChatColor.WHITE + infoMsg);
					lastInfoMap.put(plr.getName(), infoMsg);
				}
			}
			
			MC_Location lastTarget = hashCompassTarget.get(plr.getName());
			MC_Location curTarget = plr.getCompassTarget();
			if(curTarget != null)
			{
				if((lastTarget != null) && lastTarget.equals(curTarget))
				{
					// skip if same
				}
				else
				{
					hashCompassTarget.put(plr.getName(), curTarget);
					plr.sendMessage(ChatColor.LIGHT_PURPLE + "Compass Target Changed: " + ChatColor.WHITE + curTarget.toString());
				}
			}
			
		}
	} // end of method: onTick()...


	
} // end of class: MyPlugin 
