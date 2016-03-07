package org.projectrainbow.mixins;

import PluginReference.MC_Block;
import PluginReference.MC_Enderman;
import net.minecraft.src.EntityEnderman;
import net.minecraft.src.IBlockState;
import org.projectrainbow.BlockWrapper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(EntityEnderman.class)
public abstract class MixinEntityEnderman extends MixinEntityLivingBase implements MC_Enderman {

    @Shadow
    public abstract void setHeldBlockState(IBlockState var1);

    @Shadow
    public abstract IBlockState getHeldBlockState();

    @Override
    public MC_Block getCarriedBlock() {
        IBlockState heldBlockState = getHeldBlockState();
        return heldBlockState == null ? null : new BlockWrapper(heldBlockState);
    }

    @Override
    public void setCarriedBlock(MC_Block blk) {
        if (blk == null) {
            setHeldBlockState(null);
        } else {
            setHeldBlockState(((BlockWrapper)blk).m_blockState);
        }
    }
}
