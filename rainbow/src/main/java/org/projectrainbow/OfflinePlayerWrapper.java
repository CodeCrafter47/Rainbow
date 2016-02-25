package org.projectrainbow;


import PluginReference.MC_DamageType;
import PluginReference.MC_Entity;
import PluginReference.MC_EntityType;
import PluginReference.MC_GameMode;
import PluginReference.MC_ItemStack;
import PluginReference.MC_Location;
import PluginReference.MC_MotionData;
import PluginReference.MC_Player;
import PluginReference.MC_PotionEffect;
import PluginReference.MC_Server;
import PluginReference.MC_World;
import org.projectrainbow.commands._CmdNameColor;

import java.net.SocketAddress;
import java.util.List;
import java.util.UUID;


public class OfflinePlayerWrapper implements MC_Player {

    public String m_name = null;

    public OfflinePlayerWrapper(String argName) {
        this.m_name = argName;
    }

    public String getName() {
        return this.m_name;
    }

    public UUID getUUID() {
        if (this.m_name == null) {
            return null;
        } else {
            String uid = _UUIDMapper.GetUUIDFromPlayerName(this.m_name);

            return uid == null ? null : UUID.fromString(uid);
        }
    }

    public void sendMessage(String msg) {
        System.out.println(
                "DBG: Unsupported: sendMessage to OfflinePlayer: " + this.m_name);
    }

    public void executeCommand(String cmd) {
        System.out.println(
                "DBG: Unsupported: executeCommand to OfflinePlayer: "
                        + this.m_name);
    }

    public boolean isOp() {
        return false;
    }

    public String getIPAddress() {
        System.out.println(
                "DBG: Unsupported: getIPAddress to OfflinePlayer: "
                        + this.m_name);
        return "";
    }

    public MC_World getWorld() {
        System.out.println(
                "DBG: Unsupported: getWorld to OfflinePlayer: " + this.m_name);
        return null;
    }

    public MC_GameMode getGameMode() {
        System.out.println(
                "DBG: Unsupported: getGameMode to OfflinePlayer: " + this.m_name);
        return MC_GameMode.UNKNOWN;
    }

    public MC_ItemStack getItemInHand() {
        System.out.println(
                "DBG: Unsupported: getItemInHand to OfflinePlayer: "
                        + this.m_name);
        return null;
    }

    public double getEconomyBalance() {
        return _EconomyManager.GetBalance(this.m_name).doubleValue();
    }

    public void setEconomyBalance(double amt) {
        _EconomyManager.SetBalance(this.m_name, Double.valueOf(amt));
    }

    public float getHealth() {
        return 20.0F;
    }

    public void setHealth(float argHealth) {}

    public int getFoodLevel() {
        return 20;
    }

    public void setFoodLevel(int argFoodLevel) {}

    public MC_Location getLocation() {
        System.out.println(
                "DBG: Unsupported: getLocation to OfflinePlayer: " + this.m_name);
        return null;
    }

    public MC_Server getServer() {
        System.out.println(
                "DBG: Unsupported: getServer to OfflinePlayer: " + this.m_name);
        return null;
    }

    public void teleport(MC_Location arg0) {
        System.out.println(
                "DBG: Unsupported: teleport to OfflinePlayer: " + this.m_name);
    }

    public List<MC_ItemStack> getInventory() {
        System.out.println(
                "DBG: Unsupported: getInventory to OfflinePlayer: "
                        + this.m_name);
        return null;
    }

    public void updateInventory() {
        System.out.println(
                "DBG: Unsupported: updateInventory to OfflinePlayer: "
                        + this.m_name);
    }

    public void setInventory(List<MC_ItemStack> arg0) {
        System.out.println(
                "DBG: Unsupported: setInventory to OfflinePlayer: "
                        + this.m_name);
    }

    public MC_EntityType getType() {
        return MC_EntityType.PLAYER;
    }

    public String internalInfo() {
        return this.m_name;
    }

    public boolean isDead() {
        return false;
    }

    public void kill() {}

    public boolean isInvisible() {
        return false;
    }

    public boolean isSneaking() {
        return false;
    }

    public boolean isSprinting() {
        return false;
    }

    public MC_Entity getRider() {
        return null;
    }

    public MC_Entity getVehicle() {
        return null;
    }

    public void setRider(MC_Entity arg0) {}

