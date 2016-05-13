package PluginExample.commands;

import PluginExample.CmdBase;
import PluginReference.MC_Attribute;
import PluginReference.MC_AttributeModifier;
import PluginReference.MC_AttributeType;
import PluginReference.MC_Entity;
import PluginReference.MC_EntityType;
import PluginReference.MC_LivingEntity;
import PluginReference.MC_Location;
import PluginReference.MC_Player;
import PluginReference.RainbowUtils;

public class CmdAttributeTest extends CmdBase {
    public CmdAttributeTest() {
        super("attributetest", "Spawn a pig with modified attributes");
    }

    @Override
    protected void execute(MC_Player plr, String[] args) {
        MC_Location location = plr.getLocation();

        MC_LivingEntity entity = (MC_LivingEntity) plr.getWorld().spawnEntity(MC_EntityType.PIG, location, "Super Pig");

        MC_Attribute speed = entity.getAttribute(MC_AttributeType.MOVEMENT_SPEED);
        speed.addModifier(RainbowUtils.getServer().createAttributeModifier("Speed x10", MC_AttributeModifier.Operator.ADD_SCALAR, 9));

        MC_Attribute maxHealth = entity.getAttribute(MC_AttributeType.MAX_HEALTH);
        maxHealth.addModifier(RainbowUtils.getServer().createAttributeModifier("Health +20", MC_AttributeModifier.Operator.ADD_CONSTANT, 20));

        entity.setHealth(entity.getMaxHealth());
    }
}
