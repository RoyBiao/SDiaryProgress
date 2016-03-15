package com.loovee.common.xmpp.filter;

import com.loovee.common.xmpp.packet.IQ;
import com.loovee.common.xmpp.packet.Packet;

public class IQTypeFilter implements PacketFilter {
	private IQ.Type type;

	public IQTypeFilter(IQ.Type type) {
		this.type = type;
	}

	public boolean accept(Packet packet) {
		return ((packet instanceof IQ))
				&& (((IQ) packet).getType().equals(this.type));
	}
}
