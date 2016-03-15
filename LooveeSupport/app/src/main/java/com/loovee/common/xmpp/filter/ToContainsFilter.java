package com.loovee.common.xmpp.filter;

import com.loovee.common.xmpp.packet.Packet;

import java.util.Locale;

public class ToContainsFilter implements PacketFilter {
	private String to;

	public ToContainsFilter(String to) {
		if (to == null) {
			throw new IllegalArgumentException("Parameter cannot be null.");
		}
		this.to = to.toLowerCase(Locale.ENGLISH);
	}

	public boolean accept(Packet packet) {
		if (packet.getTo() == null) {
			return false;
		}

		return packet.getTo().toLowerCase(Locale.ENGLISH).indexOf(this.to) != -1;
	}
}