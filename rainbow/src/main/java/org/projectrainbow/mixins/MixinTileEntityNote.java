package org.projectrainbow.mixins;

import PluginReference.MC_Instrument;
import PluginReference.MC_NoteBlock;
import net.minecraft.src.BlockPos;
import net.minecraft.src.Blocks;
import net.minecraft.src.TileEntity;
import net.minecraft.src.TileEntityNote;
import net.minecraft.src.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(TileEntityNote.class)
public abstract class MixinTileEntityNote extends TileEntity implements MC_NoteBlock{

    @Shadow
    public byte note;

    @Shadow
    public abstract void triggerNote(World var1, BlockPos var2);

    @Override
    public int getNote() {
        return note;
    }

    @Override
    public void setNote(int note) {
        if (note < 0 || note > 24) {
            throw new IllegalArgumentException("Note must be between 0 and 24");
        }
        this.note = (byte) note;
        markDirty();
    }

    @Override
    public void play() {
        triggerNote(getWorld(), getPos());
    }

    @Override
    public void play(MC_Instrument instrument, int note) {
        if (note < 0 || note > 24) {
            throw new IllegalArgumentException("Note must be between 0 and 24");
        }
        getWorld().addBlockEvent(getPos(), Blocks.noteblock, instrument.ordinal(), note);
    }
}
