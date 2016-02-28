package PluginReference;

/** 
 * Represents a Dropped Item
 */ 			
public interface MC_ItemEntity extends MC_Entity
{
	 /** 
     * Gets the ItemStack associated with this item
     *
     * @return MC_ItemStack object
     */ 		
	public MC_ItemStack getItemStack();
	
	 /** 
     * Sets the ItemStack associated with this item
     *
     * @param is MC_ItemStack object
     */ 		
	public void setItemStack(MC_ItemStack is);

	 /** 
     * Gets name of person who throw this item
     *
     * @return Name of thrower
     */ 		
	public String getThrowerName();

	 /** 
     * Sets name of person who throw this item
     *
     * @param name Name of thrower
     */ 		
	public void setThrowerName(String name);

	 /** 
     * Gets name of person who owns this item (target of /give)
     *
     * @return Name of owner
     */ 		
	public String getOwnerName();

	 /** 
     * Sets name of person who owns this item (target of /give)
     *
     * @param name Name of owner
     */ 		
	public void setOwnerName(String name);
	
}
