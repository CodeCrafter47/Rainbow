package PluginReference;

import java.util.List;

/** 
 * Represents the Minecraft Server
 */ 			
public interface MC_Server
{
	 /** 
     * Get list of online players
     * 
     * @return List of player objects 
     */ 				
	public List<MC_Player> getPlayers();
	 /** 
     * Get list of offline players.
     * 
     * @return List of offline player objects 
     */ 			
	public List<MC_Player> getOfflinePlayers();
	 /** 
     * Get list of info about loaded plugins
     * 
     * @return List of PluginInfo objects 
     */ 			
	public List<PluginInfo> getPlugins();
	 /** 
     * Get a player object of an online player by name
     * 
     * @param pName Player Name
     * @return Player object 
     */ 			
	public MC_Player getOnlinePlayerByName(String pName);

	/** 
     * Get list of player names matching substring. Useful for implementing Tab feature of commands.
     * 
     * @param subString Substring for search
     * @return List of player names 
     */ 				
	public List<String> getMatchingOnlinePlayerNames(String subString);

	 /** 
     * Send a message to all players.
     * 
     * @param msg Message to send
     */ 				
	public void broadcastMessage(String msg);
	 /** 
     * Execute a command as Console
     * 
     * @param cmd Command to Execute
     */ 		
	public void executeCommand(String cmd);
		
	 /** 
     * Get list of permissions for a given key
     * 
     * @param plrKey Permission Key (i.e. player name or UUID)
     * @return List of permissions 
     */ 			
	public List<String> getPermissions(String plrKey);
	 /** 
     * Give a permission to a key
     * 
     * @param plrKey Key such as player name or UUID
     * @param perm Permission string
     */ 		
	public void givePermission(String plrKey, String perm);
	 /** 
     * Take a permission away from a key
     * 
     * @param plrKey Key such as player name or UUID
     * @param perm Permission string
     */ 		
	public void takePermission(String plrKey, String perm);
	 /** 
     * Check if key has permission
     * 
     * @param plrKey Player key (i.e. name or UUID)
     * @param perm Permission
     * @return True if has permission, False otherwise 
     */ 			
	public boolean hasPermission(String plrKey, String perm);
	 /** 
     * Clear all permission data
     */ 		
	public void clearAllPermissions();

	 /** 
     * Get the server spawn protection radius (in number of blocks)
     * 
     * @return Radius in number of blocks 
     */ 			
	public int getSpawnProtectionRadius(); // range of spawn protection
	 /** 
     * Get Server Port, i.e. 25565
     * 
     * @return Server port number 
     */ 			
	public int getServerPort(); // i.e. 25565
	 /** 
     * Get maximum build height (i.e. 255)
     * 
     * @return Maximum build height 
     */ 			
	public int getMaxBuildHeight();
	
	 /** 
     * Get number of seconds players must wait before reconnecting
     * 
     * @return Number of Seconds 
     */ 			
	public int getReconnectDelaySeconds();	// how long players have to wait before being able to reconnect
	 /** 
     * Set the delay required before a player can reconnect
     * 
     * @param val Reconnect Delay in seconds
     */ 		
	public void setReconnectDelaySeconds(int val);
	 /** 
     * Sets custom message players see if server closes gracefully
     * 
     * @param msg Message e.g. "We'll be right back"
     */ 		
	public void setCustomShutdownMessage(String msg); // Custom shutdown message, like "We'll be right back"
	

	 /** 
     * Returns player's exact name given a case-insensitive one. Only works for players who have logged in before.
     * 
     * @param pName Player name
     * @return Exact case of player name or NULL if not found.
     */ 			
	public String getPlayerExactName(String pName); // Only works for players who have logged in before

