package org.projectrainbow.mixins;

import java.io.DataInput;
import java.io.DataOutput;

import net.minecraft.src.NBTBase;
import org.projectrainbow.interfaces.IMixinNBTBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.src.NBTSizeTracker;

@Mixin(NBTBase.class)
public abstract class MixinNBTBase implements IMixinNBTBase {
	
	@Shadow
	abstract void read(DataInput dataInput, int i, NBTSizeTracker sizeTracker);
	
	@Shadow
	abstract void write(DataOutput dataOutput);

	@Override
	public void read1(DataInput dataInput) {
		read(dataInput, 500, NBTSizeTracker.INFINITE);
	}

	@Override
	public void write1(DataOutput dataOutput) {
		write(dataOutput);
	}
}
