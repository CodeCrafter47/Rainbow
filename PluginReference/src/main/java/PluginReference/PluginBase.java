package PluginReference;

import java.net.InetAddress;
import java.util.List;
import java.util.UUID;

/**
 * Base class for all Rainbow plugins.
 */

public abstract class PluginBase
{
	// Core events
	 /**
     * Called when a plugin is loaded. Save a reference to passed in MC_Server object.
     *
     * @param argServer Reference to Rainbow server object.
     */
	public void onStartup(MC_Server argServer) {}
	 /**
     * Called when server is shutting down
     */
	public void onShutdown()  {}
	 /**
     * Called every tick (1/20th of a second). If implementing, make sure this returns fast.
     *
     * @param tickNumber Tick Number
     */
	public void onTick(int tickNumber)  {}

	// Login/Logout...
	/**
     * Called when a player logs in. Note use onPlayerJoin if you need to interact with the player such as send messages.
     *
     * @param playerName Player Name
     * @param uuid Player UUID
     * @param ip Player IP Address
     */
	@Deprecated
	public void onPlayerLogin(String playerName, UUID uuid, String ip)  {}

	/**
	 * Called when a player logs in. Note use onPlayerJoin if you need to interact with the player such as send messages.
	 *
	 * If the player will be kicked because he is banned, a whitelist is active or the server is full,
	 * then ei.isCancelled is true and ei.tag holds the reason why the player will be disconnected.
	 *
	 * Setting ei.isCancelled to true will disconnect the player. If you do that set ei.tag to the disconnect reason
	 * displayed to the client.
	 *
	 * Setting ei.isCancelled to false will allow the player to connect, even if he is banned or the server is full.
	 *
	 * @param playerName Player Name
	 * @param uuid Player UUID
	 * @param address Remote player address
	 */
	public void onPlayerLogin(String playerName, UUID uuid, InetAddress address, MC_EventInfo ei)  {}

	 /**
     * Called when a player logs out
     *
     * @param playerName Player Name
     * @param uuid Player UUID
     */
	public void onPlayerLogout(String playerName, UUID uuid)  {}

	 /**
     * Called after an Interact (right-click) on something happened.
     *
     * @param plr Player Object
     * @param loc Location Interacted
     * @param isHandItem Item in Player's Hand
     */
	public void onInteracted(MC_Player plr, MC_Location loc, MC_ItemStack isHandItem)  {}

	 /**
     * Called after an item is placed.
     *
     * @param plr Player that placed object.
     * @param loc Location placed
     * @param isHandItem Item in Player's Hand
     * @param locPlacedAgainst Location item was placed against
     * @param dir Direction in which the item was placed
     */
	public void onItemPlaced(MC_Player plr, MC_Location loc, MC_ItemStack isHandItem, MC_Location locPlacedAgainst, MC_DirectionNESWUD dir)  {}

	 /**
     * [OLD VERSION] Called after a block was broken.
     *
     * @param plr Player that broke block.
     * @param loc Location of broken block
     * @param blockKey Minecraft integer representation of the block (See BlockHelper.getBlockID_FromKey)
     */
	@Deprecated
	public void onBlockBroke(MC_Player plr, MC_Location loc, int blockKey)  {}

	 /**
     * Called after a block was broken.
     *
     * @param plr Player that broke block.
     * @param loc Location of broken block
     * @param blk Block that was broken
     */
	public void onBlockBroke(MC_Player plr, MC_Location loc, MC_Block blk)  {}

	 /**
     * Called after a player death
     *
     * @param plrVictim Victim
     * @param plrKiller Killer (if there is one)
     * @param dmgType Damage Type
     * @param deathMsg Death Message
     */
	public void onPlayerDeath(MC_Player plrVictim, MC_Player plrKiller, MC_DamageType dmgType, String deathMsg)  {}

	/**
     * Called when a player respawns after death
     *
     * @param plr Player object
     */
	public void onPlayerRespawn(MC_Player plr)  {}

	// Cancellable events...
	 /**
     * Called for all player input. Useful if need to inspect input outside of registered commands.
     * Note: Use MC_Server.registerCommand for most plugin command interaction.
     *
     * @param plr Player object
     * @param msg Incoming Player Input
     * @param ei Event Info w/cancel option
     */
	public void onPlayerInput(MC_Player plr, String msg, MC_EventInfo ei)  {}
	 /**
     * Called for all console input.
     *
     * @param cmd Console Input
     * @param ei Event Info w/cancel option
     */
	public void onConsoleInput(String cmd, MC_EventInfo ei) {}

