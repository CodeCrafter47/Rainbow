package PluginReference;

/** 
 * Interface for controlling world column generation
 */             
public interface MC_GeneratedColumn
{
     /** 
     * Called when a new world column is generated.
     * 
     * @param arr Column block data
     */         
    public void setData(short[] arr);
}
