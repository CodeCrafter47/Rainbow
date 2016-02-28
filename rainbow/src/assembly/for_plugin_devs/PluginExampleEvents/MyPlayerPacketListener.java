package PluginExampleEvents;

import PluginReference.MC_EventInfo;
import PluginReference.MC_Player;
import PluginReference.MC_PlayerPacketListener;

public class MyPlayerPacketListener implements MC_PlayerPacketListener
{

	@Override
	public byte[] handleRawPacket(MC_Player plr, int packetID, byte[] data, String internalPacketObject, MC_EventInfo ei)
	{
		// Show packet received...
		//System.out.println("[PluginExampleEvents] " + String.format("%-30s ID: %-2d by %-20s. Bytes: %6d", internalPacketClassName, packetID, plr.getName(), data.length)); 

		// Chat Packet...
		if(packetID == 2)
		{
			// Show Chat Data we see...
			String chatData = "?";
			try
			{
				chatData = new String(data, "UTF-8");
			}
			catch(Exception exc)
			{
				
			}
			
			System.out.println("------------------------------------------");
			System.out.println(chatData);
			System.out.println("------------------------------------------");
			
			// Change "zz" to "yy" if found...
			for(int i=0; i<data.length-1; i++)
			{
				if((data[i] == (byte)'z') && (data[i+1] == (byte)'z')) 
				{
					data[i] = 'y';
					data[i+1] = 'y';
					ei.isModified = true;
				}
			}
			
		}
		return data;
	}

}
