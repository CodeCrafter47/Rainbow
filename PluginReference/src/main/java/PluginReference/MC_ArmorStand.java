package PluginReference;

import java.util.List;

/** 
 * Armor Stand
 */ 			
public interface MC_ArmorStand extends MC_Entity
{
	 /** 
     * Check if Armor Stand has arms
     * 
     * @return True if has arms, False otherwise 
     */ 		
	public boolean hasArms();
	 /** 
     * Check if Armor Stand has base plate
     * 
     * @return True if has base plate, False otherwise 
     */ 		
	public boolean hasBase();

	 /** 
     * Set whether Armor Stand has arms
     * 
     * @param flag True if has arms, False otherwise
     */ 		
	public void setHasArms(boolean flag);
	 /** 
     * Set whether Armor Stand has base plate
     * 
     * @param flag True if has base plate, False otherwise
     */ 		
	public void setHasBase(boolean flag);
	
	 /** 
     * Gets rotational pose values for head, body, left arm, right arm, left leg, and right leg.
     * 
     * @return Array of triplets representing 3d rotations for each component 
     */ 		
	public List<MC_FloatTriplet> getPose();
	 /** 
     * Set rotational pose values for head, body, left arm, right arm, left leg, and right leg.
     * 
     * @param pose Array of triplets representing 3d rotations for each component
     */ 		
	public void setPose(List<MC_FloatTriplet> pose);
	
	 /** 
     * Gets item in hand
     * 
     * @return Item in hand 
     */ 		
	public MC_ItemStack getItemInHand();

	/** 
     * Sets item in hand
     * 
     * @param item Item in hand
     */ 		
	public void setItemInHand(MC_ItemStack item);	
	
}
