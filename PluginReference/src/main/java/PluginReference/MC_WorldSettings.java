package PluginReference;

import java.io.Serializable;

public class MC_WorldSettings implements Serializable
{
	public long seed = 0L;
	public MC_WorldBiomeType biomeType = MC_WorldBiomeType.FOREST;
	public MC_WorldLevelType levelType = MC_WorldLevelType.DEFAULT;
	public boolean generateStructures = false;
}
