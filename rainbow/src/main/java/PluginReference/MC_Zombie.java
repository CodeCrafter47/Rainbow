package PluginReference;

/** 
 * Represents a Minecraft Zombie
 */ 			
public interface MC_Zombie extends MC_Entity
{
	 /** 
     * Check if a Villager zombie
     *
     * @return True if a villager, False otherwise
     */ 		
	public boolean isVillager();
}
