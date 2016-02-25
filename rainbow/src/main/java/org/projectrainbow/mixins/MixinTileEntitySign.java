package org.projectrainbow.mixins;

import PluginReference.MC_Sign;
import net.minecraft.src.ChatComponentText;
import net.minecraft.src.IChatComponent;
import net.minecraft.src.TileEntity;
import net.minecraft.src.TileEntitySign;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.ArrayList;
import java.util.List;

@Mixin(TileEntitySign.class)
public class MixinTileEntitySign extends TileEntity implements MC_Sign {
    @Shadow
    @Final
    public IChatComponent[] signText;

    @Override
    public List<String> getLines() {
        ArrayList<String> list = new ArrayList<String>(4);
        for (IChatComponent chatComponent : signText) {
            list.add(chatComponent.getUnformattedTextForChat());
        }
        return list;
    }

    @Override
    public void setLines(List<String> var1) {
        for(int i = 0; i < 4; i++) {
            signText[i] = new ChatComponentText(var1.get(i));
        }
        markDirty();
    }
}
