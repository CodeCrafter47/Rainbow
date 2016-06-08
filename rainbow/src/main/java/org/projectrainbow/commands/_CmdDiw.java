package org.projectrainbow.commands;

import PluginReference.MC_Player;
import PluginReference.RainbowUtils;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandManager;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import org.projectrainbow._ColorHelper;
import org.projectrainbow._DiwUtils;
import org.projectrainbow.interfaces.IMixinICommandSender;
import org.projectrainbow.interfaces.IMixinPlayerCapabilities;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class _CmdDiw extends CommandBase {
    public static long ServerStartTime = 0L;

    @Override
    public String getCommandName() {
        return "diw";
    }

    @Override
    public boolean checkPermission(MinecraftServer minecraftServer, ICommandSender cs) {
        return (!(cs instanceof MC_Player)) || ((MC_Player) cs).hasPermission("rainbow.diw");
    }

    public void HandleDiwGrass(EntityPlayer p, int radius) {
        int x = (int) p.lastTickPosX;
        int y = (int) p.lastTickPosY;
        int z = (int) p.lastTickPosZ;

        for (int dx = -1 * radius; dx <= radius; ++dx) {
            for (int dz = -1 * radius; dz <= radius; ++dz) {
                for (int dy = -4; dy < 0; ++dy) {
                    if (dy >= -1) {
                        p.getEntityWorld().setBlockState(new BlockPos(x + dx, y + dy, z + dz), Blocks.GRASS.getDefaultState(), 3);
                    } else {
                        p.getEntityWorld().setBlockState(new BlockPos(x + dx, y + dy, z + dz), Blocks.DIRT.getDefaultState(), 3);
                    }
                }
            }
        }

        ((IMixinICommandSender) p).sendMessage(_ColorHelper.GREEN + "Grass set around you! " + _ColorHelper.AQUA + "Radius: " + radius);
    }

    public void HandleDiwBorder(EntityPlayer p, int radius) {
        int x = (int) p.lastTickPosX;
        int y = (int) p.lastTickPosY;
        int z = (int) p.lastTickPosZ;

        for (int dx = -1 * radius; dx <= radius; ++dx) {
            int dy = 0;
            p.getEntityWorld().setBlockState(new BlockPos(x + dx, y + dy, z + radius), Blocks.COBBLESTONE_WALL.getDefaultState(), 3);
            int dz = -1 * radius;
            p.getEntityWorld().setBlockState(new BlockPos(x + dx, y + dy, z + dz), Blocks.COBBLESTONE_WALL.getDefaultState(), 3);
        }

        for (int dz = -1 * radius; dz <= radius; ++dz) {
            int dy = 0;
            p.getEntityWorld().setBlockState(new BlockPos(x + radius, y + dy, z + dz), Blocks.COBBLESTONE_WALL.getDefaultState(), 3);
            int dx = -1 * radius;
            p.getEntityWorld().setBlockState(new BlockPos(x + dx, y + dy, z + dz), Blocks.COBBLESTONE_WALL.getDefaultState(), 3);
        }

        ((IMixinICommandSender) p).sendMessage(_ColorHelper.GREEN + "Wall set around you! " + _ColorHelper.AQUA + "Radius: " + radius);
    }

    public void HandleDiwSky(EntityPlayer p, int radius) {
        int x = (int) p.lastTickPosX;
        int y = (int) p.lastTickPosY;
        int z = (int) p.lastTickPosZ;
        int maxHeight = _DiwUtils.getMinecraftServer().getBuildLimit();

        for (int dx = -1 * radius; dx <= radius; ++dx) {
            for (int dz = -1 * radius; dz <= radius; ++dz) {
                for (int dy = 0; y + dy < maxHeight; ++dy) {
                    p.getEntityWorld().setBlockState(new BlockPos(x + dx, y + dy, z + dz), Blocks.AIR.getDefaultState(), 3);
                }
            }
        }

        ((IMixinICommandSender) p).sendMessage(_ColorHelper.GREEN + "Sky cleared around you! " + _ColorHelper.AQUA + "Radius: " + radius);
    }

    public void HandleScript(EntityPlayer plr, String fname, String[] args) {
        String plrName = "CONSOLE";
        if (plr != null) {
            plrName = plr.getName();
        }

        System.out.println("[Script] Executing File: " + fname);
        File file = new File(fname);

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            ICommandSender sender = _DiwUtils.getMinecraftServer();
            if (plr != null) {
                sender = plr;
            }

            ICommandManager executor = _DiwUtils.getMinecraftServer().getCommandManager();

            String line;
            while ((line = br.readLine()) != null) {
                if (!line.isEmpty()) {
                    line = line.trim();
                    String lwr = line.toLowerCase();
                    if (!lwr.startsWith(";")) {
                        try {
                            for (int i = 0; i < 10 && line.indexOf(36) >= 0; ++i) {
                                line = _DiwUtils.StringReplace(line, "$ME$", plrName);

                                for (int j = 2; j < 10; ++j) {
                                    String key = " $ARG" + (j - 1) + "$";
                                    String tgt = "";
                                    if (j < args.length) {
                                        tgt = " " + args[j];
                                    }

                                    line = _DiwUtils.StringReplace(line, key, tgt);
                                }
                            }

                            line = _DiwUtils.FullTranslate(line);
                            System.out.println("[Script] Running: " + line);
                            executor.executeCommand(sender, line);
                        } catch (Exception var15) {
                            System.out.println("[Script] Command Error: " + var15.getMessage());
                        }
                    }
                }
            }

            br.close();
        } catch (Exception var16) {
            var16.printStackTrace();
        }

    }

    @Override
    public String getCommandUsage(ICommandSender cs) {
        return _ColorHelper.LIGHT_PURPLE + "/diw" + _ColorHelper.WHITE + " --- Admin feature";
    }

    public void ShowUsage(ICommandSender cs) {
        _DiwUtils.reply(cs, _ColorHelper.RED + "Usage: /diw [option]");
        String[] arrCmds = new String[]{"save", "speed", "flyspeed", "walkspeed", "mem", "skyclear", "setgrass", "border", "echo", "script"};
        List<String> cmds = Arrays.asList(arrCmds);
        Collections.sort(cmds);
        _DiwUtils.reply(cs, _ColorHelper.WHITE + "Options: " + RainbowUtils.RainbowStringList(cmds));
    }

    @Override
    public void execute(MinecraftServer minecraftServer, ICommandSender cs, String[] args) throws CommandException {
        EntityPlayerMP p = null;
        if (cs instanceof EntityPlayerMP) {
            p = (EntityPlayerMP) cs;
        }

        if (args.length <= 0) {
            this.ShowUsage(cs);
        } else if (args[0].equalsIgnoreCase("save")) {
            _DiwUtils.SaveStuffs();
            _DiwUtils.reply(cs, _ColorHelper.GREEN + "Rainbow data saved.");
        } else if (args[0].equalsIgnoreCase("flyspeed") && p != null) {
            if (args.length < 2) {
                ((IMixinICommandSender) p).sendMessage(_ColorHelper.RED + "Example 1: " + _ColorHelper.GOLD + "/diw flySpeed 2");
            } else {
                float spd = Float.parseFloat(args[1]);
                ((IMixinPlayerCapabilities) p.capabilities).setFlySpeed(spd * 0.05F);
                ((IMixinICommandSender) p).sendMessage(_ColorHelper.GREEN + String.format("Set fly speed to %.2f", spd));
                p.sendPlayerAbilities();
            }
        } else if (args[0].equalsIgnoreCase("speed") && p != null) {
            ((IMixinICommandSender) p).sendMessage(_ColorHelper.GOLD + String.format("Fly Speed: %.2f, Walk Speed: %.2f", (double) p.capabilities.getFlySpeed() / 0.05D, (double) p.capabilities.getWalkSpeed() / 0.1D));
            ((IMixinICommandSender) p).sendMessage(_ColorHelper.GOLD + String.format("MayFly: %s, IsFlying: %s", p.capabilities.allowFlying, p.capabilities.isFlying));
        } else if (args[0].equalsIgnoreCase("walkspeed") && p != null) {
            if (args.length < 2) {
                ((IMixinICommandSender) p).sendMessage(_ColorHelper.RED + "Example 1: " + _ColorHelper.GOLD + "/diw walkspeed 2");
            } else {
                float spd = Float.parseFloat(args[1]) * 0.1F;
                ((IMixinPlayerCapabilities) p.capabilities).setWalkSpeed(spd);
                ((IMixinICommandSender) p).sendMessage(_ColorHelper.GREEN + String.format("Set walk speed to %.2f", spd));
            }
        } else if ((args[0].equalsIgnoreCase("setgrass") || args[0].equalsIgnoreCase("skyclear")) && p != null) {
            int radius = 5;

            try {
                radius = Integer.parseInt(args[1].trim());
            } catch (Throwable var18) {
                radius = 5;
            }

            if (radius < 0) {
                radius *= -1;
            }

            if (radius >= 200) {
                radius = 200;
            }

            if (args[0].equalsIgnoreCase("setgrass")) {
                this.HandleDiwGrass(p, radius);
            } else {
                this.HandleDiwSky(p, radius);
            }

        } else if (args[0].equalsIgnoreCase("border") && p != null) {
            int radius = 5;

            try {
                radius = Integer.parseInt(args[1].trim());
            } catch (Throwable var19) {
                radius = 5;
            }

            if (radius < 0) {
                radius *= -1;
            }

            if (radius >= 200) {
                radius = 200;
            }

            this.HandleDiwBorder(p, radius);
        } else if (args[0].equalsIgnoreCase("echo")) {
            if (args.length <= 1) {
                _DiwUtils.reply(cs, _ColorHelper.RED + "Usage: /diw echo " + _ColorHelper.YELLOW + "your message to everyone.");
            } else {
                String msg = _DiwUtils.ConcatArgs(args, 1);
                msg = _DiwUtils.SpecialTranslate(msg);
                msg = _DiwUtils.TranslateColorString(msg, true);
                _DiwUtils.MessageAllPlayers(msg);
            }
        } else if (!args[0].equalsIgnoreCase("mem") && !args[0].equalsIgnoreCase("jc")) {
            if (args[0].equalsIgnoreCase("script")) {
                if (args.length <= 1) {
                    _DiwUtils.reply(cs, _ColorHelper.RED + "Usage: " + _ColorHelper.LIGHT_PURPLE + "/diw script " + _ColorHelper.GOLD + "Filename" + _ColorHelper.DARK_AQUA + " [Parm1] [Parm2] ...");
                } else {
                    String fname = _DiwUtils.RainbowDataDirectory + "Scripts" + File.separator + args[1];
                    if (!fname.endsWith(".txt")) {
                        fname = fname + ".txt";
                    }

                    File f = new File(fname);
                    if (!f.exists()) {
                        _DiwUtils.reply(cs, _ColorHelper.RED + "File not found: " + _ColorHelper.YELLOW + fname);
                    } else {
                        this.HandleScript(p, fname, args);
                    }
                }
            } else {
                _DiwUtils.reply(cs, _ColorHelper.RED + "Unknown Option: " + _ColorHelper.AQUA + args[0]);
                this.ShowUsage(cs);
            }
        } else {
            Runtime rt = Runtime.getRuntime();
            double maxGig = (double) ((float) rt.maxMemory() / 1.07374182E9F);
            double allocGig = (double) ((float) rt.totalMemory() / 1.07374182E9F);
            double freeGig = (double) ((float) rt.freeMemory() / 1.07374182E9F);
            double usagePerc = 100.0D * (allocGig - freeGig) / maxGig;
            String usageClr = _ColorHelper.GREEN;
            String usageMsg = " Good";
            if (usagePerc > 50.0D) {
                usageClr = _ColorHelper.YELLOW;
                usageMsg = " OK";
            }

            if (usagePerc > 90.0D) {
                usageClr = _ColorHelper.DARK_RED;
                usageMsg = " High";
            }

            String prefix = _ColorHelper.GOLD + "☕ " + _ColorHelper.LIGHT_PURPLE;
            int labelLen = 11;
            String uptime = _DiwUtils.TimeDeltaString(System.currentTimeMillis() - _DiwUtils.ServerStartTime);
            _DiwUtils.reply(cs, prefix + "Uptime" + _ColorHelper.DARK_GRAY + RainbowUtils.TextAlignTrailerPerfect("Uptime", labelLen) + _ColorHelper.AQUA + uptime);
            _DiwUtils.reply(cs, prefix + "RAM Usage" + _ColorHelper.DARK_GRAY + RainbowUtils.TextAlignTrailerPerfect("RAM Usage", labelLen) + usageClr + String.format(usageClr + "%.1fperc %s " + _ColorHelper.GRAY + "(%.1f of %.1f GB)", usagePerc, usageMsg, allocGig - freeGig, maxGig));
        }
    }
}
