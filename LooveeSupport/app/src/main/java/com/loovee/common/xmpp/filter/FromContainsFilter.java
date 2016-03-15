package com.loovee.common.xmpp.filter;

import com.loovee.common.xmpp.packet.Packet;

import java.util.Locale;

public class FromContainsFilter implements PacketFilter {
	private String from;

	public FromContainsFilter(String from) {
		if (from == null) {
			throw new IllegalArgumentException("Parameter cannot be null.");
		}
		this.from = from.toLowerCase(Locale.ENGLISH);
	}

	public boolean accept(Packet packet) {
		if (packet.getFrom() == null) {
			return false;
		}

		return packet.getFrom().toLowerCase(Locale.ENGLISH).indexOf(this.from) != -1;
	}
}
