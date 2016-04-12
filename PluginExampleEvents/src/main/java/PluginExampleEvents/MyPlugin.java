package PluginExampleEvents;

import PluginReference.BlockHelper;
import PluginReference.ChatColor;
import PluginReference.MC_Block;
import PluginReference.MC_Chest;
import PluginReference.MC_Container;
import PluginReference.MC_ContainerType;
import PluginReference.MC_DamageType;
import PluginReference.MC_DirectionNESWUD;
import PluginReference.MC_Entity;
import PluginReference.MC_EventInfo;
import PluginReference.MC_Hand;
import PluginReference.MC_HangingEntityType;
import PluginReference.MC_ItemFrameActionType;
import PluginReference.MC_ItemStack;
import PluginReference.MC_ItemType;
import PluginReference.MC_Location;
import PluginReference.MC_MiscGriefType;
import PluginReference.MC_Player;
import PluginReference.MC_PotionEffectType;
import PluginReference.MC_Projectile;
import PluginReference.MC_Server;
import PluginReference.MC_Sign;
import PluginReference.MC_Skeleton;
import PluginReference.PluginBase;
import PluginReference.PluginInfo;
import PluginReference.RainbowUtils;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class MyPlugin extends PluginBase {
    public static MC_Server server = null;

    @Override
    public void onStartup(MC_Server argServer) {
        System.out.println("======= Event Sample --- Startup! =======");
        server = argServer;
    }

    @Override
    public void onShutdown() {
        System.out.println("======= Event Sample --- Shutdown! =======");
    }

    @Override
    public PluginInfo getPluginInfo() {
        PluginInfo info = new PluginInfo();
        info.description = "Rainbow Events Sample";
        info.eventSortOrder = -2345.67f; // call me much earlier than default
        return info;
    }


    public static boolean TogglePlaceProtected = false;
    public static boolean ToggleBreakProtected = false;
    public static boolean ToggleExplodeProtected = false;
    public static boolean ToggleDamageHanging = false;
    public static boolean ToggleDamage = false;
    public static boolean TogglePotion = false;
    public static boolean ToggleTeleport = false;
    public static boolean ToggleDimension = false;
    public static boolean ToggleDrop = false;
    public static boolean ToggleAttack = false;
    public static boolean TogglePistons = false;
    public static boolean ToggleFlow = false;
    public static boolean ToggleMove = false;
    public static boolean ToggleSound = false;
    public static boolean ToggleSpawn = false;
    public static boolean ToggleUse = false;
    public static boolean TogglePickup = false;
    public static boolean ToggleInteract = false;
    public static boolean ToggleHealer = false;
    public static boolean ToggleNoDeath = false;
    public static boolean ToggleSuddenDeath = false;
    public static boolean ToggleFishReel = false;

    public static boolean ToggleBlockPlace = false;
    public static boolean ToggleMiscGrief = false;
    public static boolean ToggleNoSpectate = false;
    public static boolean ToggleDispense = false;

    public static boolean ToggleMidas = false;

    public static int healAmount = 20;

    @Override
    public void onConsoleInput(String cmd, MC_EventInfo ei) {
        if (ei.isCancelled) return;

        System.out.println("Console Input: " + cmd);
        if (cmd.equalsIgnoreCase("ConsoleTest")) {
            ei.isCancelled = true;
            System.out.println("---------------------");
            System.out.println("Caught this command");
            System.out.println("---------------------");
            return;
        }

    }

    @Override
    public void onPlayerInput(MC_Player plr, String msg, MC_EventInfo ei) {
        if (ei.isCancelled) return;

        if (msg.equalsIgnoreCase("/permtest")) {
            ei.isCancelled = true;
            plr.sendMessage(ChatColor.AQUA + "Perm 'joetest_true': " + ChatColor.WHITE + plr.hasPermission("joetest_true"));
            plr.sendMessage(ChatColor.AQUA + "Perm 'joetest_false': " + ChatColor.WHITE + plr.hasPermission("joetest_false"));
            return;
        }

        // op only...
        if (!plr.isOp()) return;


        if (msg.equalsIgnoreCase("/toggle")) {
            ei.isCancelled = true;
            plr.sendMessage(ChatColor.AQUA + "Usage: /TogglePlace");
            plr.sendMessage(ChatColor.AQUA + "Usage: /ToggleBreak");
            plr.sendMessage(ChatColor.AQUA + "Usage: /Toggle...Explode|Hanging|Damage|Potion|Teleport");
            plr.sendMessage(ChatColor.AQUA + "Usage: /Toggle...Dimension|Drop|Attack|Pistons|Flow|Move");
            plr.sendMessage(ChatColor.AQUA + "Usage: /Toggle...Sound|Spawn");
            return;
        }

        if (msg.equalsIgnoreCase("/TogglePlace")) {
            ei.isCancelled = true;
            TogglePlaceProtected = !TogglePlaceProtected;
            plr.sendMessage(ChatColor.AQUA + "TogglePlaceProtected set to: " + ChatColor.GREEN + TogglePlaceProtected);
            return;
        }
        if (msg.equalsIgnoreCase("/ToggleBreak")) {
            ei.isCancelled = true;
            ToggleBreakProtected = !ToggleBreakProtected;
            plr.sendMessage(ChatColor.AQUA + "ToggleBreakProtected set to: " + ChatColor.GREEN + ToggleBreakProtected);
            return;
        }
        if (msg.equalsIgnoreCase("/ToggleExplode")) {
            ei.isCancelled = true;
            ToggleExplodeProtected = !ToggleExplodeProtected;
            plr.sendMessage(ChatColor.AQUA + "ToggleExplodeProtected set to: " + ChatColor.GREEN + ToggleExplodeProtected);
            return;
        }
        if (msg.equalsIgnoreCase("/ToggleHanging")) {
            ei.isCancelled = true;
            ToggleDamageHanging = !ToggleDamageHanging;
            plr.sendMessage(ChatColor.AQUA + "ToggleDamageHanging set to: " + ChatColor.GREEN + ToggleDamageHanging);
            return;
        }
        if (msg.equalsIgnoreCase("/ToggleDamage")) {
            ei.isCancelled = true;
            ToggleDamage = !ToggleDamage;
            plr.sendMessage(ChatColor.AQUA + "TogglePlayerDamage set to: " + ChatColor.GREEN + ToggleDamage);
            return;
        }
        if (msg.equalsIgnoreCase("/TogglePotion")) {
            ei.isCancelled = true;
            TogglePotion = !TogglePotion;
            plr.sendMessage(ChatColor.AQUA + "TogglePotion set to: " + ChatColor.GREEN + TogglePotion);
            return;
        }
        if (msg.equalsIgnoreCase("/ToggleTeleport")) {
            ei.isCancelled = true;
            ToggleTeleport = !ToggleTeleport;
            plr.sendMessage(ChatColor.AQUA + "ToggleTeleport set to: " + ChatColor.GREEN + ToggleTeleport);
            return;
        }
        if (msg.equalsIgnoreCase("/ToggleDimension")) {
            ei.isCancelled = true;
            ToggleDimension = !ToggleDimension;
            plr.sendMessage(ChatColor.AQUA + "ToggleDimension set to: " + ChatColor.GREEN + ToggleDimension);
            return;
        }
        if (msg.equalsIgnoreCase("/ToggleDrop")) {
            ei.isCancelled = true;
            ToggleDrop = !ToggleDrop;
            plr.sendMessage(ChatColor.AQUA + "ToggleDrop set to: " + ChatColor.GREEN + ToggleDrop);
            return;
        }
        if (msg.equalsIgnoreCase("/ToggleAttack")) {
            ei.isCancelled = true;
            ToggleAttack = !ToggleAttack;
            plr.sendMessage(ChatColor.AQUA + "ToggleAttack set to: " + ChatColor.GREEN + ToggleAttack);
            return;
        }
        if (msg.equalsIgnoreCase("/TogglePistons")) {
            ei.isCancelled = true;
            TogglePistons = !TogglePistons;
            plr.sendMessage(ChatColor.AQUA + "TogglePistons set to: " + ChatColor.GREEN + TogglePistons);
            return;
        }
        if (msg.equalsIgnoreCase("/ToggleFlow")) {
            ei.isCancelled = true;
            ToggleFlow = !ToggleFlow;
            plr.sendMessage(ChatColor.AQUA + "ToggleFlow set to: " + ChatColor.GREEN + ToggleFlow);
            return;
        }
        if (msg.equalsIgnoreCase("/ToggleMove")) {
            ei.isCancelled = true;
            ToggleMove = !ToggleMove;
            plr.sendMessage(ChatColor.AQUA + "ToggleMove set to: " + ChatColor.GREEN + ToggleMove);
            return;
        }
        if (msg.equalsIgnoreCase("/ToggleSound")) {
            ei.isCancelled = true;
            ToggleSound = !ToggleSound;
            plr.sendMessage(ChatColor.AQUA + "ToggleSound set to: " + ChatColor.GREEN + ToggleSound);
            return;
        }
        if (msg.equalsIgnoreCase("/ToggleSpawn")) {
            ei.isCancelled = true;
            ToggleSpawn = !ToggleSpawn;
            plr.sendMessage(ChatColor.AQUA + "ToggleSpawn set to: " + ChatColor.GREEN + ToggleSpawn);
            return;
        }

        if (msg.equalsIgnoreCase("/ToggleUse")) {
            ei.isCancelled = true;
            ToggleUse = !ToggleUse;
            plr.sendMessage(ChatColor.AQUA + "ToggleUse set to: " + ChatColor.GREEN + ToggleUse);
            return;
        }
        if (msg.equalsIgnoreCase("/TogglePickup")) {
            ei.isCancelled = true;
            TogglePickup = !TogglePickup;
            plr.sendMessage(ChatColor.AQUA + "TogglePickup set to: " + ChatColor.GREEN + TogglePickup);
            return;
        }
        if (msg.equalsIgnoreCase("/ToggleInteract")) {
            ei.isCancelled = true;
            ToggleInteract = !ToggleInteract;
            plr.sendMessage(ChatColor.AQUA + "ToggleInteract set to: " + ChatColor.GREEN + ToggleInteract);
            return;
        }
        if (msg.equalsIgnoreCase("/ToggleHealer")) {
            ei.isCancelled = true;
            ToggleHealer = !ToggleHealer;
            plr.sendMessage(ChatColor.AQUA + "ToggleHealer set to: " + ChatColor.GREEN + ToggleHealer);
            return;
        }
        if (msg.equalsIgnoreCase("/ToggleBlockPlace")) {
            ei.isCancelled = true;
            ToggleBlockPlace = !ToggleBlockPlace;
            plr.sendMessage(ChatColor.AQUA + "ToggleBlockPlace set to: " + ChatColor.GREEN + ToggleBlockPlace);
            return;
        }
        if (msg.equalsIgnoreCase("/ToggleNoDeath")) {
            ei.isCancelled = true;
            ToggleNoDeath = !ToggleNoDeath;
            plr.sendMessage(ChatColor.AQUA + "ToggleNoDeath set to: " + ChatColor.GREEN + ToggleNoDeath);
            return;
        }
        if (msg.equalsIgnoreCase("/ToggleSuddenDeath")) {
            ei.isCancelled = true;
            ToggleSuddenDeath = !ToggleSuddenDeath;
            plr.sendMessage(ChatColor.AQUA + "ToggleSuddenDeath set to: " + ChatColor.GREEN + ToggleSuddenDeath);
            return;
        }
        if (msg.equalsIgnoreCase("/ToggleFishReel")) {
            ei.isCancelled = true;
            ToggleFishReel = !ToggleFishReel;
            plr.sendMessage(ChatColor.AQUA + "ToggleFishCatches set to: " + ChatColor.GREEN + ToggleFishReel);
            return;
        }
        if (msg.equalsIgnoreCase("/ToggleMiscGrief")) {
            ei.isCancelled = true;
            ToggleMiscGrief = !ToggleMiscGrief;
            plr.sendMessage(ChatColor.AQUA + "ToggleMiscGrief set to: " + ChatColor.GREEN + ToggleMiscGrief);
            return;
        }
        if (msg.equalsIgnoreCase("/ToggleNoSpectate")) {
            ei.isCancelled = true;
            ToggleNoSpectate = !ToggleNoSpectate;
            plr.sendMessage(ChatColor.AQUA + "ToggleNoSpectate set to: " + ChatColor.GREEN + ToggleNoSpectate);
            return;
        }
        if (msg.equalsIgnoreCase("/ToggleMidas")) {
            ei.isCancelled = true;
            ToggleMidas = !ToggleMidas;
            plr.sendMessage(ChatColor.AQUA + "ToggleMidas set to: " + ChatColor.GREEN + ToggleMidas);
            return;
        }
        if (msg.equalsIgnoreCase("/ToggleDispense")) {
            ei.isCancelled = true;
            ToggleDispense = !ToggleDispense;
            plr.sendMessage(ChatColor.AQUA + "ToggleDispense set to: " + ChatColor.GREEN + ToggleDispense);
            return;
        }

        if (msg.startsWith("/healer")) {
            ei.isCancelled = true;
            healAmount = RainbowUtils.GetIntegerArgument(msg, 20);
            plr.sendMessage(ChatColor.AQUA + "Heal Amount: " + ChatColor.GREEN + healAmount);
            return;
        }

        if (msg.startsWith("/bedinfo")) {
            ei.isCancelled = true;
            MC_Location loc = plr.getBedRespawnLocation();
            if (loc == null) {
                plr.sendMessage(ChatColor.AQUA + "Bed Respawn Location: " + ChatColor.RED + "None");
            } else {
                plr.sendMessage(ChatColor.AQUA + "Bed Respawn Location: " + ChatColor.WHITE + loc.toString());
            }
            return;
        }
        if (msg.startsWith("/bedset")) {
            ei.isCancelled = true;
            MC_Location loc = plr.getLocation();
            plr.setBedRespawnLocation(loc, true);
            plr.sendMessage(ChatColor.GREEN + "Bed Respawn Location Set To: " + ChatColor.WHITE + plr.getBedRespawnLocation().toString());
            return;
        }
        if (msg.startsWith("/bedforce")) {
            ei.isCancelled = true;
            MC_Location loc = plr.getLocation();
            plr.setBedRespawnLocation(loc, false);
            plr.sendMessage(ChatColor.GREEN + "Bed Respawn Location Set To: " + ChatColor.WHITE + plr.getBedRespawnLocation().toString());
            return;
        }
        if (msg.startsWith("/bedclear")) {
            ei.isCancelled = true;
            plr.setBedRespawnLocation(null, false);
            plr.sendMessage(ChatColor.GREEN + "Bed Cleared");
            return;
        }

    }

    @Override
    public void onPlayerLogin(String playerName, UUID uuid, InetAddress address, MC_EventInfo ei) {
        String logMsg = String.format("%s LOGIN from IP %s. UUID=%s", playerName, address.toString(), uuid.toString());
        System.out.println("EventSamplePlugin -- " + logMsg);
    }

    @Override
    public void onPlayerJoin(MC_Player plr) {
        String logMsg = String.format("%s LOGGED IN", plr.getName());
        System.out.println("EventSamplePlugin -- " + logMsg);
        plr.sendMessage(ChatColor.LIGHT_PURPLE + "[ExamplePlugin] " + ChatColor.AQUA + "Custom Logon Message");
        MC_ItemStack is = plr.getItemInHand();
        if ((is != null) && (is.getId() == 0)) {
            // Give them a sea lantern...
            plr.setItemInHand(server.createItemStack(MC_ItemType.SEA_LANTERN, 1, 0));
            plr.updateInventory();
        }
    }

    @Override
    public void onPlayerLogout(String playerName, UUID uuid) {
        String logMsg = String.format("%s LOGOUT. UUID=%s", playerName, uuid.toString());
        System.out.println("EventSamplePlugin -- " + logMsg);
    }

    // Past Tense / "After" Events (i.e. can't cancel)
    // ------------------------------------------------------------------------

    @Override
    public void onItemPlaced(MC_Player plr, MC_Location loc, MC_ItemStack isHandItem, MC_Location locPlacedAgainst, MC_DirectionNESWUD dir) {
        String logMsg = String.format("%s PLACED %s (%d) @ %s on %s facing %s", plr.getName(), isHandItem.getFriendlyName(), isHandItem.getId(), loc.toString(), locPlacedAgainst.toString(), dir + "");
        System.out.println("EventSamplePlugin -- " + logMsg);
    }

    public static int nextRotationValue = 0;
    public static int nextDir = 1;

    @Override
    public void onInteracted(MC_Player plr, MC_Location loc, MC_ItemStack isHandItem) {
        String strHandItem = isHandItem.getFriendlyName();
        String logMsg = String.format("%s INTERACT @ %s", plr.getName(), loc.toString());
        if ((strHandItem != null) && (strHandItem.length() > 0))
            logMsg += " w/" + strHandItem;

        System.out.println("EventSamplePlugin -- " + logMsg);
        MC_Block blk = plr.getWorld().getBlockAt((int) loc.x, (int) loc.y, (int) loc.z);
        System.out.println("Interacted Block ID: " + blk.getId());

        MC_DirectionNESWUD dir = plr.getWorld().getBlockFacing((int) loc.x, (int) loc.y, (int) loc.z);
        System.out.println("Interacted Block Facing: " + dir);

        MC_Sign sign = plr.getWorld().getSignAt(loc);
        if (sign != null) {
            // Show sign info...
            System.out.println("Found a sign...");
            List<String> lines = sign.getLines();
            for (int i = 0; i < 4; i++) {
                System.out.println(String.format("- Sign Line %d: %s", i + 1, lines.get(i)));
            }

            // Toggle a line to update
            String lastLine = lines.get(2);
            String tgtMsg = ChatColor.LIGHT_PURPLE + "PostAct";
            if (lastLine.equalsIgnoreCase(tgtMsg)) tgtMsg = "";

            lines.set(2, tgtMsg);
            sign.setLines(lines);

        }
        int rotation = plr.getWorld().getBlockRotation((int) loc.x, (int) loc.y, (int) loc.z);
        System.out.println("- Block Rotation: " + rotation);

        if (isHandItem.getId() == MC_ItemType.GOLD_NUGGET) {
            plr.getWorld().setBlockRotation((int) loc.x, (int) loc.y, (int) loc.z, nextRotationValue);
            System.out.println("========= GOLD NUGGET: Adjusted Rotation: " + nextRotationValue);
            nextRotationValue = (nextRotationValue + 1) % 15;
        }
        if (isHandItem.getId() == MC_ItemType.ARROW) {
            MC_DirectionNESWUD[] dirVals = MC_DirectionNESWUD.values();
            MC_DirectionNESWUD newDir = dirVals[nextDir];

            nextDir++;
            if (nextDir >= dirVals.length) nextDir = 1;
            plr.getWorld().setBlockFacing((int) loc.x, (int) loc.y, (int) loc.z, newDir);
            System.out.println("========= ARROW: Adjusted Direction: " + newDir);
        }
    }

    @Override
    public void onBlockBroke(MC_Player plr, MC_Location loc, MC_Block blk) {
        int blockID = blk.getId();
        int blockID_Subtype = blk.getSubtype();
        String strBlockName = BlockHelper.getBlockName(blockID);
        if (blockID_Subtype != 0) strBlockName += ":" + blockID_Subtype;

        String logMsg = String.format("%s BROKE %s @ %s", plr.getName(), strBlockName, loc.toString());
        System.out.println("EventSamplePlugin[NEW] -- " + logMsg);
    }

    @Override
    public void onPlayerDeath(MC_Player plrVictim, MC_Player plrKiller, MC_DamageType dmgType, String deathMsg) {
        String strKiller = dmgType.toString();
        if (plrKiller != null) strKiller += " by " + plrKiller.getName();

        String logMsg = String.format("[%s died @ %s from %s] DeathMsg: %s", plrVictim.getName(), plrVictim.getLocation().toString(), strKiller, deathMsg);
        System.out.println("EventSamplePlugin -- " + logMsg);
    }

    @Override
    public void onPlayerRespawn(MC_Player plr) {
        String logMsg = String.format("%s RESPAWNED TO %s", plr.getName(), plr.getLocation().toString());
        System.out.println("EventSamplePlugin -- " + logMsg);
    }
    // ------------------------------------------------------------------------


    // Events you can cancel...
    // ------------------------------------------------------------------------
    @Override
    public void onAttemptBlockBreak(MC_Player plr, MC_Location loc, MC_EventInfo ei) {
        String logMsg = String.format("%s Attempting Block Break @ %s", plr.getName(), loc.toString());
        System.out.println("EventSamplePlugin -- " + logMsg);
        if (ToggleBreakProtected) {
            ei.isCancelled = true;
            plr.sendMessage(ChatColor.RED + "You can't break that!");
        }
    }

    @Override
    public void onAttemptPlaceOrInteract(MC_Player plr, MC_Location loc, MC_DirectionNESWUD dir, MC_Hand hand, MC_EventInfo ei) {
        String logMsg = String.format("%s Attempting Block Place or Interact @ %s, dir=%s, hand=%s", plr.getName(), loc.toString(), dir.toString(), hand.name());
        System.out.println("EventSamplePlugin -- " + logMsg);
        if (TogglePlaceProtected) {
            ei.isCancelled = true;
            plr.sendMessage(ChatColor.RED + "You can't place blocks or interact here!");
        }

        // Detecting if target is a sign...
        if (plr.isOp()) {
            MC_Sign sign = plr.getWorld().getSignAt(loc);
            if (sign != null) {
                // Show sign info...
                System.out.println("Found a sign...");
                List<String> lines = sign.getLines();
                for (int i = 0; i < 4; i++) {
                    System.out.println(String.format("- Sign Line %d: %s", i + 1, lines.get(i)));
                }

                // Toggle last line
                String lastLine = lines.get(3);
                String tgtMsg = ChatColor.MAGIC + "MAGIC";
                if (lastLine.equalsIgnoreCase(tgtMsg)) tgtMsg = "";

                lines.set(3, tgtMsg);
                sign.setLines(lines);
            }
        }

        // If chest, look for nearby sign...
        //MC_Block blk = plr.getWorld().getBlockAt((int)loc.x, (int)loc.y, (int)loc.z);
        //if (blk.getId() == 54) {
        MC_Chest chest = plr.getWorld().getChestAt(loc);
        if (chest != null) {
            System.out.println("It's a CHEST. Block ID: " + chest.getBlockId());

            // Check for nearby signs...
            for (MC_DirectionNESWUD d : MC_DirectionNESWUD.values()) {
                MC_Location signLoc = loc.getLocationAtDirection(d);
                MC_Sign sign = plr.getWorld().getSignAt(signLoc);
                if (sign != null) {
                    System.out.println(" --- Sign near Chest @ " + signLoc.toString());
                }
            }

            // Do something with chest contents...
            // -------------------------------------------------------
            List<MC_ItemStack> items = chest.getInventory();
            boolean foundSponge = false;
            for (int i = 0; i < items.size(); i++) {
                MC_ItemStack is = items.get(i);
                int id = is.getId();
                if (id == 0) continue; // skip empty
                System.out.println(String.format("- Chest Contents: %d x %s", is.getCount(), ChatColor.StripColor(is.getFriendlyName())));

                // If a sponge, rename it. Why? Because this is a sample :D
                if (id == MC_ItemType.SPONGE) {
                    System.out.println("Setting Item ID " + id + " to Mr. Spongey at idx " + i + ", on item " + is.getFriendlyName());
                    is.setCustomName(RainbowUtils.RainbowString("Mr. Spongey"));
                    foundSponge = true;
                }
            }
            if (foundSponge) chest.setInventory(items);
            // -------------------------------------------------------

            // Show info about linked chests...
            List<String> strChestDirs = new ArrayList<String>();
            if (chest.GetLinkedChestAt(MC_DirectionNESWUD.NORTH) != null)
                strChestDirs.add("NORTH");
            if (chest.GetLinkedChestAt(MC_DirectionNESWUD.SOUTH) != null)
                strChestDirs.add("SOUTH");
            if (chest.GetLinkedChestAt(MC_DirectionNESWUD.EAST) != null)
                strChestDirs.add("EAST");
            if (chest.GetLinkedChestAt(MC_DirectionNESWUD.WEST) != null)
                strChestDirs.add("WEST");
            if (strChestDirs.size() > 0)
                System.out.println("* Chests at Directions: " + RainbowUtils.GetCommaList(strChestDirs));
            else
                System.out.println("* This chest is all alone! He needs a hug...");
        } // end of 'if chest'

        MC_Container container = plr.getWorld().getContainerAt(loc);
        if (container != null) {
            System.out.println("------------- BEGIN of CONTAINER --------------");
            System.out.println("Container Size: " + container.getSize());
            for (int i = 0; i < container.getSize(); i++) {
                MC_ItemStack is = container.getItemAtIdx(i);
                if ((is == null) || (is.getId() == 0)) continue;
                System.out.println("- Item at idx " + i + ": " + is.getFriendlyName());
            }
            System.out.println("------------- END of CONTAINER --------------");
        }

        if (ToggleMidas) {
            if (chest != null) {
                List<MC_ItemStack> items = chest.getInventory();
                MC_ItemStack isGold = server.createItemStack(MC_ItemType.GOLD_BLOCK, 64, 0);
                for (int i = 0; i < items.size(); i++) {
                    items.set(i, isGold.getDuplicate());
                }
                chest.setInventory(items);
                //ei.isCancelled = true;
                plr.sendMessage(ChatColor.GREEN + "Midas Touch! " + ChatColor.GOLD + "Set chest to all gold blocks!");
            }
        }


        // Note: Although API doesn't yet distinguish 'place vs interact' here, you
        // could check if player's item in hand is empty. If so, they are definitely
        // iteracting rather than placing and you could decide accordingly
    }

    @Override
    public void onAttemptExplosion(MC_Location loc, MC_EventInfo ei) {
        if (ei.isCancelled) return;
        String logMsg = String.format("EXPLOSION @ %s", loc.toString());
        System.out.println("EventSamplePlugin -- " + logMsg);
        if (ToggleExplodeProtected) {
            ei.isCancelled = true;
            server.broadcastMessage(ChatColor.AQUA + "Explosion prevented @ " + loc.toString());
        }
        // Note -- You get called once for a blast not for each block destroy.
        // As such, if you're implementing a zone-protection you'll probably want to check for
        // zones within ~6 block radius.  If found it's possible it might get affected by explosion
        // so can then decide to cancel (if appropriate for your plugin).
    }

    // ------------------------------------------------------------------------

    @Override
    public void onAttemptDamageHangingEntity(MC_Player plr, MC_Location loc, MC_HangingEntityType entType, MC_EventInfo ei) {
        if (ei.isCancelled) return;
        // Player could be null (like if damage from explosion)
        String strName = (plr == null) ? "?" : plr.getName();

        String logMsg = String.format("%s damaging %s @ %s", strName, entType.toString(), loc.toString());
        System.out.println("EventSamplePlugin -- " + logMsg);

        if (ToggleDamageHanging) {
            if (plr != null)
                plr.sendMessage(ChatColor.RED + "You can't damage that entity!");
            ei.isCancelled = true;
            System.out.println("Prevented: " + logMsg);
        }

    }

    @Override
    public void onAttemptItemFrameInteract(MC_Player plr, MC_Location loc, MC_ItemFrameActionType actionType, MC_EventInfo ei) {
        if (ei.isCancelled) return;
        // Player could be null (like if damage from explosion)
        String strName = (plr == null) ? "?" : plr.getName();

        String logMsg = String.format("%s ItemFrame Action %s @ %s", strName, actionType.toString(), loc.toString());
        System.out.println("EventSamplePlugin -- " + logMsg);

        if (ToggleDamageHanging) {
            if (plr != null)
                plr.sendMessage(ChatColor.RED + "You can't operate on that item frame!");
            ei.isCancelled = true;
            System.out.println("Prevented: " + logMsg);
        }

    }

    @Override
    public void onAttemptPotionEffect(MC_Player plr, MC_PotionEffectType potionType, MC_EventInfo ei) {
        if (ei.isCancelled) return;

        // This event could be used to prevent hurtful potions (instant damage, weakness, etc) if in a particular zone
        String logMsg = String.format("%s getting potion effect %s", plr.getName(), potionType.toString());
        System.out.println("EventSamplePlugin -- " + logMsg);

        if (TogglePotion) {
            plr.sendMessage(ChatColor.RED + "Prevented Effect: " + potionType);
            ei.isCancelled = true;
            System.out.println("Prevented: " + logMsg);
        }
    }

    @Override
    public void onAttemptPlayerTeleport(MC_Player plr, MC_Location loc, MC_EventInfo ei) {
        if (ei.isCancelled) return;

        String logMsg = String.format("%s PORT %s to %s", plr.getName(), plr.getLocation().toString(), loc.toString());
        System.out.println("EventSamplePlugin -- " + logMsg);

        if (ToggleTeleport) {
            plr.sendMessage(ChatColor.RED + "Prevented Teleport!");
            ei.isCancelled = true;
            System.out.println("Prevented: " + logMsg);
        }
    }

    // Called when player travels between dimensions (Nether, End, Overworld)
    @Override
    public void onAttemptPlayerChangeDimension(MC_Player plr, int newDimension, MC_EventInfo ei) {
        if (ei.isCancelled) return;

        String logMsg = String.format("%s changing dimension from %d to %d", plr.getName(), plr.getLocation().dimension, newDimension);
        System.out.println("EventSamplePlugin -- " + logMsg);

        if (ToggleDimension) {
            plr.sendMessage(ChatColor.RED + "Prevented Change Dimension!");
            ei.isCancelled = true;
            System.out.println("Prevented: " + logMsg);
        }
    }

    @Override
    public void onAttemptItemDrop(MC_Player plr, MC_ItemStack is, MC_EventInfo ei) {
        String logMsg = String.format("%s dropping item. InHand: %d x ID=%d (%s) @ %s", plr.getName(), is.getCount(), is.getId(), is.getFriendlyName(), plr.getLocation().toString());
        System.out.println("EventSamplePlugin -- " + logMsg);

        if (ToggleDrop) {
            plr.sendMessage(ChatColor.RED + "Prevented: " + logMsg);
            System.out.println("Prevented: " + logMsg);
            ei.isCancelled = true;
        }
    }

    // Called when a player tries to attack an entity...
    @Override
    public void onAttemptAttackEntity(MC_Player plr, MC_Entity ent, MC_EventInfo ei) {
        String entName = ent.getName();
        if (ent instanceof MC_Player) entName += " (Player)";

        if (ent instanceof MC_Skeleton) {
            MC_Skeleton skel = (MC_Skeleton) ent;
            entName += "--- " + skel.getSkeletonType() + " ---";
        }

        String logMsg = String.format("[%s @ %s] -- attacking -- [%s @ %s] : entName=%s",
                plr.getName(), plr.getLocation().toString(),
                ent.getType().toString(), ent.getLocation().toString(),
                entName);

        System.out.println("EventSamplePlugin -- " + logMsg);

        if (ToggleAttack) {
            plr.sendMessage(ChatColor.RED + "Prevented Attack on " + ChatColor.GOLD + ent.getType().toString());
            System.out.println("Prevented: " + logMsg);
            ei.isCancelled = true;
        }

    }

    // Called when an entity is about to receive damage...
    @Override
    public void onAttemptEntityDamage(MC_Entity ent, MC_DamageType dmgType, double amt, MC_EventInfo ei) {
        String entName = ent.getName();
        if (ent instanceof MC_Player) {
            entName += " (Player)";
        }

        String strAttacker = "?";
        MC_Entity attacker = ent.getAttacker();
        if (attacker != null) strAttacker = attacker.getName();

        String logMsg = String.format("onAttemptEntityDamage: %s %s by %s for %.2f @ %s", entName, dmgType.toString(), strAttacker, amt, ent.getLocation().toString());
        // Commenting out echo since it is annoyingly often.  Uncomment if you need to experiment...
        //System.out.println("EventSamplePlugin -- " + logMsg);

        if (ToggleDamage) {
            ei.isCancelled = true;
            System.out.println("Prevented: " + logMsg);
        }

        if (ToggleSuddenDeath) {
            ei.isCancelled = false;
            ent.setAdjustedIncomingDamage(100000.0f);
        }
    }

    @Override
    public void onAttemptPistonAction(MC_Location loc, MC_DirectionNESWUD dir, MC_EventInfo ei) {
        String logMsg = String.format("onAttemptPistonAction: %s w/direction %s", loc.toString(), dir.toString());
        System.out.println("EventSamplePlugin -- " + logMsg);
        if (TogglePistons) {
            ei.isCancelled = true;
            System.out.println("Prevented: " + logMsg);
        }
    }

    @Override
    public void onAttemptBlockFlow(MC_Location loc, MC_Block blk, MC_EventInfo ei) {
        String logMsg = String.format("onFlow: %s w/blk id %d", loc.toString(), blk.getId());
        //System.out.println("EventSamplePlugin -- " + logMsg);
        if (ToggleFlow) {
            ei.isCancelled = true;
            System.out.println("Prevented: " + logMsg);
        }
    }

    @Override
    public void onContainerOpen(MC_Player plr, List<MC_ItemStack> items, String internalClassName) {
        String logMsg = String.format("ChestOpen: %s @ %s. Size=%d, tag=%s", plr.getName(), plr.getLocation().toString(), items.size(), internalClassName);
        System.out.println("EventSamplePlugin -- " + logMsg);

        int containerSize = items.size();
        // Ignore unusual containers...
        if ((containerSize != 3 * 9) && (containerSize != 6 * 9)) return;

        // Otherwise, do something with contents.  Could write a chest sorting plugin with this event...
        // For now just shuffle contents...
		/*plr.sendMessage(ChatColor.GOLD + "[EventSamplePlugin] Shuffling chest contents...");
		for(int idx=0; idx<items.size(); idx++)
		{
			int idx2 = (int)(Math.random() * items.size());
			if(idx2 == idx) continue;
			MC_ItemStack is1 = items.get(idx);
			items.set(idx, items.get(idx2));
			items.set(idx2, is1);
		}*/

    }

    //public static long msLastMoveReported = 0;
    @Override
    public void onAttemptPlayerMove(MC_Player plr, MC_Location locFrom, MC_Location locTo, MC_EventInfo ei) {
		/*String logMsg = String.format("onAttemptPlayerMove: %s : %s[%.1f,%.1f] to %s[%.1f,%.1f]",
				plr.getName(),
				locFrom.toString(), locFrom.yaw, locFrom.pitch,
				locTo.toString(), locTo.yaw, locTo.pitch);

		// Throttled since spammy...
		long msNow = System.currentTimeMillis();
		if(msNow - msLastMoveReported >= 500)
		{
			System.out.println("EventSamplePlugin -- " + logMsg);
			msLastMoveReported = msNow; 
		}*/

        if (ToggleMove) {
            ei.isCancelled = true;
        }
    }

    @Override
    public void onPacketSoundEffect(MC_Player plr, String soundName, MC_Location loc, MC_EventInfo ei) {
        //String logMsg = String.format("onPacketSoundEffect: %s: %s @ %s", plr.getName(), soundName, loc.toString());
        //System.out.println("EventSamplePlugin -- " + logMsg);

        if (ToggleSound) {
            ei.isCancelled = true;
        }

    }


    @Override
    public void onSignChanging(MC_Player plr, MC_Sign sign, MC_Location loc, List<String> newLines, MC_EventInfo ei) {
        String logMsg = String.format("onSignChanging: %s: %s : %s", plr.getName(), plr.getLocation().toString(), RainbowUtils.GetCommaList(newLines));
        System.out.println("EventSamplePlugin -- " + logMsg);

        newLines.set(2, ChatColor.BLUE + "RAND: " + System.currentTimeMillis() % 1000);
        // Verify coordinates
        MC_Sign signVerify = plr.getWorld().getSignAt(loc);
        if (signVerify != null) System.out.println("SIGN COORDINATES VERIFIED");
        else
            System.out.println("SIGN COORDINATES WRONG?  No Sign at location.");
    }

    @Override
    public void onSignChanged(MC_Player plr, MC_Sign sign, MC_Location loc) {
        String logMsg = String.format("onSignChanged: %s: %s : %s", plr.getName(), plr.getLocation().toString(), RainbowUtils.GetCommaList(sign.getLines()));
        System.out.println("EventSamplePlugin -- " + logMsg);

        // Verify coordinates
        MC_Sign signVerify = plr.getWorld().getSignAt(loc);
        if (signVerify != null) System.out.println("SIGN COORDINATES VERIFIED");
        else
            System.out.println("SIGN COORDINATES WRONG?  No Sign at location.");

    }

    @Override
    public void onAttemptEntitySpawn(MC_Entity ent, MC_EventInfo ei) {
        // commented out since spammy...
        //String logMsg = String.format("onAttemptEntitySpawn: %s @ %s", ent.getType().toString(), ent.getLocation().toString());
        //System.out.println("EventSamplePlugin -- " + logMsg);
        if (ent instanceof MC_Projectile) {
            System.out.printf("Projectile %s spawned by %s\n", ent, ((MC_Projectile) ent).getProjectileSource());
        }
        if (ToggleSpawn) {
            ei.isCancelled = true;
        }
    }

    @Override
    public void onAttemptHopperReceivingItem(MC_Location loc, MC_ItemStack is, boolean isMinecartHopper, MC_EventInfo ei) {
        String logMsg = String.format("onAttemptHopperReceivingItem: %s: %s : IsMinecart=%s", loc.toString(), is.getFriendlyName(), isMinecartHopper + "");
        System.out.println("EventSamplePlugin -- " + logMsg);
    }

    @Override
    public void onAttemptBookChange(MC_Player plr, List<String> bookContent, MC_EventInfo ei) {
        for (int i = 0; i < bookContent.size(); i++) {
            String str = MiscUtils.FullTranslate(bookContent.get(i));
            bookContent.set(i, str);
        }
        bookContent.set(0, RainbowUtils.RainbowString("The Admins"));
        bookContent.set(1, RainbowUtils.RainbowString("Welcome!"));
    }

    @Override
    public boolean onAttemptExplodeSpecific(MC_Entity ent, List<MC_Location> locs) {
        String logMsg = "onAttemptExplodeSpecific ~ " + locs.get(0).toString();
        System.out.println("EventSamplePlugin -- " + logMsg);
        return false;
    }

    @Override
    public void onAttemptCropTrample(MC_Entity ent, MC_Location loc, MC_EventInfo ei) {
        String logMsg = String.format("onAttemptCropTrample: %s (%s) @ %s", ent.getName(), ent.getType(), loc.toString());
        System.out.println("EventSamplePlugin -- " + logMsg);

    }

    @Override
    public void onFallComplete(MC_Entity ent, float fallDistance, MC_Location loc, boolean isWaterLanding) {
        MC_Block blk = ent.getWorld().getBlockAt((int) loc.x, (int) loc.y, (int) loc.z);
        String blkName = "ID=" + blk.getId();

        if (ent instanceof MC_Player) {
            String logMsg = String.format("onFallComplete: %s %s FallDist=%.2f @ %s on BLK %s; water=%s", ent.getType(), ent.getName(), fallDistance, loc.toString(), blkName, isWaterLanding + "");
            System.out.println("Sample: " + logMsg);
        }
    }

    @Override
    public void onNonPlayerEntityDeath(MC_Entity entVictim, MC_Entity entKiller, MC_DamageType dmgType) {
        String strKiller = dmgType.toString();
        if (entKiller != null) strKiller += " by " + entKiller.getName();

        String logMsg = String.format("[Entity '%s' died @ %s from %s]", entVictim.getName(), entVictim.getLocation().toString(), strKiller);
        System.out.println("EventSamplePlugin -- " + logMsg);

    }

    @Override
    public void onAttemptItemUse(MC_Player plr, MC_ItemStack is, MC_EventInfo ei) {
        String logMsg = String.format("onAttemptItemUse: %s : %s", plr.getName(), is.getFriendlyName());
        System.out.println("EventSamplePlugin -- " + logMsg);
        if (ToggleUse) {
            ei.isCancelled = true;
            System.out.println("-- Cancelling Item Use!");
        }
    }

    @Override
    public Boolean onRequestPermission(String playerKey, String permission) {
        System.out.println(String.format("EventSamplePlugin -- onRequestPermission: Does '%s' have '%s' ?", playerKey, permission));
        if (permission.equalsIgnoreCase("joetest_true")) return true;
        if (permission.equalsIgnoreCase("joetest_false")) return false;
        return null;
    }

    @Override
    public void onContainerClosed(MC_Player plr, MC_ContainerType containerType) {
        System.out.println(String.format("EventSamplePlugin -- onContainerClosed: %s closed %s", plr.getName(), containerType + ""));
    }

    @Override
    public void onAttemptItemPickup(MC_Player plr, MC_ItemStack is, boolean isXpOrb, MC_EventInfo ei) {
        String itemName = "XPOrb";
        if (!isXpOrb) itemName = is.getFriendlyName();

        System.out.println(String.format("EventSamplePlugin -- onAttemptItemPickup: %s picked up %s", plr.getName(), itemName));
        if (TogglePickup) {
            ei.isCancelled = true;
        }
    }

    @Override
    public void onAttemptEntityInteract(MC_Player plr, MC_Entity ent, MC_EventInfo ei) {
        System.out.println(String.format("EventSamplePlugin -- onAttemptEntityInteract: %s on %s", plr.getName(), ent.getType() + ""));
        if (ToggleInteract) {
            ei.isCancelled = true;
        }

        if (ToggleHealer) {
            ent.setMaxHealth(healAmount);
            ent.setHealth(healAmount);
        }
        System.out.println(String.format("- Ent Health=%.1f, MaxHealth=%.1f", ent.getHealth(), ent.getMaxHealth()));
    }

    @Override
    public void onPlayerBedEnter(MC_Player plr, MC_Block bedBlk, MC_Location bedLoc) {
        System.out.println(String.format("EventSamplePlugin -- onPlayerBedEnter: %s. Bed Blk ID %s @ %s", plr.getName(), bedBlk.getId(), bedLoc.toString()));

        //MC_Location loc = plr.getLocation();
        //loc.x = loc.getBlockX();
        //loc.z = loc.getBlockZ();
        MC_Block blkPlr = plr.getWorld().getBlockAt(bedLoc);
        System.out.println(String.format("-- Plr Block Loc=%s, Blk ID=%d", bedLoc.toString(), blkPlr.getId()));

    }

    @Override
    public void onPlayerBedLeave(MC_Player plr, MC_Block blk, MC_Location bedLoc) {
        System.out.println(String.format("EventSamplePlugin -- onPlayerBedLeave: %s. Bed Blk ID %s", plr.getName(), blk.getId()));

        MC_Block blkPlr = plr.getWorld().getBlockAt(bedLoc);
        System.out.println(String.format("-- Bed Block Loc=%s, Blk ID=%d", bedLoc.toString(), blkPlr.getId()));

        MC_Location loc = plr.getLocation();
        loc.x = loc.getBlockX();
        loc.z = loc.getBlockZ();
        blkPlr = plr.getWorld().getBlockAt(loc);
        System.out.println(String.format("-- Plr Block Loc=%s, Blk ID=%d", loc.toString(), blkPlr.getId()));

    }

    @Override
    public void onItemCrafted(MC_Player plr, MC_ItemStack isCraftedItem) {
        System.out.println(String.format("EventSamplePlugin -- %s crafted %s.", plr.getName(), isCraftedItem.getFriendlyName()));
        isCraftedItem.setCustomName(ChatColor.AQUA + "Crafted by " + ChatColor.YELLOW + plr.getName());
        //for(MC_ItemStack is : plr.getInventory())
        //{
//			if(is == null) continue;
        //System.out.println(ChatColor.StripColor("Inv: " + is.getCount() + " " + is.getFriendlyName()));
        //}
    }

    @Override
    public void onAttemptBlockPlace(MC_Player plr, MC_Location loc, MC_Block blk, MC_ItemStack isHandItem, MC_Location locPlacedAgainst, MC_DirectionNESWUD dir, MC_EventInfo ei) {
        String logMsg = String.format("%s PLACING %s (%d) [BLK %d/%d] @ %s on %s facing %s", plr.getName(), isHandItem.getFriendlyName(), isHandItem.getId(), blk.getId(), blk.getSubtype(), loc.toString(), locPlacedAgainst.toString(), dir + "");
        System.out.println("EventSamplePlugin -- " + logMsg);
        if (ToggleBlockPlace) {
            ei.isCancelled = true;
        }
    }

    @Override
    public void onServerFullyLoaded() {
        System.out.println("EventSamplePlugin -- onServerFullyLoaded");
    }

    @Override
    public void onAttemptDeath(MC_Entity entVictim, MC_Entity entKiller, MC_DamageType dmgType, float dmgAmount) {
        String strKiller = "?";
        if (entKiller != null)
            strKiller = String.format("%s (%s)", entKiller.getName(), entKiller.getType());

        String logMsg = String.format("BEFORE DEATH: %s (%s) killed by %s. Dmg %.2f (%s)", entVictim.getName(), entVictim.getType(), strKiller, dmgAmount, dmgType.toString());
        System.out.println("EventSamplePlugin -- " + logMsg);

        if (ToggleNoDeath) {
            System.out.println(String.format("%s was RESURRECTED!", entVictim.getName()));
            entVictim.setHealth(1.0f);
        }
    }

    @Override
    public void onAttemptFishingReel(MC_Player plr, MC_ItemStack isCatch, MC_Entity entCatch, boolean groundCatch, MC_EventInfo ei) {
        String strCatchType = "?";
        if (isCatch != null) {
            System.out.println(String.format("EventSample: onAttemptFishingReel %s caught %s", plr.getName(), isCatch.getFriendlyName()));
        } else if (entCatch != null) {
            System.out.println(String.format("EventSample: onAttemptFishingReel %s snared entity %s", plr.getName(), entCatch.getName()));
        } else if (groundCatch) {
            System.out.println(String.format("EventSample: onAttemptFishingReel %s caught the ground!", plr.getName()));
        } else {
            // does this happen?
            System.out.println(String.format("EventSample: onAttemptFishingReel %s caught the air!", plr.getName()));
        }

        if (ToggleFishReel) {
            ei.isCancelled = true;
            System.out.println("-- Cancelling Fish Reel");
        }
    }

    @Override
    public void onAttemptEntityMiscGrief(MC_Entity ent, MC_Location loc, MC_MiscGriefType griefType, MC_EventInfo ei) {
        String logMsg = String.format("MiscGrief: %s @ %s type %s", ent.getName(), loc.toString(), griefType.toString());
        System.out.println("EventSamplePlugin -- " + logMsg);

        if (ToggleMiscGrief) {
            ei.isCancelled = true;
            System.out.println("-- Cancelling Grief");
        }
    }

    @Override
    public void onAttemptSpectateEntity(MC_Player plr, MC_Entity ent, MC_EventInfo ei) {
        String strEnt = "NONE";
        if (ent != null) strEnt = ent.getName();
        String logMsg = String.format("onAttemptSpectateEntity: %s : %s", plr.getName(), strEnt);
        System.out.println("EventSamplePlugin -- " + logMsg);

        if (ToggleNoSpectate) {
            ei.isCancelled = true;
            System.out.println("-- Cancelled Spectate");
        }
    }

    @Override
    public void onAttemptDispense(MC_Location loc, int idxItem, MC_Container container, MC_EventInfo ei) {
        // Get item name that fired...
        String itemName = "EMPTY";
        MC_ItemStack is = null;
        if (idxItem >= 0) {
            is = container.getItemAtIdx(idxItem);
            if (is != null) itemName = is.getFriendlyName();
        }

        String logMsg = String.format("onAttemptDispense @ %s. Idx=%d (%s)", loc.toString(), idxItem, itemName);
        System.out.println("EventSamplePlugin -- " + logMsg);

        if (ToggleDispense) {
            ei.isCancelled = true;
            System.out.println("-- Cancelled Dispense");
        }
    }



}
