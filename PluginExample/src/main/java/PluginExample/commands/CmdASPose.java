package PluginExample.commands;

import PluginExample.CmdBase;
import PluginReference.ChatColor;
import PluginReference.MC_ArmorStand;
import PluginReference.MC_Entity;
import PluginReference.MC_FloatTriplet;
import PluginReference.MC_Location;
import PluginReference.MC_Player;

import java.util.List;

public class CmdASPose extends CmdBase {
    public CmdASPose() {
        super("aspose", "Change Armor Stand pose");
    }

    @Override
    protected void execute(MC_Player plr, String[] args) {
        int slotIdx = 0;
        float val1 = 0;
        float val2 = 0;
        float val3 = 0;
        try {
            slotIdx = Integer.parseInt(args[0]);
            val1 = Float.parseFloat(args[1]);
            val2 = Float.parseFloat(args[2]);
            val3 = Float.parseFloat(args[3]);
        } catch (Exception exc) {
            plr.sendMessage(ChatColor.RED + "Usage: /aspose Slot#(1-6) Value1 Value2 Value3");
            return;
        }
        slotIdx--;
        if ((slotIdx < 0) || (slotIdx > 5)) {
            plr.sendMessage(ChatColor.RED + "Slot# must be between 1-6");
            return;
        }
        plr.sendMessage(ChatColor.AQUA + String.format("Using Slot %d, x=%.1f, y=%.1f, z=%.1f", slotIdx + 1, val1, val2, val3));

        int nStands = 0;
        for (MC_Entity ent : plr.getWorld().getEntities()) {
            if (!(ent instanceof MC_ArmorStand)) continue;
            MC_ArmorStand stand = (MC_ArmorStand) ent;
            List<MC_FloatTriplet> pose = stand.getPose();
            MC_FloatTriplet entry = pose.get(slotIdx);
            entry.x = val1;
            entry.y = val2;
            entry.z = val3;
            pose.set(slotIdx, entry);
            stand.setPose(pose);
            nStands++;
        }
        plr.sendMessage(ChatColor.GREEN + "Modified " + nStands + " Armor Stands!");
    }
}
