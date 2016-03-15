package com.loovee.common.xmpp.core;

import com.loovee.common.xmpp.packet.Body;
import com.loovee.common.xmpp.packet.Message;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public class Chat {
	private ChatManager chatManager;
	private String threadID;
	private String participant;
	private final Set<MessageListener> listeners = new CopyOnWriteArraySet<MessageListener>();

	Chat(ChatManager chatManager, String participant, String threadID) {
		this.chatManager = chatManager;
		this.participant = participant;
		this.threadID = threadID;
	}

	public String getThreadID() {
		return this.threadID;
	}

	public String getParticipant() {
		return this.participant;
	}

	public void sendMessage(String text) throws XMPPException {
		Message message = new Message(this.participant, Message.Type.chat);
		message.setThread(this.threadID);
		message.setBody(new Body(text));
		this.chatManager.sendMessage(this, message);
	}

	public void sendMessage(Message message) throws XMPPException {
		message.setTo(this.participant);
		message.setType(message.getType());
		message.setThread(this.threadID);
		this.chatManager.sendMessage(this, message);
	}

	public void addMessageListener(MessageListener listener) {
		if (listener == null) {
			return;
		}
		this.listeners.add(listener);
	}

	public void removeMessageListener(MessageListener listener) {
		this.listeners.remove(listener);
	}

	public Collection<MessageListener> getListeners() {
		return Collections.unmodifiableCollection(this.listeners);
	}

	public PacketCollector createCollector() {
		return this.chatManager.createPacketCollector(this);
	}

	/**
	 * 分发消息
	 * @param message 接收到的消息
	 */
	void deliver(Message message) {
		message.setThread(this.threadID);
		for (MessageListener listener : this.listeners) {
			listener.processMessage(this, message);
		}
			
	}

	@Override
	public boolean equals(Object obj) {
		return ((obj instanceof Chat))
				&& (this.threadID.equals(((Chat) obj).getThreadID()))
				&& (this.participant.equals(((Chat) obj).getParticipant()));
	}
}
