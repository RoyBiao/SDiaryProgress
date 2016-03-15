package com.loovee.common.xmpp.filter;

import com.loovee.common.xmpp.packet.Packet;

/**
 * 数据包过滤器
 * @author Jesse
 */
public interface PacketFilter {
	 public abstract boolean accept(Packet packet);
}
