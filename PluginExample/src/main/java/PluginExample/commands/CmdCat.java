package PluginExample.commands;

import PluginExample.CmdBase;
import PluginReference.MC_EntityType;
import PluginReference.MC_Ocelot;
import PluginReference.MC_OcelotType;
import PluginReference.MC_Player;
import PluginReference.RainbowUtils;

public class CmdCat extends CmdBase {
    public CmdCat() {
        super("cat", "Get a nice kitty.");
    }

    @Override
    protected void execute(MC_Player plr, String[] args) {
        int num = (int) (Math.random() * 4);
        MC_Ocelot cat = (MC_Ocelot) plr.getWorld().spawnEntity(MC_EntityType.OCELOT, plr.getLocation(), RainbowUtils.RainbowString("Spawn Kitty"));
        cat.setTamed(true);
        if (num == 1) cat.setCatType(MC_OcelotType.BLACK);
        if (num == 2) cat.setCatType(MC_OcelotType.RED);
        if (num == 3) cat.setCatType(MC_OcelotType.SIAMESE);

        cat.setOwner(plr);
        cat.showLoveHateEffect(true);
    }
}
