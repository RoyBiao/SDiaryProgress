package com.loovee.common.xmpp.core;

import com.loovee.common.xmpp.packet.Message;


public interface MessageListener {
	
	public abstract void processMessage(Chat paramChat, Message message);
}
