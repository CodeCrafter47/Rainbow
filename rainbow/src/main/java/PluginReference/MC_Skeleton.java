package PluginReference;

/** 
 * A Skeleton
 */ 			
public interface MC_Skeleton extends MC_Entity
{
	 /** 
     * Get type of skeleton (regular / wither)
     * 
     * @return Skeleton type enum 
     */ 			
	public MC_SkeletonType getSkeletonType();
	 /** 
     * Set type of skeleton (regular, wither)
     * 
     * @param skellyType Skeleton type
     */ 			
	public void setSkeletonType(MC_SkeletonType skellyType);
}

