package PluginReference;

import java.util.List;

/** 
 * A sign
 */ 			
public interface MC_Sign
{
	 /** 
     * Get sign text
     * 
     * @return List of strings 
     */ 			
	public List<String> getLines();

	 /** 
     * Set sign text
     * 
     * @param lines List of strings
     */ 			
	public void setLines(List<String> lines);
}
