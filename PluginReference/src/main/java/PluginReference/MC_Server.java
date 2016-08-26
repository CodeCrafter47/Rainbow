package PluginReference;

import java.util.List;
import java.util.UUID;

/**
 * Represents the Minecraft Server
 */
public interface MC_Server {

    /**
     * Get list of online players
     *
     * @return List of player objects
     */
    List<MC_Player> getPlayers();

    /**
     * Get list of offline players.
     *
     * @return List of offline player objects
     */
    List<MC_Player> getOfflinePlayers();

    /**
     * Get list of info about loaded plugins
     *
     * @return List of PluginInfo objects
     */
    List<PluginInfo> getPlugins();

    /**
     * Get plugin info for a specific plugin
     *
     * @param name the plugins name
     * @return plugin info
     */
    PluginInfo getPluginInfo(String name);

    /**
     * Get a player object of an online player by name
     *
     * @param pName Player Name
     * @return Player object
     */
    MC_Player getOnlinePlayerByName(String pName);

    /**
     * Get list of player names matching substring. Useful for implementing Tab feature of commands.
     *
     * @param subString Substring for search
     * @return List of player names
     */
    List<String> getMatchingOnlinePlayerNames(String subString);

    /**
     * Send a message to all players.
     *
     * @param msg Message to send
     */
    void broadcastMessage(String msg);

    /**
     * Execute a command as Console
     *
     * @param cmd Command to Execute
     */
    void executeCommand(String cmd);

    /**
     * Get list of permissions for a given key
     *
     * @param plrKey Permission Key (i.e. player name or UUID)
     * @return List of permissions
     */
    List<String> getPermissions(String plrKey);

    /**
     * Give a permission to a key
     *
     * @param plrKey Key such as player name or UUID
     * @param perm   Permission string
     */
    void givePermission(String plrKey, String perm);

    /**
     * Take a permission away from a key
     *
     * @param plrKey Key such as player name or UUID
     * @param perm   Permission string
     */
    void takePermission(String plrKey, String perm);

    /**
     * Check if key has permission
     *
     * @param plrKey Player key (i.e. name or UUID)
     * @param perm   Permission
     * @return True if has permission, False otherwise
     */
    boolean hasPermission(String plrKey, String perm);

    /**
     * Clear all permission data
     */
    void clearAllPermissions();

    /**
     * Get the server spawn protection radius (in number of blocks)
     *
     * @return Radius in number of blocks
     */
    int getSpawnProtectionRadius(); // range of spawn protection

    /**
     * Get Server Port, i.e. 25565
     *
     * @return Server port number
     */
    int getServerPort(); // i.e. 25565

    /**
     * Get maximum build height (i.e. 255)
     *
     * @return Maximum build height
     */
    int getMaxBuildHeight();

    /**
     * Get number of seconds players must wait before reconnecting
     *
     * @return Number of Seconds
     */
    int getReconnectDelaySeconds();    // how long players have to wait before being able to reconnect

    /**
     * Set the delay required before a player can reconnect
     *
     * @param val Reconnect Delay in seconds
     */
    void setReconnectDelaySeconds(int val);

    /**
     * Sets custom message players see if server closes gracefully
     *
     * @param msg Message e.g. "We'll be right back"
     */
    void setCustomShutdownMessage(String msg); // Custom shutdown message, like "We'll be right back"


    /**
     * Returns player's exact name given a case-insensitive one. Only works for players who have logged in before.
     *
     * @param pName Player name
     * @return Exact case of player name or NULL if not found.
     */
    String getPlayerExactName(String pName); // Only works for players who have logged in before

    /**
     * Get UUID of player by name
     *
     * @param pName player name
     * @return UUID of player
     */
    String getPlayerUUIDFromName(String pName); // ...

    /**
     * Get list of player names associated with a given UUID.  Should be only one entry until name changing is supported.
     *
     * @param uid UUID to inspect
     * @return List of player names seen with that UUID
     */
    List<String> getPlayerNamesFromUUID(String uid); // ...

    /**
     * Gets the last known player name associated with a UUID
     *
     * @param uid UUID of player
     * @return Last known player name
     */
    String getLastKnownPlayerNameFromUUID(String uid);  // ...

    /**
     * Get World object from dimension number
     *
     * @param idxDimension Dimension Number
     * @return MC_World object
     */
    MC_World getWorld(int idxDimension);

    /**
     * Create an MC_ItemStack given id, count, and dmg/subtype value
     *
     * @param id    Item ID
     * @param count Stack Count
     * @param dmg   Damage or Subtype
     * @return MC_ItemStack object
     */
    MC_ItemStack createItemStack(int id, int count, int dmg);

    /**
     * Construct an MC_ItemStack given raw byte data
     *
     * @param rawItemData Raw data from calling .serialize() on MC_ItemStack
     * @return MC_ItemStack object
     */
    MC_ItemStack createItemStack(byte[] rawItemData); // raw data from calling .serialize() on MC_ItemStack

    /**
     * Register a custom plugin command
     *
     * @param cmd Object implementing MC_Command
     */
    void registerCommand(MC_Command cmd);

