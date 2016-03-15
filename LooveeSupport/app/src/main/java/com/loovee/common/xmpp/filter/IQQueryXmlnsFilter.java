package com.loovee.common.xmpp.filter;

import com.loovee.common.xmpp.packet.IQ;
import com.loovee.common.xmpp.packet.Packet;

/**
 * 
 * @author Jesse
 *
 */
public class IQQueryXmlnsFilter implements PacketFilter {
	private String queryXmlns;

	public IQQueryXmlnsFilter(String queryXmlns) {
		this.queryXmlns = queryXmlns;
	}

	public boolean accept(Packet packet) {
		return ((packet instanceof IQ))
				&& (((IQ) packet).getQueryXmlns() != null)
				&& (((IQ) packet).getQueryXmlns().equals(this.queryXmlns));
	}
}
