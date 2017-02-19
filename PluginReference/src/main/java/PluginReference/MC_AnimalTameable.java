package PluginReference;

/** 
 * Interface representing a tameable animal
 */ 			
public interface MC_AnimalTameable extends MC_Animal
{
	 /** 
     * Check if an Animal is tamed
     * @return True if tamed, False otherwise
     */ 		
	public boolean isTamed();
	 /** 
     * Sets tamed setting
     * 
     * @param flag True to tame, false otherwise
     */ 		
	public void setTamed(boolean flag);

	 /** 
     * Check if an Animal is sitting
     * @return True if sitting, False otherwise
     */ 		
	public boolean getSitting();
	 /** 
     * Sets the sitting status
     * 
     * @param flag True to sit, False otherwise
     */ 		
	public void setSitting(boolean flag);

	 /** 
     * Get UUID of Owner
     * 
     * @return UUID of Owner    
     */ 			
	public String getUUIDOfOwner();
	 /** 
     * Sets UUID of owning player
     * 
     * @param uuid UUID of owning player
     */ 		
	public void setUUIDOfOwner(String uuid);
	 /** 
     * Sets owner to player
     * 
     * @param plr Player object
     */ 		
	public void setOwner(MC_Player plr);
	 /** 
     * Get Owning Player
     * 
     * @return Player Object    
     */ 			
	public MC_Player getOwner();
	 /** 
     * Check if owned by specified player
     * 
     * @param plr Player object
     * @return True if owned by player, False otherwise    
     */ 			
	public boolean isOwnedBy(MC_Player plr);


	 /** 
     * Initiates a particle effect, true for hearts, false for smoke.
     * 
     * @param flag True for Love, False for Hate
     */ 		
	public void showLoveHateEffect(boolean flag); // true = hearts, false = smoke particles
	
}
