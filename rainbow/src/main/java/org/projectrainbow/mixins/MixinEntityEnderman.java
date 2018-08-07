package org.projectrainbow.mixins;

import PluginReference.MC_Block;
import PluginReference.MC_Enderman;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.monster.EntityEnderman;
import org.projectrainbow.BlockWrapper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(EntityEnderman.class)
public abstract class MixinEntityEnderman extends MixinEntityLivingBase implements MC_Enderman {

    @Shadow(prefix = "setHeldBlockState$")
    public abstract void setHeldBlockState$func_195406_b(IBlockState var1);

    @Shadow(prefix = "getHeldBlockState$")
    public abstract IBlockState getHeldBlockState$func_195405_dq();

    @Override
    public MC_Block getCarriedBlock() {
        IBlockState heldBlockState = getHeldBlockState$func_195405_dq();
        return heldBlockState == null ? null : new BlockWrapper(heldBlockState);
    }

    @Override
    public void setCarriedBlock(MC_Block blk) {
        if (blk == null) {
            setHeldBlockState$func_195406_b(null);
        } else {
            setHeldBlockState$func_195406_b(((BlockWrapper)blk).m_blockState);
        }
    }
}
