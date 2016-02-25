package org.projectrainbow.mixins;

import PluginReference.MC_Chest;
import PluginReference.MC_DirectionNESWUD;
import com.google.common.base.Objects;
import net.minecraft.src.Block;
import net.minecraft.src.ItemStack;
import net.minecraft.src.TileEntity;
import net.minecraft.src.TileEntityChest;
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
    public List getInventory() {
        return PluginHelper.invArrayToList(chestContents);
    }

    @Override
    public void setInventory(List var1) {
        PluginHelper.updateInv(chestContents, var1);
    }

    @Override
    public MC_Chest GetLinkedChestAt(MC_DirectionNESWUD var1) {
        return (MC_Chest) Objects.firstNonNull(Objects.firstNonNull(adjacentChestXNeg, adjacentChestXPos), Objects.firstNonNull(adjacentChestZNeg, adjacentChestZPos));
    }

    @Override
    public int getBlockId() {
        return Block.getIdFromBlock(blockType);
    }
}
