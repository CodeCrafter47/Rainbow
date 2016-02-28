package PluginExampleEvents;

import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;

import PluginReference.*;

public class MyServerPacketListener  implements MC_ServerPacketListener
{

	@Override
	public byte[] handleRawPacket(SocketAddress addr, int packetID, byte[] data, String internalPacketObject, MC_EventInfo ei)
	{
		/*
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
			
			// Change "kk" to "KK" if found...
			for(int i=0; i<data.length-1; i++)
			{
				if((data[i] == (byte)'t') && (data[i+1] == (byte)'W')) 
				{
					data[i] = 'K';
					data[i+1] = 'K';
					ei.isModified = true;
				}
			}
			
		}*/
		//if(!internalPacketObject.equalsIgnoreCase("Packet_IncomingPlayer")) System.out.println("INC PACKET: " + internalPacketObject);
		
		if(packetID == 0x0B)
		{
			if(data.length > 1)
			{
				int action = data[data.length-2];
				if(action == 0) System.out.println(addr.toString() + " --- Crouch ---");
				if(action == 1) System.out.println(addr.toString() + " --- Uncrouch ---");
				if(action == 2) System.out.println(addr.toString() + " --- Leave Bed ---");
				if(action == 3) System.out.println(addr.toString() + " --- Start Sprinting ---");
				if(action == 4) System.out.println(addr.toString() + " --- Stop Sprinting ---");
				if(action == 5) System.out.println(addr.toString() + " --- Jump with horse ---");
				if(action == 6) System.out.println(addr.toString() + " --- Open Inventory ---");
				//List<String> arr = new ArrayList<String>();
				//for(int i=0; i<data.length; i++) arr.add((int)data[i] + "");
				//System.out.println("Packet EntityAction: " + RainbowUtils.GetCommaList(arr));
			}
			
		}
		
		return data;
	}

}
