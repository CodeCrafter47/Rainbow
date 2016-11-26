package org.projectrainbow;


import PluginReference.*;
import joebkt._JOT_OnlineTimeEntry;
import joebkt._SerializableLocation;
import net.md_5.bungee.api.chat.BaseComponent;
import org.projectrainbow.commands._CmdNameColor;

import java.net.SocketAddress;
import java.util.List;
import java.util.UUID;


public class OfflinePlayerWrapper implements MC_Player {

    private static void unsupported() {
        throw new UnsupportedOperationException("This method is not supported for offline players");
    }

    public String uuid = null;

    public OfflinePlayerWrapper(String uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return ServerWrapper.getInstance().getLastKnownPlayerNameFromUUID(uuid);
    }

    public UUID getUUID() {
        return UUID.fromString(uuid);
    }

    public void sendMessage(String msg) {
        unsupported();
    }

    @Override
    public void sendJsonMessage(String json) {
        unsupported();
    }

    @Override
    public void sendMessage(BaseComponent... msg) {
        unsupported();
    }

    public void executeCommand(String cmd) {
        unsupported();
    }

    public boolean isOp() {
        unsupported();
        return false;
    }

    public String getIPAddress() {
        unsupported();
        return "";
    }

    public MC_World getWorld() {
        unsupported();
        return null;
    }

    public MC_GameMode getGameMode() {
        unsupported();
        return MC_GameMode.UNKNOWN;
    }

    public MC_ItemStack getItemInHand() {
        unsupported();
        return null;
    }

    public double getEconomyBalance() {
        return _EconomyManager.GetBalance(getUUID());
    }

    public void setEconomyBalance(double amt) {
        _EconomyManager.SetBalance(getUUID(), amt);
    }

    public float getHealth() {
        unsupported();
        return 0.0f;
    }

    public void setHealth(float argHealth) {
        unsupported();
    }

    public int getFoodLevel() {
        unsupported();
        return 0;
    }

    public void setFoodLevel(int argFoodLevel) {
        unsupported();
    }

    public MC_Location getLocation() {
        unsupported();
        return null;
    }

    public MC_Server getServer() {
        unsupported();
        return null;
    }

    public void teleport(MC_Location arg0) {
        unsupported();
    }

    @Override
    public void teleport(MC_Location loc, boolean safe) {
        unsupported();
    }

    public List<MC_ItemStack> getInventory() {
        unsupported();
        return null;
    }

    public void updateInventory() {
        unsupported();
    }

    public void setInventory(List<MC_ItemStack> arg0) {
        unsupported();
    }

    public MC_EntityType getType() {
        return MC_EntityType.PLAYER;
    }

    public String internalInfo() {
        return this.getName();
    }

    public boolean isDead() {
        unsupported();
        return false;
    }

    public void kill() {
        unsupported();
    }

    public boolean isInvisible() {
        unsupported();
        return false;
    }

    public boolean isSneaking() {
        unsupported();
        return false;
    }

    public boolean isSprinting() {
        unsupported();
        return false;
    }

    public MC_Entity getRider() {
        unsupported();
        return null;
    }

    public MC_Entity getVehicle() {
        unsupported();
        return null;
    }

    public void setRider(MC_Entity arg0) {
        unsupported();
    }

    public void setVehicle(MC_Entity arg0) {
        unsupported();
    }

    public boolean isInvulnerable() {
        unsupported();
        return false;
    }

    public void setInvulnerable(boolean arg0) {
        unsupported();
    }

    public List<MC_ItemStack> getArmor() {
        unsupported();
        return null;
    }

    public void setArmor(List<MC_ItemStack> arg0) {
        unsupported();
    }

    public void setItemInHand(MC_ItemStack arg0) {
        unsupported();
    }

    public boolean hasPermission(String arg0) {
        unsupported();
        return false;
    }

    public void setGameMode(MC_GameMode arg0) {
        unsupported();
    }

    public void setInvisible(boolean arg0) {
        unsupported();
    }

    public boolean isSleeping() {
        unsupported();
        return false;
    }

    public MC_Location getCompassTarget() {
        unsupported();
        return null;
    }

    public void setCompassTarget(MC_Location arg0) {
        unsupported();
    }

    public float getFlySpeed() {
        unsupported();
        return 0.0F;
    }

    public float getWalkSpeed() {
        unsupported();
        return 0.0F;
    }

    public boolean isAllowedFlight() {
        unsupported();
        return false;
    }

    public boolean isFlying() {
        unsupported();
        return false;
    }

    public void setAllowFlight(boolean arg0) {
        unsupported();
    }

    public void setFlySpeed(float arg0) {
        unsupported();
    }

    public void setFlying(boolean arg0) {
        unsupported();
    }

    public void setWalkSpeed(float arg0) {
        unsupported();
    }

    public int getFireTicks() {
        return 0;
    }

    public void setFireTicks(int arg0) {
        unsupported();
    }

    public void playSound(String arg0, float arg1, float arg2) {
        unsupported();
    }

    public MC_MotionData getMotionData() {
        return null;
    }

    public void setMotionData(MC_MotionData arg0) {
        unsupported();
    }

    public float getExp() {
        unsupported();
        return 0.0F;
    }

    public int getLevel() {
        unsupported();
        return 0;
    }

    public int getTotalExperience() {
        unsupported();
        return 0;
    }

    public void giveExp(int arg0) {
        unsupported();
    }

