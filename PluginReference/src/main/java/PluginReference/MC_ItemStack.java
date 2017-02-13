package PluginReference;

import java.util.List;

/** 
 * Interface representing an Item Stack.
 */             
public interface MC_ItemStack
{
     /** 
     * Get the name player's see for this item.
     * E.g. "Acacia Wood Planks"
     * 
     * @return Item Name
     */         
    public String getFriendlyName();

     /** 
     * Get the customized name for this item (i.e. anvil renamed)
     * 
     * @return Item Name
     */         
    public String getCustomizedName();
    
     /** 
     * Get the Minecraft internal name.
     * E.g. "minecraft:air"
     * 
     * @return Item Name
     */         
    public String getOfficialName();
    
     /** 
     * Get the Minecraft ID for this item. E.g. 1=Stone, 2=Grass
     * 
     * @return Minecraft ID
     */         
    public int getId();
     /** 
     * Gets the damage/subtype value for this item. Note: This value is
     * also associated with the 'subtype' for certain items such as Wool.
     * For example, with Wool this value represents the color.
     * 
     * @return Damage/Subtype Value
     */         
    public int getDamage();
     /** 
     * Get the stack size of this item stack.
     * 
     * @return Stack size
     */         
    public int getCount();
    
     /** 
     * Sets the damage/subtype value for this item
     * 
     * @param dmg Damage/Subtype value
     */         
    public void setDamage(int dmg);
     /** 
     * Sets the stack count for this item stack.
     * 
     * @param cnt Count of items
     */         
    public void setCount(int cnt);

     /** 
     * Check if an item has a custom name.
     * @return True if has custom name, False otherwise.
     */         
    public boolean hasCustomName();
     /** 
     * Sets a custom name for the item.
     * 
     * @param str Custom name
     */         
    public void setCustomName(String str);
     /** 
     * Remove custom item name (if one exists) 
     */         
    public void removeCustomName();
    
     /** 
     * Determine if an item has custom details associated with it.
     * Such items can not normally be stacked together safely. 
     * 
     * @return True if has custom details, False otherwise.
     */         
    public boolean getHasCustomDetails(); // One use is to know whether can safely stack items together
     /** 
     * Get the maximum stacking size for this item.  I.e. 64 for most items. 
     * 
     * @return Maximum stack size for this item. 
     */         
    public int getMaxStackSize();
    
     /** 
     * Get the enchantment level of a given enchantment for this item.
     * 
     * @param encType Enchantment Type to inspect
     * @return Enchantment level. 
     */         
    public int getEnchantmentLevel(MC_EnchantmentType encType);
     /** 
     * Sets the enchantments for this item.
     * 
     * @param enchants List of enchantments
     */         
    public void setEnchantments(List<MC_Enchantment> enchants);
     /** 
     * Get the enchantments this item has.
     * 
     * @return List of enchantments 
     */         
    public List<MC_Enchantment> getEnchantments();
     /** 
     * Sets the enchantment level of a given enchantment for this item.
     * 
     * @param encType Enchantment Type
     * @param level Enchantment Level
     */         
    public void setEnchantmentLevel(MC_EnchantmentType encType, int level);

     /** 
     * Clone an item 
     * 
     * @return Clone of item 
     */         
    public MC_ItemStack getDuplicate();

     /** 
     * Gets the Lore settings for an item 
     * 
     * @return List of Lore settings as strings 
     */     
    public List<String> getLore();
    
    /** 
     * Sets the Lore settings for an item
     * 
     * @param lore List of Lore settings as strings
     */     
    public void setLore(List<String> lore);

     /** 
     * Convert item to a byte array. Use MC_Server.createItemStack() to construct a new item from this data. 
     * 
     * @return Byte array representation of the item 
     */     
    public byte[] serialize();

     /** 
     * If item is a skull, sets the skull owner. 
     * 
     * @param pName Skull Name
     */     
    public void setSkullOwner(String pName);
}
