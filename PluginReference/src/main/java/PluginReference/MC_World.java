package PluginReference;

import java.util.List;

/** 
 * Represents a World (or dimension) and allows operations to be performed.
 */ 			
public interface MC_World
{
	 /** 
     * Get an MC_Block from a block name
     * 
     * @param name Block name
     * @return MC_Block object
	 * @deprecated use MC_Server#getBlockFromName instead
     */ 		
	@Deprecated
	public MC_Block getBlockFromName(String name);

	 /** 
     * Get an MC_Block object at location
     * 
     * @param x X-Coordinate
     * @param y Y-Coordinate
     * @param z Z-Coordinate
     * @return MC_Block object 
     */ 			
	public MC_Block getBlockAt(int x, int y, int z); 

	 /** 
     * Get direction a block is facing (or null if doesn't support)
     * 
     * @param x X-Coordinate
     * @param y Y-Coordinate
     * @param z Z-Coordinate
     * @return Direction block is facing (or null if doesn't support) 
     */ 			
	public MC_DirectionNESWUD getBlockFacing(int x, int y, int z); 

	 /** 
     * Set block rotation (signs, banners, etc)
     * 
     * @param x X-Coordinate
     * @param y Y-Coordinate
     * @param z Z-Coordinate
     * @param rotationValue rotation value (0-15) 
     */ 			
	public void setBlockRotation(int x, int y, int z, int rotationValue); 

	
	 /** 
     * Set direction a block is facing (or null if doesn't support)
     * 
     * @param x X-Coordinate
     * @param y Y-Coordinate
     * @param z Z-Coordinate
     * @param dir Direction block is facing 
     */ 			
	public void setBlockFacing(int x, int y, int z, MC_DirectionNESWUD dir); 

	 /** 
     * Get block rotation (signs, banners, etc)
     * 
     * @param x X-Coordinate
     * @param y Y-Coordinate
     * @param z Z-Coordinate
     * @return Gets rotation value for signs and banners 
     */ 			
	public int getBlockRotation(int x, int y, int z); 
	
	
	 /** 
     * Get an MC_Block object at location
     * 
     * @param loc Location
     * @return MC_Block object 
     */ 			
	public MC_Block getBlockAt(MC_Location loc); 
	
	
	 /** 
     * Set block at location
     * 
     * @param x X-Coordinate
     * @param y Y-Coordinate
     * @param z Z-Coordinate
     * @param blk MC_Block object
     * @param subType block Subtype
     */ 			
	public void setBlockAt(int x, int y, int z, MC_Block blk, int subType);

	 /** 
     * Set block at location
     * 
     * @param loc Location
     * @param blk MC_Block object
     * @param subType block Subtype
     */ 			
	public void setBlockAt(MC_Location loc, MC_Block blk, int subType);
	
	 /** 
     * Break block at location and trigger item drop
     * 
     * @param x X-Coordinate
     * @param y Y-Coordinate
     * @param z Z-Coordinate
     * @return True if success, False otherwise 
     */ 			
	public boolean breakNaturallyAt(int x, int y, int z);
	 /** 
     * Break block at location as if given tool was used
     * 
     * @param x X-Coordinate
     * @param y Y-Coordinate
     * @param z Z-Coordinate
     * @param tool Item object
     * @return True if success, False otherwise 
     */ 			
	public boolean breakNaturallyAt(int x, int y, int z, MC_ItemStack tool);
	
	// World Info
	 /** 
     * Get location of spawn for this world.
     * 
     * @return Spawn location 
     */ 			
	public MC_Location getSpawnLocation();
	 /** 
     * Get name for this world.
     * 
     * @return World Name 
     */ 			
	public String getName();
	 /** 
     * Get list of entities in this world.
     * 
     * @return List of entities 
     */ 			
	public List<MC_Entity> getEntities();
	
	 /** 
     * Get GameRule setting
     * 
     * @param ruleType Boolean Game Rule interested in
     * @return Game Rule setting 
     */ 			
	public boolean getGameRuleBool(MC_GameRuleType ruleType);
	 /** 
     * Set a Game Rule
     * 
     * @param ruleType GameRule type
     * @param newVal New Setting
     */ 			
	public void setGameRule(MC_GameRuleType ruleType, boolean newVal);
	
	 /** 
     * Get Sign object at location
     * 
     * @param loc Location
     * @return Sign object 
     */ 			
	public MC_Sign getSignAt(MC_Location loc);
	 /** 
     * Get Chest object at location
     * 
     * @param loc Location
     * @return Chest object 
     */ 			
	public MC_Chest getChestAt(MC_Location loc);

    
	 /** 
     * Spawn an entity at location
     * 
     * @param entType Entity Type
     * @param loc Location
     * @param optionalCustomName Custom Entity Name
     * @return Entity object 
     */ 			
	public MC_Entity spawnEntity(MC_EntityType entType, MC_Location loc, String optionalCustomName);

	 /** 
     * Spawn an Item entity at location
     * 
     * @param is ItemStack
     * @param loc Location
     * @param throwerName Name of thrower (null if not used)
     * @return Item Entity object 
     */ 			
	public MC_Entity dropItem(MC_ItemStack is, MC_Location loc, String throwerName);

	 /** 
     * Gets the dimension identifier for this world.
     * 
     * @return Integer uniquely identifying this world. 
     */ 			
	public int getDimension();
	
	 /** 
     * Gets the Minecraft day/night time value in ticks. Matches '/time query daytime'. Each Minecraft day is 24000 ticks.
     * 
     * @return Long type representing day time
     */ 			
	public int getDayTime();

	/** 
     * Gets the world time in ticks. Matches '/time query gametime'. Incremented by one every world tick. Each Minecraft day is 24000 ticks.
     *  
     * @return Long type representing game time
     */ 			
	public int getGameTime();

	 /** 
     * Get generic Container object at location
     * 
     * @param loc Location
     * @return MC_Container object 
     */ 			
	public MC_Container getContainerAt(MC_Location loc);

	 /** 
     * Get biome type at location
     * 
     * @param x X coordinate
     * @param z Z coordinate
     * @return MC_WorldBiomeType value 
     */ 			
	public MC_WorldBiomeType getBiomeTypeAt(int x, int z);

	 /** 
     * Get biome type at location
     * 
     * @param x X coordinate
     * @param z Z coordinate
     * @return MC_WorldBiomeType object 
     */ 			
	
	 /** 
     * Set biome type at location
     * 
     * @param x X coordinate
     * @param z Z coordinate
     * @param biomeType Biome 
     */ 			
	public void setBiomeTypeAt(int x, int z, MC_WorldBiomeType biomeType);
	
	 /** 
     * Loads chunk at specified coordinate
     * 
     * @param cx Chunk X coordinate (i.e. x >> 4)
     * @param cz Chunk Z coordinate (i.e. z >> 4)
     * @return True if chunk loaded. False otherwise 
     */ 			
	public boolean loadChunk(int cx, int cz);

	 /** 
     * Check if particular chunk is loaded
     * 
     * @param cx Chunk X coordinate (i.e. x >> 4)
     * @param cz Chunk Z coordinate (i.e. z >> 4)
     * @return True if chunk loaded. False otherwise 
     */ 			
	public boolean isChunkLoaded(int cx, int cz);

	 /** 
     * Get list of loaded chunks
     * 
     * @return List of MC_Chunk objects 
     */ 			
	public List<MC_Chunk> getLoadedChunks();
	
}