	 /**
     * Called when a player attempts a block break.
     *
     * @param plr Player Object
     * @param loc Location of Block
     * @param ei Event Info w/cancel option
     */
	public void onAttemptBlockBreak(MC_Player plr, MC_Location loc, MC_EventInfo ei)  {}
	 /**
     * Called when a player attempts to either place or interact with a block
     *
     * @param plr Player Object
     * @param loc Location of Block
     * @param ei Event Info w/cancel option
     * @param dir Direction of the interaction
	 * @deprecated this is only called for main hand interactions
     */
	@Deprecated
	public void onAttemptPlaceOrInteract(MC_Player plr, MC_Location loc, MC_EventInfo ei, MC_DirectionNESWUD dir)  {}
	/**
	 * Called when a player attempts to either place or interact with a block
	 *
	 * @param plr Player Object
	 * @param loc Location of Block
	 * @param dir Direction of the interaction
	 * @param hand Which hand the player uses
	 * @param ei Event Info w/cancel option
	 */
	public void onAttemptPlaceOrInteract(MC_Player plr, MC_Location loc, MC_DirectionNESWUD dir, MC_Hand hand, MC_EventInfo ei)  {}
	 /**
     * Called when an explosion occurs but not for individual blocks.
     *
     * @param loc Location of Block
     * @param ei Event Info w/cancel option
     */
	public void onAttemptExplosion(MC_Location loc, MC_EventInfo ei)  {}

	 /**
     * Called when an explosion occurs but you can selectively cancel individual blocks.
     * To prevent a block location from exploding, remove from the list.
     *
     * @param ent Entity involved in the explosion
     * @param locs List of block locations
     */
	public boolean onAttemptExplodeSpecific(MC_Entity ent, List<MC_Location> locs) {return false;}

	 /**
     * Called when a player attempts to damage a Painting or Item Frame
     *
     * @param plr Player Object
     * @param loc Location of Block
     * @param entType Type of hanging entity
     * @param ei Event Info w/cancel option
     */
	public void onAttemptDamageHangingEntity(MC_Player plr, MC_Location loc, MC_HangingEntityType entType, MC_EventInfo ei)  {}
	 /**
     * Called when a player attempts to interact with an Item Frame
     *
     * @param plr Player Object
     * @param loc Location of Block
     * @param actionType Action Type
     * @param ei Event Info w/cancel option
     */
	public void onAttemptItemFrameInteract(MC_Player plr, MC_Location loc, MC_ItemFrameActionType actionType , MC_EventInfo ei)  {}
	 /**
     * Called when a player is receiving a potion effect (from potion or beacon etc).
     *
     * @param plr Player Object
     * @param potionType Type of Effect
     * @param ei Event Info w/cancel option
     */
	public void onAttemptPotionEffect(MC_Player plr, MC_PotionEffectType potionType, MC_EventInfo ei)  {}
	 /**
     * Called when a player teleport is occurring.
     *
     * @param plr Player Object
     * @param loc Location of Block
     * @param ei Event Info w/cancel option
     */
	public void onAttemptPlayerTeleport(MC_Player plr, MC_Location loc, MC_EventInfo ei)  {}
	 /**
     * Called when a player changes dimension (Nether, TheEnd, etc)
     *
     * @param plr Player Object
     * @param newDimension Dimension IDX (0=world, 1=TheEnd, -1=Nether)
     * @param ei Event Info w/cancel option
     */
	public void onAttemptPlayerChangeDimension(MC_Player plr, int newDimension, MC_EventInfo ei)  {}
	 /**
     * Called when a player attempts to drop an item
     *
     * @param plr Player Object
     * @param is Item being dropped.
     * @param ei Event Info w/cancel option
     */
	public void onAttemptItemDrop(MC_Player plr, MC_ItemStack is, MC_EventInfo ei)  {}
	 /**
     * Called when a player attempts to attack an entity
     *
     * @param plr Player Object
     * @param ent Entity being attacked
     * @param ei Event Info w/cancel option
     */
	public void onAttemptAttackEntity(MC_Player plr, MC_Entity ent, MC_EventInfo ei)  {}
	 /**
     * Called when an entity is about to take damage.
     *
     * @param ent Entity Object
     * @param dmgType Damage Type
     * @param amt Amount of Damage
     * @param ei Event Info w/cancel option
     */
	public void onAttemptEntityDamage(MC_Entity ent, MC_DamageType dmgType, double amt, MC_EventInfo ei)  {} // replaced onAttemptPlayerTakeDamage

