package PluginReference;

/** 
 * Represents an Item Enchantment
 */ 			
public class MC_Enchantment
{
	public MC_EnchantmentType type = MC_EnchantmentType.UNKNOWN;
	public int level = 0;
	
	public MC_Enchantment(MC_EnchantmentType argType, int argLevel)
	{
		type = argType;
		level = argLevel;
	}
}
