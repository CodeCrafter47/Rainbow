package org.projectrainbow;


import PluginReference.MC_Block;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.registry.IRegistry;


public class BlockWrapper implements MC_Block {

    public Block m_blockObject = null;
    public IBlockState m_blockState = null;

    public BlockWrapper(IBlockState parm) {
        this.m_blockState = parm;
        this.m_blockObject = parm.getBlock();
    }

    @Override
    @Deprecated
    public int getId() {
        return PluginHelper.getLegacyBlockId(m_blockObject);
    }

    @Override
    @Deprecated
    public int getSubtype() {
        return 0;
    }

    @Override
    public boolean isLiquid() {
        return this.m_blockObject.getMaterial(m_blockState).isLiquid();
    }

    @Override
    public boolean isSolid() {
        return this.m_blockObject.getMaterial(m_blockState).isSolid();
    }

    @Override
    @Deprecated
    public void setSubtype(int idx) {
    }

    @Override
    public String getOfficialName() {
        return IRegistry.BLOCK.getKey(m_blockObject).getPath();
    }

    @Override
    public String getFriendlyName() {
        return Item.BLOCK_TO_ITEM.get(m_blockObject).getDisplayName(new ItemStack(m_blockObject)).getFormattedText();
    }
}