	 /**
     * Called when a new column of terrain needs generating in default world.
     *
     * @param x X-Coordinate of column
     * @param z Z-Coordinate of column
     * @param data Generated Column data
     */
	public void onGenerateWorldColumn(int x, int z, MC_GeneratedColumn data) {}

	 /**
     * Called when a piston fires
     *
     * @param loc Location of Piston
     * @param dir Direction of action
     * @param ei Event Info w/cancel option
     */
	public void onAttemptPistonAction(MC_Location loc, MC_DirectionNESWUD dir, MC_EventInfo ei)  {}
	 /**
     * Called when a block flows (i.e. water, lava)
     *
     * @param loc Location of Flow
     * @param blk Block being flowed
     * @param ei Event Info w/cancel option
     */
	public void onAttemptBlockFlow(MC_Location loc, MC_Block blk, MC_EventInfo ei)  {}

	 /**
     * Called when most containers is opened.
     * This event is not currently called for Crafting Table, Enchanting Table, Player Inventory, and Villager trades.
     *
     * @param plr Player object
     * @param items Items in container
     * @param internalClassName Internal Value for future use
     */
	public void onContainerOpen(MC_Player plr, List<MC_ItemStack> items, String internalClassName) {}

	 /**
     * Called when a player moves.  This is called frequently so plugins need to return quickly.
     *
     * @param plr Player object
     * @param locFrom Location moving from
     * @param locTo Location moving to
     * @param ei Event Info w/cancel option
     */
	public void onAttemptPlayerMove(MC_Player plr, MC_Location locFrom, MC_Location locTo, MC_EventInfo ei)  {}

	 /**
     * Called when a player is about to receive a sound effect from server.
     *
     * @param plr Player object
     * @param soundName Sound name
     * @param loc Location of sound
     * @param ei Event Info w/cancel option
     */
	public void onPacketSoundEffect(MC_Player plr, String soundName, MC_Location loc, MC_EventInfo ei)  {}

	 /**
     * Called after a player is joined and is able to interact and receive messages
     *
     * @param plr Player object
     */
	public void onPlayerJoin(MC_Player plr) {}

	 /**
     * Called when a sign is changing. Plugins can change lines
     *
     * @param plr Player object
     * @param sign MC_Sign object
     * @param loc Location of sign
     * @param newLines Sign text lines (can be modified)
     * @param ei Event Info w/cancel option
     */
	public void onSignChanging(MC_Player plr, MC_Sign sign, MC_Location loc, List<String> newLines, MC_EventInfo ei) {}
	 /**
     * Called after a sign update occurs
     *
     * @param plr Player object
     * @param sign MC_Sign object
     * @param loc Location of sign
     */
	public void onSignChanged(MC_Player plr, MC_Sign sign, MC_Location loc) {}

	 /**
     * Called by Rainbow to get details about your plugin.
     *
     * @return PluginInfo object
     */
	public PluginInfo getPluginInfo() { return null; }

	 /**
     * Called when an entity is about to spawn.
     *
     * @param ent Entity
     * @param ei Event Info w/cancel option
     */
	public void onAttemptEntitySpawn(MC_Entity ent, MC_EventInfo ei) {}

	 /**
     * Called when server has finished loading.
     * For example, after "Done." is shown in console.
     */
	public void onServerFullyLoaded() {}

	 /**
     * Called when a hopper is about to receive an item.
     *
     * @param loc Location of hopper
     * @param is Item involved
     * @param isMinecartHopper True if a Minecart Hopper, False otherwise
     * @param ei Event Info w/cancel option
     */
	public void onAttemptHopperReceivingItem(MC_Location loc, MC_ItemStack is, boolean isMinecartHopper, MC_EventInfo ei)  {}

	 /**
     * Called when a book is about to change.
     *
     * @param plr Player object
     * @param bookContent Array of book data. Index 0 is author, 1 is title, 2+ are page data. If not signed, author/title are null.
     * @param ei Event Info w/cancel option
     */
	public void onAttemptBookChange(MC_Player plr, List<String> bookContent, MC_EventInfo ei) {} // bookContent index: 0=author, 1=title, 2=page1, 3=page2, etc...  If not signed, author/title are null.

