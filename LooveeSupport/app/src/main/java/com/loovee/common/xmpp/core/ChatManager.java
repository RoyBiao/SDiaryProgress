package com.loovee.common.xmpp.core;

import com.loovee.common.xmpp.filter.AndFilter;
import com.loovee.common.xmpp.filter.FromContainsFilter;
import com.loovee.common.xmpp.filter.PacketFilter;
import com.loovee.common.xmpp.filter.ThreadFilter;
import com.loovee.common.xmpp.packet.Message;
import com.loovee.common.xmpp.packet.Packet;
import com.loovee.common.xmpp.utils.StringUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

public class ChatManager {
	private static String prefix = StringUtils.randomString(5);

	private static long id = 0L;

	private Map<String, Chat> threadChats = new ConcurrentHashMap<String, Chat>();

	private Map<String, Chat> jidChats = new ConcurrentHashMap<String, Chat>();

	private Set<ChatManagerListener> chatManagerListeners = new CopyOnWriteArraySet<ChatManagerListener>();

	private Map<PacketInterceptor, PacketFilter> interceptors = new WeakHashMap<PacketInterceptor, PacketFilter>();

	private XMPPConnection connection;

	private static synchronized String nextID() {
		return prefix + Long.toString(id++);
	}

	ChatManager(XMPPConnection connection) {
		this.connection = connection;

		PacketFilter filter = new PacketFilter() {
			public boolean accept(Packet packet) {
				if (!(packet instanceof Message)) {
					return false;
				}
				Message.Type messageType = ((Message) packet).getType();
				return (messageType != Message.Type.groupchat)
						&& (messageType != Message.Type.headline);
			}
		};
		connection.addPacketListener(new PacketListener() {
			public void processPacket(Packet packet) {
				Message message = (Message) packet;
				Chat chat;
				if (message.getThread() == null) {
					chat = ChatManager.this.getUserChat(StringUtils
							.parseBareAddress(message.getFrom()));
				} else {
					chat = ChatManager.this.getThreadChat(message.getThread());
					if (chat == null) {
						chat = ChatManager.this.getUserChat(StringUtils
								.parseBareAddress(message.getFrom()));
					}
				}

				if (chat == null) {
					chat = ChatManager.this.createChat(message);
				}
				ChatManager.this.deliverMessage(chat, message);
			}
		}, filter);
	}

	public Chat createChat(String userJID, MessageListener listener) {
		String threadID;
		do
			threadID = nextID();
		while (this.threadChats.get(threadID) != null);

		return createChat(userJID, threadID, listener);
	}

	public Chat createChat(String userJID, String thread,
			MessageListener listener) {
		if (thread == null) {
			thread = nextID();
		}
		Chat chat = (Chat) this.threadChats.get(thread);
		if (chat != null) {
			throw new IllegalArgumentException("ThreadID is already used");
		}
		chat = createChat(userJID, thread, true);
		chat.addMessageListener(listener);
		return chat;
	}

	private Chat createChat(String userJID, String threadID,
			boolean createdLocally) {
		Chat chat = new Chat(this, userJID, threadID);
		this.threadChats.put(threadID, chat);
		this.jidChats.put(userJID, chat);

		for (ChatManagerListener listener : this.chatManagerListeners) {
			listener.chatCreated(chat, createdLocally);
		}
		return chat;
	}

	private Chat createChat(Message message) {
		String threadID = message.getThread();
		if (threadID == null) {
			threadID = nextID();
		}
		String userJID = message.getFrom();

		return createChat(userJID, threadID, false);
	}

	private Chat getUserChat(String userJID) {
		return (Chat) this.jidChats.get(userJID);
	}

	public Chat getThreadChat(String thread) {
		return (Chat) this.threadChats.get(thread);
	}

	public void addChatListener(ChatManagerListener listener) {
		this.chatManagerListeners.add(listener);
	}

	public void removeChatListener(ChatManagerListener listener) {
		this.chatManagerListeners.remove(listener);
	}

	public Collection<ChatManagerListener> getChatListeners() {
		return Collections.unmodifiableCollection(this.chatManagerListeners);
	}

	private void deliverMessage(Chat chat, Message message) {
		chat.deliver(message);
	}

	void sendMessage(Chat chat, Message message) {
		for (Map.Entry<PacketInterceptor, PacketFilter> interceptor : this.interceptors
				.entrySet()) {
			PacketFilter filter = (PacketFilter) interceptor.getValue();
			if ((filter != null) && (filter.accept(message))) {
				((PacketInterceptor) interceptor.getKey())
						.interceptPacket(message);
			}
		}

		if (message.getFrom() == null) {
			message.setFrom(XMPPConnection.getUser().getJid());
		}
		this.connection.sendPacket(message);
	}

	PacketCollector createPacketCollector(Chat chat) {
		return this.connection.createPacketCollector(new AndFilter(
				new PacketFilter[] { new ThreadFilter(chat.getThreadID()),
						new FromContainsFilter(chat.getParticipant()) }));
	}

	public void addOutgoingMessageInterceptor(
			PacketInterceptor packetInterceptor) {
		addOutgoingMessageInterceptor(packetInterceptor, null);
	}

	public void addOutgoingMessageInterceptor(
			PacketInterceptor packetInterceptor, PacketFilter filter) {
		if (packetInterceptor != null)
			this.interceptors.put(packetInterceptor, filter);
	}
}
