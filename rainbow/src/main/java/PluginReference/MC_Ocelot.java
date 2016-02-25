package PluginReference;

/** 
 * Ocelot Entity interface
 */ 			
public interface MC_Ocelot extends MC_AnimalTameable
{
	 /** 
     * Get Cat Type
     * 
     * @return Cat Type 
     */ 			
	   public MC_OcelotType getCatType();
		 /** 
	     * Sets Cat Type (Wild, Black, Siamese, etc)
	     * 
	     * @param catType Cat Type
	     */ 		
	   public void setCatType(MC_OcelotType catType);
}