	 /**
     * Called when a farmland is about to get trampled
     *
     * @param ent Entity trampling
     * @param loc Location of farmland block (about to turn to DIRT)
     * @param ei Event Info w/cancel option
     */
	public void onAttemptCropTrample(MC_Entity ent, MC_Location loc, MC_EventInfo ei) {}
	 /**
     * Called when a player lands from a fall or jump.
     *
     * @param ent Entity trampling
     * @param fallDistance fall distance in blocks
     * @param loc Location of impact
     * @param isWaterLanding True if landed in water, false otherwise
     */
	public void onFallComplete(MC_Entity ent, float fallDistance, MC_Location loc, boolean isWaterLanding) {}
	 /**
     * Called when a player interacts with an Armor Stand
     *
     * @param plr Player interacting
     * @param entStand Armor Stand Entity
     * @param actionType Type of interaction
     * @param ei Event Info w/cancel option
     */
	public void onAttemptArmorStandInteract(MC_Player plr, MC_Entity entStand, MC_ArmorStandActionType actionType, MC_EventInfo ei)  {}

	 /**
     * Called when a non-player entity dies
     *
     * @param entVictim Victim that died
     * @param entKiller Killer (if exists)
     * @param dmgType Damage Type
     */
	public void onNonPlayerEntityDeath(MC_Entity entVictim, MC_Entity entKiller, MC_DamageType dmgType) {}

	 /**
     * Called when a player attempts to use an item, even into the air or not a valid item use.
     * One use is catching thrown objects before they're thrown or for inventing new uses for custom items.
     *
     * @param plr Player using item
     * @param is Item being used
     * @param ei Event Info w/cancel option
     */
	public void onAttemptItemUse(MC_Player plr, MC_ItemStack is, MC_EventInfo ei)  {}

	 /**
     * Called whenever a permission check occurs.  Override if you manage permissions.
     *
     * @param playerKey Permission Key (e.g. player name or UUID)
     * @param permission Permission String
     * @return True if allowed, False if not, Null means 'let Rainbow decide'
     */
	public Boolean onRequestPermission(String playerKey, String permission) { return null; }

	 /**
     * Called when any container closes, even player inventory
     *
     * @param plr Player closing container
     * @param containerType Type of container
     */
	public void onContainerClosed(MC_Player plr, MC_ContainerType containerType) {}
	 /**
     * Called when a player attempts to pickup an item
     *
     * @param plr Player making pick up
     * @param is Item being picked up (NULL for XP Orb pickups)
     * @param isXpOrb True if XP Orb, False otherwise
     * @param ei Event Info w/cancel option
     */
	public void onAttemptItemPickup(MC_Player plr, MC_ItemStack is, boolean isXpOrb, MC_EventInfo ei) {}

	 /**
     * Called when a player interacts with an entity
     *
     * @param plr Player interacting
     * @param ent Entity being interacted with
     * @param ei Event Info w/cancel option
     */
	public void onAttemptEntityInteract(MC_Player plr, MC_Entity ent, MC_EventInfo ei) {}

	 /**
     * Called after a player finished crafting something.
     *
     * @param plr Player object
     * @param isCraftedItem Item Crafted
     */
	public void onItemCrafted(MC_Player plr, MC_ItemStack isCraftedItem) {}


	 /**
     * Called when a player starts sleeping
     *
     * @param plr Player object
     * @param blk Bed Block
     */
	//public void onPlayerBedEnter(MC_Player plr, MC_Block blk) {}

	 /**
     * Called when a player starts sleeping
     *
     * @param plr Player object
     * @param blkBed Bed Block
     * @param locBed Location of Bed Block
     */
	public void onPlayerBedEnter(MC_Player plr, MC_Block blkBed, MC_Location locBed) {}

	 /**
     * Called when a player stops sleeping
     *
     * @param plr Player object
     * @param blkBed Bed Block
     * @param locBed Location of Bed Block
     */
	public void onPlayerBedLeave(MC_Player plr, MC_Block blkBed, MC_Location locBed) {}


