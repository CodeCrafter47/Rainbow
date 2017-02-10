package PluginExample;

import PluginExample.commands.*;
import PluginReference.ChatColor;
import PluginReference.MC_Block;
import PluginReference.MC_BlockType;
import PluginReference.MC_Entity;
import PluginReference.MC_ItemStack;
import PluginReference.MC_Location;
import PluginReference.MC_Player;
import PluginReference.MC_Server;
import PluginReference.MC_World;
import PluginReference.PluginBase;
import PluginReference.PluginInfo;
import PluginReference.RainbowUtils;
import net.md_5.bungee.config.Configuration;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class MyPlugin extends PluginBase {
    public static MC_Server server = null;

    public static ConcurrentHashMap<String, MC_Location> hashCompassTarget = new ConcurrentHashMap<String, MC_Location>();

    public void onStartup(MC_Server argServer) {
        System.out.println("======= ExamplePlugin --- Startup! =======");
        server = argServer;

        Configuration config = getConfig(true);

        // Set a custom shutdown message.
        server.setCustomShutdownMessage(config.getString("customShutdownMessage"));

        server.registerCommand(new CmdArrows());
        server.registerCommand(new CmdASAllArms());
        server.registerCommand(new CmdASAllBase());
        server.registerCommand(new CmdAsArmsOnly());
        server.registerCommand(new CmdAsBaseOnly());
        server.registerCommand(new CmdASNoArms());
        server.registerCommand(new CmdASNoBase());
        server.registerCommand(new CmdASPose());
        server.registerCommand(new CmdBiome());
        server.registerCommand(new CmdBurn());
        server.registerCommand(new CmdCarry());
        server.registerCommand(new CmdCat());
        server.registerCommand(new CmdChunkTest1());
        server.registerCommand(new CmdChunkTest2());
        server.registerCommand(new CmdChunkTest3());
        server.registerCommand(new CmdClearItemName());
        server.registerCommand(new CmdClearLore());
        server.registerCommand(new CmdDrop1());
        server.registerCommand(new CmdDrop2());
        server.registerCommand(new CmdECarry());
        server.registerCommand(new CmdEgg());
        server.registerCommand(new CmdEHead());
        server.registerCommand(new CmdEnchants());
        server.registerCommand(new CmdFly());
        server.registerCommand(new CmdFlySpeed());
        server.registerCommand(new CmdGetBurn());
        server.registerCommand(new CmdGetLore());
        server.registerCommand(new CmdHat());
        server.registerCommand(new CmdHead());
        server.registerCommand(new CmdHeal());
        server.registerCommand(new CmdHero());
        server.registerCommand(new CmdInfo());
        server.registerCommand(new CmdItemInfo());
        server.registerCommand(new CmdItemsGold());
        server.registerCommand(new CmdJunk());
        server.registerCommand(new CmdNameItem());
        server.registerCommand(new CmdNearinvis());
        server.registerCommand(new CmdNearvis());
        server.registerCommand(new CmdNether());
        server.registerCommand(new CmdNickClear());
        server.registerCommand(new CmdNickTest());
        server.registerCommand(new CmdRemEffects());
        server.registerCommand(new CmdSetAbsorb());
        server.registerCommand(new CmdSetBiome());
        server.registerCommand(new CmdSetFly());
        server.registerCommand(new CmdSetHead());
        server.registerCommand(new CmdSetLore());
        server.registerCommand(new CmdShake());
        server.registerCommand(new CmdStalkMe());
        server.registerCommand(new CmdTest1());
        server.registerCommand(new CmdTestEffects());
        server.registerCommand(new CmdTestSpawn());
        server.registerCommand(new CmdWalkSpeed());
        server.registerCommand(new CmdWorlds());
        server.registerCommand(new CmdAttributeTest());
        server.registerCommand(new CmdGui());
    }

    public void onShutdown() {
        System.out.println("======= ExamplePlugin --- Shutdown! =======");
    }

    public void onServerFullyLoaded() {
        server.setServerMOTD("Hello from PluginExample");
        server.setServerIconFilename("icon2.png");
    }

    public PluginInfo getPluginInfo() {
        PluginInfo info = new PluginInfo();
        info.description = "Rainbow Plugin Sample";
        info.eventSortOrder = 1000.0f; // call me much later on than default.

        // As example of controlling order based on another plugin.
        // This tells Rainbow to put us before 'RainbowZones' if it was found
        info.pluginNamesINeedToGetEventsBefore = new ArrayList<String>();
        info.pluginNamesINeedToGetEventsBefore.add("RainbowZones");

        return info;
    }

    public void onInteracted(MC_Player plr, MC_Location loc, MC_ItemStack isHandItem) {
        if (isHandItem.getId() == 280) {
            plr.setCompassTarget(loc);
        }
    }

    @Override
    public void onPlayerJoin(MC_Player plr) {
        plr.setPlayerListHeaderFooter(ChatColor.GOLD + "Welcome " + RainbowUtils.RainbowString(plr.getName()),
                ChatColor.AQUA + "This server is running\nRainbow v" + ChatColor.YELLOW + server.getRainbowVersion());
    }

    // Event handler: onTick...
    static long lastProcessedMS = 0;
    public ConcurrentHashMap<String, String> lastInfoMap = new ConcurrentHashMap<String, String>();

    public void onTick(int tickNumber) {
        long ms = System.currentTimeMillis();
        if (ms - lastProcessedMS < 500) return;
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
        for (MC_Player plr : server.getPlayers()) {
            // Are they on a sponge?
            MC_World world = plr.getWorld();
            MC_Location loc = plr.getLocation();

            int x = (int) Math.floor(loc.x); //loc.getBlockX();
            int y = loc.getBlockY() - 1;
            int z = (int) Math.floor(loc.z); //loc.getBlockZ();

            MC_Block blk = world.getBlockAt(x, y, z);

            int id = blk.getId();
            if (id == MC_BlockType.SPONGE) {
                // Let them know...
                plr.sendMessage(ChatColor.GREEN + "You're on a sponge!");
            } else {
                //plr.sendMessage(ChatColor.DARK_AQUA + String.format("Block Under You: " + ChatColor.WHITE + "ID=%d, Subtype=%d", blk.getId(), blk.getSubtype()));
            }

            // Show player status info...
            ArrayList<String> info = new ArrayList<String>();
            if (plr.isDead()) info.add("Dead");
            if (plr.isSleeping()) info.add("Sleeping");
            if (plr.isSneaking()) info.add("Sneaking");
            if (plr.isSprinting()) info.add("Sprinting");
            if (plr.isInvisible()) info.add("Invisible");
            if (plr.isInvulnerable()) info.add("Invulnerable");
            MC_Entity rider = plr.getRider();
            if (rider != null)
                info.add("Carrying " + rider.getType() + ": " + rider.getName());
            MC_Entity vehicle = plr.getVehicle();
            if (vehicle != null)
                info.add("Vehicle " + vehicle.getType() + ": " + vehicle.getName());
            info.add("Gamemode: " + plr.getGameMode());
            if (info.size() > 0) {
                String infoMsg = RainbowUtils.GetCommaList(info);
                String lastMsg = lastInfoMap.get(plr.getName());
                if ((lastMsg == null) || !lastMsg.equalsIgnoreCase(infoMsg)) {
                    plr.sendMessage(ChatColor.DARK_AQUA + "You are: " + ChatColor.WHITE + infoMsg);
                    lastInfoMap.put(plr.getName(), infoMsg);
                }
            }

            MC_Location lastTarget = hashCompassTarget.get(plr.getName());
            MC_Location curTarget = plr.getCompassTarget();
            if (curTarget != null) {
                if ((lastTarget != null) && lastTarget.equals(curTarget)) {
                    // skip if same
                } else {
                    hashCompassTarget.put(plr.getName(), curTarget);
                    plr.sendMessage(ChatColor.LIGHT_PURPLE + "Compass Target Changed: " + ChatColor.WHITE + curTarget.toString());
                }
            }

        }
    } // end of method: onTick()...


} // end of class: MyPlugin
