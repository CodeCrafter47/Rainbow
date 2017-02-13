package PluginReference;

/** 
 * Interface for handling raw network packets outgoing to players.
 */             
public interface MC_PlayerPacketListener
{
     /** 
     * Method for handling raw packet data
     * 
     * @param plr Player object
     * @param packetID Minecraft Packet ID
     * @param data Raw byte data
     * @param internalPacketClassName internal Packet name
     * @param ei Event Info w/option to cancel
     * @return Packet data 
     */             
    public byte[] handleRawPacket(MC_Player plr, int packetID, byte[] data, String internalPacketClassName, MC_EventInfo ei);
}

