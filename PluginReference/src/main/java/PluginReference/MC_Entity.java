package PluginReference;

import java.util.List;
import java.util.UUID;

/** 
 * Interface for an Entity
 */ 			
public interface MC_Entity 
{
	 /**
     * Gets name of this entity
     * 
     * @return Entity Name 
     */ 			
	public String getName();
	
	 /** 
     * Gets location of entity
     * 
     * @return Location of entity 
     */ 				
	public MC_Location getLocation();
	
	 /** 
     * Gets World object this entity is in.
     * 
     * @return World object 
     */ 				
	public MC_World getWorld();
	
	 /** 
     * Gets Entity Type
     * @return Entity Type 
     */ 			
	public MC_EntityType getType();
	 /** 
     * Check if entity is sneaking
     * @return True if sneaking, False otherwise
     */ 		
	public boolean isSneaking();
	 /** 
     * Check if entity is sprinting
     * @return True if sprinting, False otherwise
     */ 		
	public boolean isSprinting();
	 /** 
     * Check if entity is invisible
     * @return True if invisible, False otherwise
     */ 		
	public boolean isInvisible();
	 /** 
     * Sets entity as invisible
     * 
     * @param flag True for Invisible, False for Visible
     */ 		
	public void setInvisible(boolean flag);

	// Life/Death
	 /** 
     * Check if entity is dead
     * @return True if dead, False otherwise
     */ 		
	public boolean isDead();	
	 /** 
     * Kills entity with max damage (drops items)
     */ 			
	public void kill();

	 /** 
     * Get Vehicle entity
     * 
     * @return Vehicle entity or NULL if none. 
     */ 			
	public MC_Entity getVehicle();
	 /** 
     * Get Rider entity
     * 
     * @return Rider entity or NULL if none. 
     */ 			
	public MC_Entity getRider();
	 /** 
     * Sets the entity as a rider for this entity
     * 
     * @param ent Entity to set as rider
     */ 		
	public void setRider(MC_Entity ent);
	 /** 
     * Sets the entity as a vehicle for this entity
     * 
     * @param ent Entity to set as vehicle
     */ 		
	public void setVehicle(MC_Entity ent);
	
	 /** 
     * Get Armor as a list of Items
     * 
     * @return List of armor (3=hat, 2=chest, 1=legs, 0=boots).    
     */ 			
	public List<MC_ItemStack> getArmor();
	 /** 
     * Sets the armor for this entity
     * 
     * @param items List of armor (3=hat, 2=chest, 1=legs, 0=boots)
     */ 		
	public void setArmor(List<MC_ItemStack> items);
	
	 /** 
     * Internal Use Only, Not Documented
     * 
     * @return Undocumented    
     */ 			
	public String internalInfo();
	
	 /** 
     * Get Number of ticks (1/20th second) entity is on fire
     * 
     * @return Number of ticks    
     */ 			
	public int getFireTicks();
	 /** 
     * Sets number of ticks to stay on fire
     * 
     * @param val Number of Ticks
     */ 		
	public void setFireTicks(int val);
	
	 /** 
     * Gets motion-related data for this entity
     * 
     * @return Motion Related data structure    
     */ 			
	public MC_MotionData getMotionData();
	 /** 
     * Sets various motion fields for a player
     * 
     * @param data Motion data
     */ 		
	public void setMotionData(MC_MotionData data);
	
	 /** 
     * Check if entity has a custom name
     * @return True if has a custom name, False otherwise
     */ 		
	public boolean hasCustomName();
	 /** 
     * Sets custom name for this entity
     * 
     * @param str Custom Name
     */ 		
	public void setCustomName(String str);
	 /** 
     * Gets custom name for this entity
     * 
     * @return Custom name    
     */ 			
	public String getCustomName();

	// Basic Info
	UUID getUUID();

	/**
    * Teleports a player somewhere
    *
    * @param loc Location to teleport
    */
	void teleport(MC_Location loc);