    public void setVehicle(MC_Entity arg0) {}

    public boolean isInvulnerable() {
        return false;
    }

    public void setInvulnerable(boolean arg0) {}

    public List<MC_ItemStack> getArmor() {
        return null;
    }

    public void setArmor(List<MC_ItemStack> arg0) {}

    public void setItemInHand(MC_ItemStack arg0) {}

    public boolean hasPermission(String arg0) {
        return false;
    }

    public void setGameMode(MC_GameMode arg0) {}

    public void setInvisible(boolean arg0) {}

    public boolean isSleeping() {
        return false;
    }

    public MC_Location getCompassTarget() {
        return null;
    }

    public void setCompassTarget(MC_Location arg0) {}

    public float getFlySpeed() {
        return 0.0F;
    }

    public float getWalkSpeed() {
        return 0.0F;
    }

    public boolean isAllowedFlight() {
        return false;
    }

    public boolean isFlying() {
        return false;
    }

    public void setAllowFlight(boolean arg0) {}

    public void setFlySpeed(float arg0) {}

    public void setFlying(boolean arg0) {}

    public void setWalkSpeed(float arg0) {}

    public int getFireTicks() {
        return 0;
    }

    public void setFireTicks(int arg0) {}

    public void playSound(String arg0, float arg1, float arg2) {}

    public MC_MotionData getMotionData() {
        return null;
    }

    public void setMotionData(MC_MotionData arg0) {}

    public float getExp() {
        return 0.0F;
    }

    public int getLevel() {
        return 0;
    }

    public int getTotalExperience() {
        return 0;
    }

    public void giveExp(int arg0) {}

    public void giveExpLevels(int arg0) {}

    public void setExp(float arg0) {}

    public void setLevel(int arg0) {}

    public void setTotalExperience(int arg0) {}

    public boolean hasCustomName() {
        String pName = this.m_name;
        String key = pName.toLowerCase();
        String customName = (String) _CmdNameColor.ColorNameDict.get(key);

        return customName == null ? false : customName.equalsIgnoreCase(pName);
    }

    public void setCustomName(String newName) {
        String pName = this.m_name;
        String key = pName.toLowerCase();

        if (newName != null && newName.length() > 0) {
            _CmdNameColor.ColorNameDict.put(key, newName);
        } else {
            _CmdNameColor.ColorNameDict.remove(key);
        }
    }

    public String getCustomName() {
        String pName = this.m_name;
        String key = pName.toLowerCase();
        String customName = (String) _CmdNameColor.ColorNameDict.get(key);

        return customName == null ? pName : customName;
    }

    public float getMaxHealth() {
        return 0.0F;
    }

    public void setMaxHealth(float arg0) {}

    public MC_Location getBedRespawnLocation() {
        return null;
    }

    public void setBedRespawnLocation(MC_Location arg0, boolean arg1) {}

    public float getFoodRegenAmount() {
        return 0.0F;
    }

    public void setFoodRegenAmount(float arg0) {}

    public int getBaseArmorScore() {
        return 0;
    }

    public float getArmorAdjustedDamage(MC_DamageType arg0, float arg1) {
        return 0.0F;
    }

    public float getAbsorptionAmount() {
        return 0.0F;
    }

    public float getTotalAdjustedDamage(MC_DamageType arg0, float arg1) {
        return 0.0F;
    }

    public void setAbsorptionAmount(float arg0) {}

    public void setNumberOfArrowsHitWith(int arg0) {}

    public void kick(String arg0) {}

    public MC_Entity getAttacker() {
        return null;
    }

    public List<MC_PotionEffect> getPotionEffects() {
        return null;
    }

    public void setPotionEffects(List<MC_PotionEffect> arg0) {}

    public void setAdjustedIncomingDamage(float arg0) {}

    public SocketAddress getSocketAddress() {
        return null;
    }

    public int getEntityId() {
        return 0;
    }

    public List<MC_Entity> getNearbyEntities(float arg0) {
        return null;
    }

    public MC_Entity getEntitySpectated() {
        return null;
    }

    public void spectateEntity(MC_Entity arg0) {}

    @Override
    public MC_ItemStack getItemInOffHand() {
        return null;
    }

    @Override
    public void setItemInOffHand(MC_ItemStack item) {

    }

    public void removeEntity() {}
}
