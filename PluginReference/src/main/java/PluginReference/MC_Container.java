package PluginReference;

/** 
 * Interface representing a Container
 */             
public interface MC_Container
{
     /** 
     * Gets the size of the container (how many items it holds)
     * 
     * @return Size of container 
     */         
    public int getSize();
    
    /** 
     * Get item at specified position (index) within container
     *
     * @param idx Index of item
     * @return Item at specified index 
     */         
    public MC_ItemStack getItemAtIdx(int idx);

    
     /** 
     * Set item at specified position (index) within container
     *
     * @param idx Index of item
     * @param is Item to set
     */         
    public void setItemAtIdx(int idx, MC_ItemStack is);
    
    
     /** 
     * Clear contents of container
     */         
    public void clearContents();


    //public MC_ContainerType getContainerType();
}