	/**
	 * Teleports a player somewhere
	 *
	 * @param loc Location to teleport
	 * @param safe if true the player will be teleported to a nearby safe
	 *             location if you try to teleport the player into a wall
	 */
	void teleport(MC_Location loc, boolean safe);

	/**
     * Get Current health of this entity
     * 
     * @return Health Value    
     */ 			
	public float getHealth();
	 /** 
     * Sets current health (won't exceed maximum).
     * 
     * @param argHealth Health value
     */ 		
	public void setHealth(float argHealth);
	 /** 
     * Get Maximum Health of this entity
     * 
     * @return Maximum Health    
     */ 			
	public float getMaxHealth();
	 /** 
     * Sets maximum health
     * 
     * @param argHealth Health value
     */ 		
	public void setMaxHealth(float argHealth);

	/**
	 * Get base armor score (doesn't include enchantments, potion effects, etc).
	 * Full set of diamond armor is 20. 
	 * Full set of iron armor is 15. 
	 * Full set of chain armor is 12. 
	 * Full set of gold armor is 11. 
	 * Full set of leather armor is 7. 
     * @return Base armor score (as of 1.8, a number between 0 and 20)    
	 */
	public int getBaseArmorScore();

	/**
	 * Get damage amount after armor applied
	 *
     * @param dmgType Type of damage (some types go through armor)
     * @param dmg Amount of damage before armor considered
     * @return Armor adjusted damage value    
	 */
	public float getArmorAdjustedDamage(MC_DamageType dmgType, float dmg);

	/**
	 * Get total damage amount after armor, absorption, effects, and enchantments applied
	 *
     * @param dmgType Type of damage (some types go through armor)
     * @param dmg Amount of damage before armor considered
     * @return Total adjusted damage value
	 */
	public float getTotalAdjustedDamage(MC_DamageType dmgType, float dmg);
	
	/**
	 * Gets absorption amount (used up before damage applies, like a shield value)
	 *
     * @return Absorption amount    
	 */
	public float getAbsorptionAmount();
	/**
	 * Sets absorption amount
	 *
     * @param val New absorption value
	 */
	public void setAbsorptionAmount(float val);
	
	/**
	 * Sets number of arrows sticking out. Up to 127.
	 *
     * @param numArrows Number of arrows
	 */
	public void setNumberOfArrowsHitWith(int numArrows);

	 /** 
     * Gets attacker (valid during certain events like onAttemptEntityDamage)
     * 
     * @return Attacking entity 
     */ 			
	public MC_Entity getAttacker();

	 /** 
     * Gets the active potion effects on this entity
     * 
     * @return List of potion effects 
     */ 			
	public List<MC_PotionEffect> getPotionEffects();

	 /** 
     * Sets the active potion effects on this entity. Use null or empty list to cancel all potion effects
     * 
     * @param potionEffects List of potion effects
     */ 			
	public void  setPotionEffects(List<MC_PotionEffect> potionEffects);

	/** 
     * Sets adjusted damage if called from onAttemptEntityDamage
     * 
     * @param dmg Damage value
     */ 			
	public void setAdjustedIncomingDamage(float dmg);

	/** 
     * Returns an integer ID representing this entity.
     * 
     * @return Entity ID
     */ 			
	public int getEntityId();
	

	 /** 
     * Gets a list of nearby entities (up to a specified radius)
     * 
     * @param radius Euclidean Radius in Blocks
     * @return List of entities 
     */ 			
	public List<MC_Entity> getNearbyEntities(float radius);

	/** 
     * Removes entity (no item drops).  Ignores player entities.
     */ 			
	public void removeEntity();

	 /** 
     * Get Riders
     * 
     * @return List of Rider entity or NULL if none. 
     */ 			
	public List<MC_Entity> getRiders();

	 /** 
     * Add a rider
     * 
     * @param ent Entity to set as rider
     */ 		
	public void addRider(MC_Entity ent);

	 /** 
     * Remove a rider
     * 
     * @param ent Entity to set as rider
     */ 		
	public void removeRider(MC_Entity ent);
	
}

