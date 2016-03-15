package com.loovee.common.xmpp.core;

import com.loovee.common.xmpp.packet.Packet;


/**
 * 数据包拦截器
 * @author Jesse
 *
 */
public interface PacketInterceptor {
	public abstract void interceptPacket(Packet packet);
}