    /**
     * Check if online mode
     *
     * @return True if online mode, False otherwise
     */
    boolean getOnlineMode();

    /**
     * Gets Rainbow version number
     *
     * @return Version number
     */
    double getRainbowVersion();

    /**
     * Get current filename of server icon
     *
     * @return Server icon filename
     */
    String getServerIconFilename();

    /**
     * Sets server icon players see before joining
     *
     * @param arg Server Icon
     */
    void setServerIconFilename(String arg);

    /**
     * Get current server MOTD (Message of the Day)
     *
     * @return Message of the Day
     */
    String getServerMOTD();

    /**
     * Sets server MOTD players see before joining
     *
     * @param arg Message of the Day
     */
    void setServerMOTD(String arg);

    /**
     * Register a handler to intercept outgoing network packets to player
     *
     * @param myListener Object implementing MC_PlayerPacketListener
     */
    @Deprecated
    void registerPlayerPacketListener(MC_PlayerPacketListener myListener);

    /**
     * Register a handler to intercept incoming network packets to server
     *
     * @param myListener Object implementing MC_ServerPacketListener
     */
    @Deprecated
    void registerServerPacketListener(MC_ServerPacketListener myListener);

    /**
     * Gets info about the currently executing command (or the last command if called from a non-command event).
     *
     * @return Command Sender Info
     */
    MC_CommandSenderInfo getExecutingCommandInfo();

    /**
     * Get an MC_Block from a block name
     *
     * @param name Block name
     * @return MC_Block object
     */
    MC_Block getBlockFromName(String name);

    /**
     * Get an MC_Block from a block id
     *
     * @param id Block id
     * @return MC_Block object
     */
    MC_Block getBlock(int id);

    /**
     * Get an MC_Block from a block id and subtype
     *
     * @param id      Block id
     * @param subtype Block subtype
     * @return MC_Block object
     */
    MC_Block getBlock(int id, int subtype);

    /**
     * Logs a message both to console and server logs
     *
     * @param msg Message
     */
    void log(String msg);

    /**
     * Register a world name. If already exists, returns existing identifier.
     *
     * @param worldName World Name
     * @return Dimension (Integer identifier) for this world
     */
    @Deprecated
    int registerWorld(String worldName, MC_WorldSettings settings);

    /**
     * Unregister a world
     *
     * @param worldName World Name
     * @return True if unregistered, False if unrecognized
     */
    @Deprecated
    boolean unregisterWorld(String worldName);


    /**
     * Get list of world objects
     *
     * @return List of MC_World objects
     */
    List<MC_World> getWorlds();

    /**
     * Add a shaped recipe
     * <p>
     * e.g the following code sample adds a recipe for crafting a stone pickaxe
     * using clean stone.
     * <pre>
     * {@code
     * MC_Server server = RainbowUtils.getServer();
     * server.addRecipe(server.createItemStack(MC_ID.ITEM_STONE_PICKAXE, 1, 0),
     *                  "XXX", " I ", " I ",
     *                  'X', server.createItemStack(MC_ID.BLOCK_STONE, 1, 0),
     *                  'I', server.createItemStack(MC_ID.ITEM_STICK, 1, 0));
     * }
     * </pre>
     *
     * @param result      craft result
     * @param ingredients recipe
     */
    void addRecipe(MC_ItemStack result, Object... ingredients);

    /**
     * Add a shapeless recipe.
     * <p>
     * e.g the following code sample adds a recipe that turns rotten flesh into
     * leather.
     * <pre>
     * {@code
     * MC_Server server = RainbowUtils.getServer();
     * server.addShapelessRecipe(server.createItemStack(MC_ID.ITEM_LEATHER, 1, 0),
     *                           server.createItemStack(MC_ID.ITEM_ROTTEN_FLESH, 1, 0));
     * }
     * </pre>
     *
     * @param result
     * @param ingredients
     */
    void addShapelessRecipe(MC_ItemStack result, MC_ItemStack... ingredients);

    /**
     * Factory method to create an attribute modifier with a random unique uuid.
     *
     * @param name     the name of the attribute modifier
     * @param operator the operator to use
     * @param value    the value of the modifier
     * @return the created attribute modifier
     */
    MC_AttributeModifier createAttributeModifier(String name, MC_AttributeModifier.Operator operator, double value);

    /**
     * Factory method to create an attribute modifier using a given uuid.
     *
     * @param uuid     the uuid to use
     * @param name     the name of the attribute modifier
     * @param operator the operator to use
     * @param value    the value of the modifier
     * @return the created attribute modifier
     */
    MC_AttributeModifier createAttributeModifier(UUID uuid, String name, MC_AttributeModifier.Operator operator, double value);

    /**
     * Create an inventory for use as GUI.
     *
     * @param size  the size of the inventory, must be a multiple of 9
     * @param title the title of the inventory
     * @return the {@link MC_InventoryGUI} instance
     */
    MC_InventoryGUI createInventoryGUI(int size, String title);
}

