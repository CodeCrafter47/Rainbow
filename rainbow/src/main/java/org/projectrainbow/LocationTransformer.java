package org.projectrainbow;

import PluginReference.MC_Location;
import PluginReference.MC_World;
import com.google.common.base.Function;
import net.minecraft.src.BlockPos;
import net.minecraft.src.World;

public class LocationTransformer implements Function<BlockPos, MC_Location> {

    private final World worldObj;

    public LocationTransformer(World worldObj) {
        this.worldObj = worldObj;
    }

    @Override
    public MC_Location apply(BlockPos o) {
        return new MC_Location(o.getX(), o.getY(), o.getZ(), ((MC_World) worldObj).getDimension());
    }
}
