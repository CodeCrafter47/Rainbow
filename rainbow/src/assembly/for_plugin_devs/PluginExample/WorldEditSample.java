package PluginExample;

import java.util.concurrent.ConcurrentHashMap;

import PluginReference.*;

public class WorldEditSample
{
	public static ConcurrentHashMap<String, MC_Location> pos1 = new ConcurrentHashMap<String, MC_Location>();
	public static ConcurrentHashMap<String, MC_Location> pos2 = new ConcurrentHashMap<String, MC_Location>();
	
	public static void SetPosition1(MC_Player plr, MC_Location loc)
	{
		pos1.put(plr.getName(), loc);
	}
	public static void SetPosition2(MC_Player plr, MC_Location loc)
	{
		pos2.put(plr.getName(), loc);
	}
	
	public static void SetToBlock(MC_Player plr, String blockName)
	{
		String pName = plr.getName();
		MC_Location loc1 = pos1.get(pName);
		MC_Location loc2 = pos2.get(pName);
		if(loc1 == null)
		{
			plr.sendMessage(ChatColor.RED + "Position 1 Not Set!");
			return;
		}
		if(loc2 == null)
		{
			plr.sendMessage(ChatColor.RED + "Position 2 Not Set!");
			return;
		}
		
		MC_World world = plr.getWorld();
		MC_Block blk = world.getBlockFromName(blockName.toLowerCase());
		if(blk == null)
		{
			plr.sendMessage(ChatColor.RED + "Unknown Block Name: " + ChatColor.GOLD + blockName);
			return;
		}
		int sx = loc1.getBlockX();
		int sy = loc1.getBlockY();
		int sz = loc1.getBlockZ();
		
		int ex = loc2.getBlockX();
		int ey = loc2.getBlockY();
		int ez = loc2.getBlockZ();
		
		if(sx > ex) 
		{
			int tmp = sx;
			sx = ex;
			ex = tmp;
		}

		if(sy > ey) 
		{
			int tmp = sy;
			sy = ey;
			ey = tmp;
		}
		if(sz > ez) 
		{
			int tmp = sz;
			sz = ez;
			ez = tmp;
		}
		
		int nBlocks = 0;
		for(int x=sx; x<=ex; x++)
		{
			for(int z=sz; z<=ez; z++)
			{
				for(int y=sy; y<=ey; y++)
				{
					nBlocks++;
					world.setBlockAt(x,  y, z, blk, 0);
				}
			}
		}
		
		plr.sendMessage(ChatColor.GREEN + "Set " + ChatColor.AQUA + nBlocks + ChatColor.GREEN + " blocks to " + ChatColor.GOLD + blockName); 
	}

	public static void WallsBlock(MC_Player plr, String blockName)
	{
		String pName = plr.getName();
		MC_Location loc1 = pos1.get(pName);
		MC_Location loc2 = pos2.get(pName);
		if(loc1 == null)
		{
			plr.sendMessage(ChatColor.RED + "Position 1 Not Set!");
			return;
		}
		if(loc2 == null)
		{
			plr.sendMessage(ChatColor.RED + "Position 2 Not Set!");
			return;
		}
		
		MC_World world = plr.getWorld();
		MC_Block blk = world.getBlockFromName(blockName.toLowerCase());
		if(blk == null)
		{
			plr.sendMessage(ChatColor.RED + "Unknown Block Name: " + ChatColor.GOLD + blockName);
			return;
		}
		int sx = loc1.getBlockX();
		int sy = loc1.getBlockY();
		int sz = loc1.getBlockZ();
		
		int ex = loc2.getBlockX();
		int ey = loc2.getBlockY();
		int ez = loc2.getBlockZ();
		
		if(sx > ex) 
		{
			int tmp = sx;
			sx = ex;
			ex = tmp;
		}

		if(sy > ey) 
		{
			int tmp = sy;
			sy = ey;
			ey = tmp;
		}
		if(sz > ez) 
		{
			int tmp = sz;
			sz = ez;
			ez = tmp;
		}
		
		int nBlocks = 0;
		for(int x=sx; x<=ex; x++)
		{
			for(int y=sy; y<=ey; y++)
			{
				nBlocks++;
				world.setBlockAt(x,  y, sz, blk, 0);
				world.setBlockAt(x,  y, ez, blk, 0);
			}
		}
		for(int z=sz; z<=ez; z++)
		{
			for(int y=sy; y<=ey; y++)
			{
				nBlocks++;
				world.setBlockAt(sx,  y, z, blk, 0);
				world.setBlockAt(ex,  y, z, blk, 0);
			}
		}
		
		plr.sendMessage(ChatColor.GREEN + "Walled " + ChatColor.AQUA + nBlocks + ChatColor.GREEN + " blocks to " + ChatColor.GOLD + blockName); 
	}
	
}
