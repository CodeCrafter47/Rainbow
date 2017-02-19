package PluginReference;

/** 
 * Interface for an Entity supporting aging.
 */ 			
public interface MC_EntityAgeable extends MC_LivingEntity
{
	 /** 
     * Get the age value
     * 
     * @return Age (-1=baby, 1=adult)  
     */ 		
	public int getAge();
	
	 /** 
     * Sets age of entity
     * 
     * @param idx Age (-1=baby, 1=adult)
     */ 		
	public void setAge(int idx); 
}
