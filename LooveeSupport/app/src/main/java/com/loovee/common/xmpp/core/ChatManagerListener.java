package com.loovee.common.xmpp.core;

public interface ChatManagerListener {
	public abstract void chatCreated(Chat chat, boolean createdLocally);
}
