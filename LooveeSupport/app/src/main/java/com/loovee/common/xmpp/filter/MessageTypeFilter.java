package com.loovee.common.xmpp.filter;

import com.loovee.common.xmpp.packet.Message;
import com.loovee.common.xmpp.packet.Packet;


public class MessageTypeFilter implements PacketFilter {
	private final Message.Type type;

	public MessageTypeFilter(Message.Type type) {
		this.type = type;
	}

	@Override
	public boolean accept(Packet packet) {
		if (!(packet instanceof Message)) {
			return false;
		}
		return ((Message) packet).getType().equals(this.type);
	}
}
