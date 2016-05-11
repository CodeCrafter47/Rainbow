package PluginExample.commands;

import PluginExample.CmdBase;
import PluginReference.ChatColor;
import PluginReference.MC_Player;
import PluginReference.MC_PotionEffect;
import PluginReference.MC_PotionEffectType;

import java.util.List;

public class CmdTestEffects extends CmdBase {
    public CmdTestEffects() {
        super("testeffects", "Test potion effects!");
    }

    @Override
    protected void execute(MC_Player plr, String[] args) {
        List<MC_PotionEffect> effects = plr.getPotionEffects();
        effects.add(new MC_PotionEffect(MC_PotionEffectType.NIGHT_VISION, 41 * 20, 2));
        effects.add(new MC_PotionEffect(MC_PotionEffectType.HASTE, 51 * 20, 2));
        plr.setPotionEffects(effects);

        plr.sendMessage(ChatColor.AQUA + "Gave you night vision!");
    }
}
