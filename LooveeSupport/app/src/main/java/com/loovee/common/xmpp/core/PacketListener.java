package com.loovee.common.xmpp.core;

import com.loovee.common.xmpp.packet.Packet;


public abstract interface PacketListener {
	
  public abstract void processPacket(Packet packet);
  
}
