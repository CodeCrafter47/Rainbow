package PluginReference;

/** 
 * Interface representing a Minecraft block.
 */ 			
public interface MC_Block
{
	 /** 
     * Get the Minecraft ID for this block.
     * @return Minecraft ID
     */
	 @Deprecated
	public int getId();
	
	 /** 
     * Get the subtype for this block type
     * @return Subtype value
     */
	 @Deprecated
	public int getSubtype();
	
	 /** 
     * Check if block is solid
     * @return True if solid, False otherwise
     */ 		
	public boolean isSolid();
	 /** 
     * Check if block is liquid
     * @return True if liquid, False otherwise
     */ 		
	public boolean isLiquid();

	 /** 
     * Set Subtype value
     * @param idx Subtype value
     */
	 @Deprecated
	public void setSubtype(int idx);

	 public String getOfficialName();

	 public String getFriendlyName();
	
}
