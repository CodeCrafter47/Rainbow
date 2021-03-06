package PluginReference;

/**
 * A Skeleton
 */
public interface MC_Skeleton extends MC_LivingEntity {
    /**
     * Get type of skeleton (regular / wither)
     *
     * @return Skeleton type enum
     */
    @Deprecated
    MC_SkeletonType getSkeletonType();

    /**
     * Set type of skeleton (regular, wither)
     *
     * @param skellyType Skeleton type
     */
    @Deprecated
    void setSkeletonType(MC_SkeletonType skellyType);
}

