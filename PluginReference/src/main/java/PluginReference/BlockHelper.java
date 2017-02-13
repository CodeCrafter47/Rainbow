package PluginReference;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/** 
 * Block-related utility functions and maps available to Rainbow plugins.
 */             
public class BlockHelper {
     /** 
     * Map of Block ID to internal Block Name
     * For example, 8 maps to 'flowing_water'
     */             
    public static Map<Integer, String> mapBlockNames = new ConcurrentHashMap<Integer, String>();
     /** 
     * Map of ID:Subtype string to friendly Block Name
     * For example, "126:5" maps to "Dark Oak Wood Slab"
     */             
    public static Map<String, String> mapItemNames = new ConcurrentHashMap<String, String>();
     /**
     * Map of ID to number of subtypes
     * For example, ID 1 maps to 7 because there are 7 stone subtypes in MC 1.8
     */             
    public static Map<Integer, Integer> mapNumSubtypes = new ConcurrentHashMap<Integer, Integer>();

     /** 
     * Get internal block name from ID.
     * For example, returns "flowing_water" for ID 8.
     * @return Block Name
     */             
    public static String getBlockName(int blockID) {
        String res = mapBlockNames.get(blockID);
        if (res != null) return res;
        res = ("BlockID_" + blockID);
        //System.out.println("----- DBG: " + res + " -----");
        return res;
    }

     /** 
     * Translates (ID=139,Subtype=1) into "Mossy Cobblestone Wall" and so forth.

     * @return Friendly Name
     */             
    public static String getBlockFriendlyName(int blockID, int subType){
        Integer numSubTypes = mapNumSubtypes.get(blockID);
        if(numSubTypes == null) numSubTypes = 1;
        if(numSubTypes <= 1){
            String key = blockID + ":0";
            String res = mapItemNames.get(key);
            return (res != null) ? res : ("BlockID_" + blockID + ":" + subType);
        }

        // Reduce by next highest power of 2...
        // This is because the dmg value may include other bitwise markers like directional attribute
        int powTwo = 2;
        for(int i=0; i<5; i++){
            if(numSubTypes <= powTwo){
                subType %= powTwo;
                break;
            }
            powTwo *= 2;
        }
        // Lookup based on refined dmg value...

        String key = blockID + ":" + subType;
        String res = mapItemNames.get(key);
        if (res != null) return res;
        
        res = ("BlockID_" + blockID + ":" + subType);
        //System.out.println("----- DBG: " + res + " -----");
        return res;
    }

     /** 
     * Translates Minecraft integer representation of a block into just the Block ID.
     * See PluginBase.onBlockBroke() for an example of where this is used. 
     * @return Block ID
     */             
    public static int getBlockID_FromKey(int blockKey){
        int blockID = blockKey & ((1 << 12) - 1);
        return blockID;
    }

     /** 
     * Translates Minecraft integer representation of a block into just the Block Subtype
     * See PluginBase.onBlockBroke() for an example of where this is used. 
     * @return Block Subtype
     */             
    public static int getBlockSubtype_FromKey(int blockKey){
        return (blockKey >> 12);
    }
}
