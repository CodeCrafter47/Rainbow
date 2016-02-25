package PluginReference;

import java.util.List;

/** 
 * Interface representing a Chest
 */ 			
public interface MC_Chest
{
	 /** 
     * Get the contents of a chest
     * 
     * @return List of items 
     */ 		
	public List<MC_ItemStack> getInventory();
	
	 /** 
     * Set contents of this chest
     * 
     * @param items List of items 
     */ 		
	public void setInventory(List<MC_ItemStack> items);

	 /** 
     * Get connected chest at a direction
     * 
     * @param dir Direction to inspect
     * @return Connected chest object at that direction 
     */ 		
	public MC_Chest GetLinkedChestAt(MC_DirectionNESWUD dir);

	 /** 
     * Get the Block ID for this chest (for determining chest type)
     * 
     * @return Block ID 
     */ 			
	public int getBlockId(); 
}