	 /** 
     * Get UUID of player by name
     * 
     * @param pName player name
     * @return UUID of player 
     */ 				
	public String getPlayerUUIDFromName(String pName); // ...
	 /** 
     * Get list of player names associated with a given UUID.  Should be only one entry until name changing is supported.
     * 
     * @param uid UUID to inspect
     * @return List of player names seen with that UUID 
     */ 			
	public List<String> getPlayerNamesFromUUID(String uid); // ...
	 /** 
     * Gets the last known player name associated with a UUID
     * 
     * @param uid UUID of player
     * @return Last known player name 
     */ 			
	public String getLastKnownPlayerNameFromUUID(String uid);  // ...

	 /** 
     * Get World object from dimension number
     * 
     * @param idxDimension Dimension Number
     * @return MC_World object 
     */ 			
	public MC_World getWorld(int idxDimension); 
	 /** 
     * Create an MC_ItemStack given id, count, and dmg/subtype value
     * 
     * @param id Item ID
     * @param count Stack Count
     * @param dmg Damage or Subtype
     * @return MC_ItemStack object 
     */ 			
	public MC_ItemStack createItemStack(int id, int count, int dmg);
	 /** 
     * Construct an MC_ItemStack given raw byte data
     * 
     * @param rawItemData Raw data from calling .serialize() on MC_ItemStack
     * @return MC_ItemStack object 
     */ 			
	public MC_ItemStack createItemStack(byte[] rawItemData); // raw data from calling .serialize() on MC_ItemStack

	 /** 
     * Register a custom plugin command
     * 
     * @param cmd Object implementing MC_Command
     */ 		
	public void registerCommand(MC_Command cmd);
	
	 /** 
     * Check if online mode
     * @return True if online mode, False otherwise
     */ 		
	public boolean getOnlineMode();

	 /** 
     * Gets Rainbow version number
     * @return Version number
     */ 		
	public double getRainbowVersion();
	
	 /** 
     * Get current filename of server icon
     * 
     * @return Server icon filename 
     */ 			
	public String getServerIconFilename();
	 /** 
     * Sets server icon players see before joining
     * 
     * @param arg Server Icon
     */ 		
	public void setServerIconFilename(String arg);
	 /** 
     * Get current server MOTD (Message of the Day)
     * 
     * @return Message of the Day 
     */ 			
	public String getServerMOTD();
	 /** 
     * Sets server MOTD players see before joining
     * 
     * @param arg Message of the Day
     */ 		
	public void setServerMOTD(String arg);

	 /** 
     * Register a handler to intercept outgoing network packets to player
     * 
     * @param myListener Object implementing MC_PlayerPacketListener
     */
	@Deprecated
	public void registerPlayerPacketListener(MC_PlayerPacketListener myListener); 

	 /** 
     * Register a handler to intercept incoming network packets to server
     * 
     * @param myListener Object implementing MC_ServerPacketListener
     */
	@Deprecated
	public void registerServerPacketListener(MC_ServerPacketListener myListener); 
	
	 /** 
     * Gets info about the currently executing command (or the last command if called from a non-command event).
     * 
     * @return Command Sender Info 
     */ 		
	public MC_CommandSenderInfo getExecutingCommandInfo();
	
	 /** 
     * Get an MC_Block from a block name
     * 
     * @param name Block name
     * @return MC_Block object 
     */ 		
	public MC_Block getBlockFromName(String name);

	 /** 
     * Logs a message both to console and server logs
     * 
     * @param msg Message
     */ 		
	public void log(String msg);

	/**
	 * Register a world name. If already exists, returns existing identifier.
     * 
     * @param worldName World Name
     * @return Dimension (Integer identifier) for this world
	 */
	@Deprecated
	public int registerWorld(String worldName, MC_WorldSettings settings);

	/**
	 * Unregister a world
     * 
     * @param worldName World Name
     * @return True if unregistered, False if unrecognized
	 */
	@Deprecated
	public boolean unregisterWorld(String worldName);
	

	 /** 
     * Get list of world objects
     * 
     * @return List of MC_World objects 
     */ 			
	public List<MC_World> getWorlds();
	
}

