package PluginReference;

import net.md_5.bungee.api.chat.BaseComponent;

import java.net.SocketAddress;
import java.util.List;

/**
 * Represents a Minecraft player
 */
public interface MC_Player extends MC_LivingEntity {
    public String getIPAddress();

    // Operations

    /**
     * Sends a a message to player
     *
     * @param msg Message
     */
    public void sendMessage(String msg);

    /**
     * Sends message to player
     *
     * @param msg Message
     */
    public void sendMessage(BaseComponent... msg);

    /**
     * Sends a message to player
     *
     * @param json Message using Minecrafts json massage format.
     */
    public void sendJsonMessage(String json);

    /**
     * Execute a command (as if user typed it)
     *
     * @param cmd Command to Execute
     */
    public void executeCommand(String cmd);

    // Details

    /**
     * Check if op
     *
     * @return True if op, False otherwise
     */
    public boolean isOp();

    /**
     * Get Player GameMode
     *
     * @return Game Mode
     */
    public MC_GameMode getGameMode();

    /**
     * Sets player's GameMode
     *
     * @param gameMode Game Mode
     */
    public void setGameMode(MC_GameMode gameMode);


    /**
     * Get Player current health
     *
     * @return Health value
     */
    public float getHealth();

    /**
     * Sets Player Health
     *
     * @param argHealth Health value
     */
    public void setHealth(float argHealth);

    /**
     * Get Food Level
     *
     * @return Food Level
     */
    public int getFoodLevel();

    /**
     * Sets Player Food Level
     *
     * @param argFoodLevel Food Level
     */
    public void setFoodLevel(int argFoodLevel);

    /**
     * Get Economy balance (as managed by Rainbow)
     *
     * @return Economy balance
     */
    public double getEconomyBalance();

    /**
     * Sets Economy balance
     *
     * @param amt New Balance
     */
    public void setEconomyBalance(double amt);

    /**
     * Get Item in hand
     *
     * @return Item in hand
     */
    public MC_ItemStack getItemInHand();

    /**
     * Sets item in hand
     *
     * @param item Item
     */
    public void setItemInHand(MC_ItemStack item);

    /**
     * Get inventory of player
     *
     * @return List of items
     */
    public List<MC_ItemStack> getInventory();

    /**
     * Sets inventory
     *
     * @param items List of items
     */
    public void setInventory(List<MC_ItemStack> items);

    /**
     * Notify player inventory has changed
     */
    public void updateInventory();


    // Miscellaneous

    /**
     * Check if invulnerable
     *
     * @return True if invulnerable, False otherwise
     */
    public boolean isInvulnerable();

    /**
     * Check if sleeping
     *
     * @return True if sleeping, False otherwise
     */
    public boolean isSleeping();

    /**
     * Check if allow flight
     *
     * @return True if allow flight, False otherwise
     */
    public boolean isAllowedFlight();

    /**
     * Check if flying
     *
     * @return True if flying, False otherwise
     */
    public boolean isFlying();

    /**
     * Get fly speed
     *
     * @return Fly speed
     */
    public float getFlySpeed();

    /**
     * Get walk speed
     *
     * @return Walk speed
     */
    public float getWalkSpeed();

    /**
     * Sets Fly Speed
     *
     * @param newVal Fly Speed
     */
    public void setFlySpeed(float newVal);

    /**
     * Sets Walk Speed
     *
     * @param newVal Walk Speed
     */
    public void setWalkSpeed(float newVal);

    /**
     * Sets Invulnerability
     *
     * @param flag True if invulnerable, False otherwise
     */
    public void setInvulnerable(boolean flag);

    /**
     * Sets Allow Flight
     *
     * @param flag True if allows flight, False otherwise
     */
    public void setAllowFlight(boolean flag);

    /**
     * Sets Flying mode
     *
     * @param flag True if flying, False otherwise
     */
    public void setFlying(boolean flag);

    /**
     * Give an amount of EXP
     *
     * @param exp Amount of EXP to give
     */
    public void giveExp(int exp);

    /**
     * Give an amount of EXP Levels
     *
     * @param levels Number of levels to give
     */
    public void giveExpLevels(int levels);

    /**
     * Get EXP value
     *
     * @return EXP Value
     */
    public float getExp();

    /**
     * Sets EXP value
     *
     * @param exp EXP value
     */
    public void setExp(float exp);

    /**
     * Get EXP Level
     *
     * @return EXP Level
     */
    public int getLevel();

    /**
     * Sets EXP Level
     *
     * @param level EXP level
     */
    public void setLevel(int level);

    /**
     * Get total EXP
     *
     * @return Total Experience
     */
    public int getTotalExperience();

    /**
     * Sets Total EXP
     *
     * @param exp Total EXP
     */
    public void setTotalExperience(int exp);

    /**
     * Get Server object (convenience function)
     *
     * @return Server object
     */
    public MC_Server getServer();

    /**
     * Check if player has a certani permission
     *
     * @param perm Permission to check
     * @return True if yes, False if no.
     */
    public boolean hasPermission(String perm);

    /**
     * Sets Compass Target of player
     *
     * @param loc Location of compass target
     */
    public void setCompassTarget(MC_Location loc);

    /**
     * Get the Compass Target for this player
     *
     * @return Location of compass target
     */
    public MC_Location getCompassTarget();

    /**
     * Gets the respawn/bed location for this player
     *
     * @return Location of respawn
     */
    public MC_Location getBedRespawnLocation();

    /**
     * Sets the respawn/bed location for this player
     * Only Dimension 0 (OverWorld) supported at this time.
     *
     * @param loc                       Respawn location
     * @param requireBedBlockAtLocation If true, require a bed at location. Otherwise user taken to world spawn
     */
    public void setBedRespawnLocation(MC_Location loc, boolean requireBedBlockAtLocation);


    /**
     * Play a sound to a player
     *
     * @param soundName Sound Name, e.g. random.click
     * @param volume    Volume
     * @param pitch     Pitch
     */
    public void playSound(String soundName, float volume, float pitch);


    /**
     * Gets health added when not hungry. Default 1.0
     *
     * @return Location of respawn
     */
    public float getFoodRegenAmount();

    /**
     * Sets health added when not hungry. Default 1.0
     *
     * @param val Health amount restored
     */
    public void setFoodRegenAmount(float val);

    /**
     * Kick player from the server
     *
     * @param msg Disconnect message
     */
    public void kick(String msg);

    /**
     * Gets Socket address
     *
     * @return Gets socket address of player
     */
    public SocketAddress getSocketAddress();

    /**
     * Retrieve the entity this player is spectating
     *
     * @return Entity being spectated
     */
    public MC_Entity getEntitySpectated();

    /**
     * Sets entity spectated
     *
     * @param ent Entity to spectate
     */
    public void spectateEntity(MC_Entity ent);

    // 1.9 stuff...

    /**
     * Get Item in off-hand
     *
     * @return Item in off-hand
     */
    public MC_ItemStack getItemInOffHand();

    /**
     * Sets item in off-hand
     *
     * @param item Item
     */
    public void setItemInOffHand(MC_ItemStack item);

    /**
     * Set the header and footer of the player list (tab overlay). The header and footer arguments are provided as
     * plain text and may contain legacy color codes.
     *
     * @param header the header
     * @param footer the footer
     */
    void setPlayerListHeaderFooter(String header, String footer);

    /**
     * Display an inventory gui to the player.
     *
     * @param gui the gui
     */
    void displayInventoryGUI(MC_InventoryGUI gui);

    /**
     * Closes any open inventory.
     */
    void closeInventory();
}

