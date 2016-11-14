package PluginReference;

import java.net.SocketAddress;

/** 
 * Interface for handling raw network packets incoming to server.
 */ 			
public interface MC_ServerPacketListener
{
	 /** 
     * Method for handling raw packet data
     * 
     * @param addr Source network address
     * @param packetID Minecraft Packet ID
     * @param data Raw byte data
     * @param internalPacketClassName Internal Packet name
     * @param ei Event Info w/option to cancel
     * @return Packet data 
     */ 			
	public byte[] handleRawPacket(SocketAddress addr, int packetID, byte[] data, String internalPacketClassName, MC_EventInfo ei);
}

