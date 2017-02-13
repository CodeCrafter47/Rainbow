package PluginReference;

/** 
 * Represents a Minecraft Enderman
 */             
public interface MC_Enderman extends MC_LivingEntity
{
     /** 
     * Get carried block
     *
     * @return Carried block
     */         
    public MC_Block getCarriedBlock();

    /** 
     * Set carried block
     *
     * @param blk Carried block
     */         
    public void setCarriedBlock(MC_Block blk);
}
