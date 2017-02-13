package PluginReference;

/** 
 * Potion Effect
 */             
public class MC_PotionEffect
{
    public MC_PotionEffectType type = MC_PotionEffectType.UNSPECIFIED;
    public int duration = 0;
    public int amplifier = 0;
    public boolean ambient = true;
    public boolean showsParticles = true;
    
    /**
     * Sets number of arrows sticking out. Up to 127.
     *
     * @param argType Potion Type
     * @param argDuration Duration in Ticks (1/20th of a second)
     * @param argAmplifier Amplifier (zero-based so Haste with 2 amplifier is 'Haste III') 
     */
    public MC_PotionEffect(MC_PotionEffectType argType, int argDuration, int argAmplifier)
    {
        type = argType;
        duration = argDuration;
        amplifier = argAmplifier;
    }
}
