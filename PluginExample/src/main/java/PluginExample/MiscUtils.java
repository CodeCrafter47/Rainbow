package PluginExample;

import PluginReference.BlockHelper;
import PluginReference.MC_ItemStack;

import java.util.ArrayList;
import java.util.List;


public class MiscUtils {

    public static MC_ItemStack getRandomItem() {
        List<Integer> bad = new ArrayList<Integer>();
        bad.add(43);
        bad.add(62); // works but no texture
        bad.add(93);
        bad.add(125);
        bad.add(178);
        bad.add(132);
        bad.add(181);
        bad.add(144);
        bad.add(119);
        bad.add(94);
        bad.add(17); // acacia wood, no texture
        bad.add(176);
        bad.add(117);
        bad.add(150);
        bad.add(60);
        bad.add(63);
        bad.add(149);
        bad.add(44); // no texture : wooden slab
        bad.add(373); // potion, could work but might be garbage
        bad.add(383); // empty spawn egg
        bad.add(140);
        bad.add(74);
        bad.add(118); // cauldron
        bad.add(387); // written book, as-is would be invalid
        bad.add(403); // enchanted book, as-is would be invalid
        bad.add(358); // map #0

        // These work but probably not good idea to hand out
        bad.add(166); // barrier:
        bad.add(137); // command block
        bad.add(52); // mob spawner

        List<Integer> keys = new ArrayList(BlockHelper.mapNumSubtypes.keySet());
        int len = keys.size();
        int idxKey = (int) (Math.random() * len);
        int id = keys.get(idxKey);
        while (bad.contains(id)) {
            idxKey = (int) (Math.random() * len);
            id = keys.get(idxKey);
        }

        int numSubTypes = BlockHelper.mapNumSubtypes.get(id);
        int dmg = 0;
        if (numSubTypes > 1) {
            dmg = (int) (Math.random() * numSubTypes);
        }
        System.out.println(" --- Creating ID=" + id + ", DMG=" + dmg + " ---");
        return MyPlugin.server.createItemStack(id, 1, dmg);
    }

}
