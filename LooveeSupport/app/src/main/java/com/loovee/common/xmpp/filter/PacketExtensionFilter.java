package com.loovee.common.xmpp.filter;

import com.loovee.common.xmpp.packet.Packet;

public class PacketExtensionFilter implements PacketFilter {
	private String elementName;
	private String namespace;

	public PacketExtensionFilter(String elementName, String namespace) {
		this.elementName = elementName;
		this.namespace = namespace;
	}

	public PacketExtensionFilter(String namespace) {
		this(null, namespace);
	}

	public boolean accept(Packet packet) {
		return packet.getExtension(this.elementName, this.namespace) != null;
	}
}