    public void giveExpLevels(int arg0) {
        unsupported();
    }

    public void setExp(float arg0) {
        unsupported();
    }

    public void setLevel(int arg0) {
        unsupported();
    }

    public void setTotalExperience(int arg0) {
        unsupported();
    }

    public boolean hasCustomName() {
        String key = uuid;
        String customName = (String) _CmdNameColor.ColorNameDict.get(key);

        return customName == null ? false : customName.equalsIgnoreCase(getName());
    }

    public void setCustomName(String newName) {
        String key = uuid;

        if (newName != null && newName.length() > 0) {
            _CmdNameColor.ColorNameDict.put(key, newName);
        } else {
            _CmdNameColor.ColorNameDict.remove(key);
        }
    }

    public String getCustomName() {
        String key = uuid;
        String customName = (String) _CmdNameColor.ColorNameDict.get(key);

        return customName == null ? getName() : customName;
    }

    public float getMaxHealth() {
        unsupported();
        return 0.0F;
    }

    public void setMaxHealth(float arg0) {
        unsupported();
    }

    public MC_Location getBedRespawnLocation() {
        unsupported();
        return null;
    }

    public void setBedRespawnLocation(MC_Location arg0, boolean arg1) {
        unsupported();
    }

    public float getFoodRegenAmount() {
        unsupported();
        return 0.0F;
    }

    public void setFoodRegenAmount(float arg0) {
        unsupported();
    }

    public int getBaseArmorScore() {
        unsupported();
        return 0;
    }

    public float getArmorAdjustedDamage(MC_DamageType arg0, float arg1) {
        unsupported();
        return 0.0F;
    }

    public float getAbsorptionAmount() {
        unsupported();
        return 0.0F;
    }

    public float getTotalAdjustedDamage(MC_DamageType arg0, float arg1) {
        unsupported();
        return 0.0F;
    }

    public void setAbsorptionAmount(float arg0) {
        unsupported();
    }

    public void setNumberOfArrowsHitWith(int arg0) {
        unsupported();
    }

    public void kick(String arg0) {
        unsupported();
    }

    public MC_Entity getAttacker() {
        unsupported();
        return null;
    }

    public List<MC_PotionEffect> getPotionEffects() {
        unsupported();
        return null;
    }

    public void setPotionEffects(List<MC_PotionEffect> arg0) {
        unsupported();
    }

    public void setAdjustedIncomingDamage(float arg0) {
        unsupported();
    }

    public SocketAddress getSocketAddress() {
        unsupported();
        return null;
    }

    public int getEntityId() {
        unsupported();
        return 0;
    }

    public List<MC_Entity> getNearbyEntities(float arg0) {
        unsupported();
        return null;
    }

    public MC_Entity getEntitySpectated() {
        unsupported();
        return null;
    }

    public void spectateEntity(MC_Entity arg0) {
        unsupported();
    }

    @Override
    public MC_ItemStack getItemInOffHand() {
        unsupported();
        return null;
    }

    @Override
    public void setItemInOffHand(MC_ItemStack item) {
        unsupported();
    }

    public void removeEntity() {
        unsupported();
    }

    @Override
    public List<MC_Entity> getRiders() {
        unsupported();
        return null;
    }

    @Override
    public void addRider(MC_Entity ent) {
        unsupported();
    }

    @Override
    public void removeRider(MC_Entity ent) {
        unsupported();
    }

    @Override
    public byte[] serialize() {
        unsupported();
        return null;
    }

    @Override
    public MC_Attribute getAttribute(MC_AttributeType type) {
        unsupported();
        return null;
    }

    @Override
    public void setPlayerListHeaderFooter(String header, String footer) {
        unsupported();
    }

    @Override
    public void displayInventoryGUI(MC_InventoryGUI gui) {
        unsupported();
    }

    @Override
    public void closeInventory() {
        unsupported();
    }

    @Override
    public boolean hasPlayedBefore() {
        _JOT_OnlineTimeEntry entry = _JOT_OnlineTimeUtils.Data.playerData.get(getUUID().toString());
        return entry != null && entry.msTotal > 0;
    }

    @Override
    public long getOnlineTime() {
        _JOT_OnlineTimeEntry entry = _JOT_OnlineTimeUtils.Data.playerData.get(getUUID().toString());
        return entry == null ? 0 : entry.msTotal + System.currentTimeMillis() - entry.msLastLogin;
    }

    @Override
    public MC_Location getHome() {
        _SerializableLocation sloc = _HomeUtils.getHome(getUUID());
        return new MC_Location(sloc.x, sloc.y, sloc.z, sloc.dimension, sloc.yaw, sloc.pitch);
    }

    @Override
    public void setHome(MC_Location location) {
        _HomeUtils.setHome(getUUID(), new _SerializableLocation(
                location.x, location.y, location.z,
                location.dimension, location.yaw, location.pitch
        ));
    }

    @Override
    public MC_Location getHome2() {
        _SerializableLocation sloc = _HomeUtils.getHome2(getUUID());
        return new MC_Location(sloc.x, sloc.y, sloc.z, sloc.dimension, sloc.yaw, sloc.pitch);
    }

    @Override
    public void setHome2(MC_Location location) {
        _HomeUtils.setHome2(getUUID(), new _SerializableLocation(
                location.x, location.y, location.z,
                location.dimension, location.yaw, location.pitch
        ));
    }
}