	 /**
     * Called before a block is placed.
     *
     * @param plr Player that placed object.
     * @param loc Location placed
     * @param blk Potential Block
     * @param isHandItem Item in Player's Hand
     * @param locPlacedAgainst Location item was placed against
     * @param dir Direction in which the item was placed
     * @param ei Event Info w/cancel option
     */
	public void onAttemptBlockPlace(MC_Player plr, MC_Location loc, MC_Block blk, MC_ItemStack isHandItem, MC_Location locPlacedAgainst, MC_DirectionNESWUD dir, MC_EventInfo ei)  {}

	 /**
     * Called before a living entity dies
     *
     * @param entVictim Victim
     * @param entKiller Killer (if there is one)
     * @param dmgType Damage Type
     * @param dmgAmount Damage Amount
     */
	public void onAttemptDeath(MC_Entity entVictim, MC_Entity entKiller, MC_DamageType dmgType, float dmgAmount)  {}

	 /**
     * Called when fishing rod is reeled in but before action taken.
     *
     * @param plr Player fishing
     * @param isCatch If catching item, the item. Otherwise null.
     * @param entCatch If reeling an entity, the entity. Otherwise null.
     * @param groundCatch True if cork was stuck in ground when reeled in.
     * @param ei Event Info w/cancel option. A cancel will avoid item award or entity pull back
     */
	public void onAttemptFishingReel(MC_Player plr, MC_ItemStack isCatch, MC_Entity entCatch, boolean groundCatch, MC_EventInfo ei)  {}


	 /**
     * Called when there is a 'miscellaneous' entity grief not specifically handled elsewhere.
     *
     * @param ent Entity griefing
     * @param loc Location of grief
     * @param griefType Type of grief
     * @param ei Event Info w/cancel option.
     */
	public void onAttemptEntityMiscGrief(MC_Entity ent, MC_Location loc, MC_MiscGriefType griefType, MC_EventInfo ei) {}

	 /**
     * Called when a spectator attempts to spectate an entity (or cancel spectating)
     *
     * @param plr Player attempting spectate
     * @param ent Entity target (if null, cancelling spectate)
     * @param ei Event Info w/cancel option
     */
	public void onAttemptSpectateEntity(MC_Player plr, MC_Entity ent, MC_EventInfo ei) {}

	 /**
     * Called when an item is about to be eject from a dispenser or dropper.
     * If index is negative, it's an 'empty fire'.
     *
     * @param loc Location of dispenser
     * @param idxItem Index of item to be dispensed
     * @param container Container interface for get/set items inside dispenser
     * @param ei Event Info w/cancel option
     */
	public void onAttemptDispense(MC_Location loc, int idxItem, MC_Container container, MC_EventInfo ei) {}

	/**
	 * Called when an entity is pushed by another entity.
	 *
	 * @param entity entity
	 * @param pushedEntity pushed entity
	 * @param velocity velocity applied to the pushed entity, can be changed
     * @param ei event info, can be cancelled
     */
	public void onEntityPushed(MC_Entity entity, MC_Entity pushedEntity, MC_FloatTriplet velocity, MC_EventInfo ei) {}

	/**
	 * Called when a projectile is about to hit an entity.
	 *
	 * Canceling the event will cause the projectile to fly through the entity rather than hit it. It will however not
	 * remove the projectile from the world. Use {@link MC_Entity#removeEntity()} to remove the projectile from the world.
	 *  @param projectile the projectile
	 * @param entity the entity which is hit
	 * @param hitLocation the location where the entity was hit
	 * @param ei event info, can be cancelled
	 */
    public void onAttemptProjectileHitEntity(MC_Projectile projectile, MC_Entity entity, MC_Location hitLocation, MC_EventInfo ei) {
    }

    /**
     * Called when a projectile is about to hit a block.
     * <p>
     * Canceling the event will cause the projectile to fly through the block rather than hit it. It will however not
     * remove the projectile from the world. Use {@link MC_Entity#removeEntity()} to remove the projectile from the world.
     *  @param projectile    the projectile
     * @param blockLocation the location of the block which is hit
	 * @param blockFaceHit  black face that has been hit
	 * @param hitLocation   the location where the block was hit
	 * @param ei            event info, can be cancelled
	 */
    public void onAttemptProjectileHitBlock(MC_Projectile projectile, MC_Location blockLocation, MC_DirectionNESWUD blockFaceHit, MC_Location hitLocation, MC_EventInfo ei) {
    }
}

