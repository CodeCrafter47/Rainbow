package joebkt;

import java.io.Serializable;

public class _SerializableLocation implements Serializable
{
    public int dimension;
    public double x;
    public double y;
    public double z;
    public float yaw;
    public float pitch;
    
    public _SerializableLocation(final double x, final double y, final double z, final int dimension, final float yaw, final float pitch) {
        super();
        this.dimension = dimension;
        this.x = x;
        this.y = y;
        this.z = z;
        this.pitch = pitch;
        this.yaw = yaw;
    }
    
    public static String GetDimensionName(final int dimension) {
        if (dimension == 0) {
            return "world";
        }
        if (dimension == -1) {
            return "world_nether";
        }
        if (dimension == 1) {
            return "world_the_end";
        }
        return "Dimension " + dimension;
    }
    
    @Override
    public String toString() {
        return String.valueOf(GetDimensionName(this.dimension)) + "(" + (int)this.x + "," + (int)this.y + "," + (int)this.z + ")";
    }
}
