package org.projectrainbow.mixins;

import PluginReference.MC_Chest;
import PluginReference.MC_DirectionNESWUD;
import PluginReference.MC_ItemStack;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import org.projectrainbow.PluginHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Mixin(TileEntityChest.class)
public abstract class MixinTileEntityChest extends TileEntity implements MC_Chest {
    @Shadow
    private ItemStack[] chestContents = new ItemStack[27];
    @Shadow
    public TileEntityChest adjacentChestZNeg;
    @Shadow
    public TileEntityChest adjacentChestXPos;
    @Shadow
    public TileEntityChest adjacentChestXNeg;
    @Shadow
    public TileEntityChest adjacentChestZPos;

    @Override
    public List<MC_ItemStack> getInventory() {
        return PluginHelper.invArrayToList(chestContents);
    }

    @Override
    public void setInventory(List<MC_ItemStack> var1) {
        PluginHelper.updateInv(chestContents, var1);
    }

    @Override
    public MC_Chest GetLinkedChestAt(MC_DirectionNESWUD var1) {
        if (adjacentChestXNeg != null)
            return (MC_Chest) adjacentChestXNeg;
        else if (adjacentChestXPos != null)
            return (MC_Chest) adjacentChestXPos;
        else if (adjacentChestZNeg != null)
            return (MC_Chest) adjacentChestZNeg;
        else
            return (MC_Chest) adjacentChestZPos;
    }

    @Override
    public int getBlockId() {
        return Block.getIdFromBlock(getBlockType());
    }
}
