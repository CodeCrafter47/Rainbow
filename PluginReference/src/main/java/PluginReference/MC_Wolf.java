package PluginReference;

/** 
 * Wolf Entity interface
 */             
public interface MC_Wolf extends MC_AnimalTameable
{
     /** 
     * Check if angry
     * @return True if angry, False otherwise
     */         
    public boolean getAngry();
     /** 
     * Set angry flag
     * 
     * @param flag True if angry, false otherwise
     */             
    public void setAngry(boolean flag);
}
