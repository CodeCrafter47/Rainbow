package PluginReference;

/** 
 * Horse Entity
 */ 			
public interface MC_Horse extends MC_Animal
{
	   public MC_HorseType getHorseType();
		 /** 
	     * Sets the horse type (Horse, Donkey, etc)
	     * 
	     * @param arg Horse Type
	     */ 		
	   public void setHorseType(MC_HorseType arg);

		 /** 
	     * Get Horse Variant
	     * 
	     * @return Horse Variant 
	     */ 			
	   public MC_HorseVariant getHorseVariant();
		 /** 
	     * Sets the horse variant (Chestnut, Creamy, etc)
	     * 
	     * @param arg Horse Variant
	     */ 		
	   public void setHorseVariant(MC_HorseVariant arg);

		 /** 
	     * Check if horse has chest
	     * @return True if has chest, False otherwise
	     */ 		
	   public boolean hasChest();
		 /** 
	     * Sets whether horse has chest
	     * 
	     * @param flag True for chest, False otherwise
	     */ 		
	   public void setHasChest(boolean flag);

		 /** 
	     * Sets whether horse is tamed.
	     * 
	     * @param flag True for tamed, False otherwise
	     */ 		
	   public void setTamed(boolean flag);
		 /** 
	     * Check if horse is tamed
	     * @return True if horse is tamed, False otherwise
	     */ 		
	   public boolean isTame();
	   
		 /** 
	     * Gets temper of Horse
	     * 
	     * @return Temper value 
	     */ 			
	   public int getTemper();
		 /** 
	     * Sets temper of horse
	     * 
	     * @param val New Temper
	     */ 		
	   public void setTemper(int val);

		 /** 
	     * Gets Owner UUID
	     * 
	     * @return UUID of owner 
	     */ 			
	   public String getOwnerUUID();
		 /** 
	     * Sets Owning UUID
	     * 
	     * @param strUUID UUID of owner
	     */ 		
	   public void setOwnerUUID(String strUUID);
		 /** 
	     * Sets Owning Player
	     * 
	     * @param plr Player Object
	     */ 		
	   public void setOwner(MC_Player plr);

	   
}
