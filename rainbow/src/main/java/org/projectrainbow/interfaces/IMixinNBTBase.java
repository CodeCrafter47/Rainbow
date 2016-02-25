package org.projectrainbow.interfaces;

import java.io.DataInput;
import java.io.DataOutput;

public interface IMixinNBTBase {
	void read1(DataInput dataInput);
	
	void write1(DataOutput dataOutput);

}
