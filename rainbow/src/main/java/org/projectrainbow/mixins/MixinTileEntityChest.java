package org.projectrainbow.mixins;

import PluginReference.MC_Block;
import PluginReference.MC_Chest;
import PluginReference.MC_DirectionNESWUD;
import PluginReference.MC_ItemStack;
import net.minecraft.block.BlockChest;
import net.minecraft.item.ItemStack;
import net.minecraft.state.properties.ChestType;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import org.projectrainbow.BlockWrapper;
import org.projectrainbow.PluginHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Mixin(TileEntityChest.class)
public abstract class MixinTileEntityChest extends TileEntity implements MC_Chest {
    @Shadow
    private NonNullList<ItemStack> chestContents;

    public MixinTileEntityChest(TileEntityType<?> tileEntityType) {
        super(tileEntityType);
    }

    @Override
    public List<MC_ItemStack> getInventory() {
        return PluginHelper.copyInvList(chestContents);
    }

    @Override
    public void setInventory(List<MC_ItemStack> var1) {
        PluginHelper.updateInv(chestContents, var1);
    }

    @Override
    public MC_Chest GetLinkedChestAt(MC_DirectionNESWUD var1) {
        if (func_195044_w().getValue(BlockChest.field_196314_b) == ChestType.SINGLE) {
            return null;
        }
        BlockPos adjacent = pos.offset(BlockChest.func_196311_i(func_195044_w()));
        if (world.getBlockState(adjacent).getBlock() != func_195044_w().getBlock()) {
            return null;
        }
        return (MC_Chest)world.getTileEntity(adjacent);
    }

    @Override
    @Deprecated
    public int getBlockId() {
        return 54;
    }

    @Override
    public MC_Block getBlock() {
        return new BlockWrapper(func_195044_w());
    }
}
