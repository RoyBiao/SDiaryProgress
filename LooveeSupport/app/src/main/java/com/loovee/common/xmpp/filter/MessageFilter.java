package com.loovee.common.xmpp.filter;

import com.loovee.common.xmpp.packet.Message;
import com.loovee.common.xmpp.packet.Packet;

public class MessageFilter implements PacketFilter{

	@Override
	public boolean accept(Packet packet) {
		if(packet instanceof Message) {
			return true;
		}
		return false;
	}

}
