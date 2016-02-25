package org.projectrainbow;


import PluginReference.MC_GeneratedColumn;


public class GeneratedColumnWrapper implements MC_GeneratedColumn {

    public boolean isChanged = false;
    public short[] data = null;

    public GeneratedColumnWrapper() {}

    public void setData(short[] arrParm) {
        this.data = arrParm;
        this.isChanged = true;
    }
}
