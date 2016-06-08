package org.projectrainbow.mixins;

import PluginReference.MC_Sign;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.ArrayList;
import java.util.List;

@Mixin(TileEntitySign.class)
public class MixinTileEntitySign extends TileEntity implements MC_Sign {
    @Shadow
    @Final
    public ITextComponent[] signText;

    @Override
    public List<String> getLines() {
        ArrayList<String> list = new ArrayList<String>(4);
        for (ITextComponent chatComponent : signText) {
            list.add(chatComponent.getUnformattedText());
        }
        return list;
    }

    @Override
    public void setLines(List<String> var1) {
        for(int i = 0; i < 4; i++) {
            signText[i] = new TextComponentString(var1.get(i));
        }
        markDirty();
    }
}
