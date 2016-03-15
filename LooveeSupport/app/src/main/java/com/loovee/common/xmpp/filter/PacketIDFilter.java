package com.loovee.common.xmpp.filter;

import com.loovee.common.xmpp.packet.Packet;

public class PacketIDFilter implements PacketFilter {
	private String packetID;

	public PacketIDFilter(String packetID) {
		if (packetID == null) {
			throw new IllegalArgumentException("Packet ID cannot be null.");
		}
		this.packetID = packetID;
	}

	@Override
	public boolean accept(Packet packet) {
		return this.packetID.equals(packet.getPacketID());
	}

	@Override
	public String toString() {
		return "PacketIDFilter by id: " + this.packetID;
	}
}
