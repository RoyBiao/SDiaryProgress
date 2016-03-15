package com.loovee.common.xmpp.core;

import com.loovee.common.xmpp.filter.PacketFilter;
import com.loovee.common.xmpp.packet.IQ;
import com.loovee.common.xmpp.packet.IQ.Type;
import com.loovee.common.xmpp.packet.Packet;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

public class PacketWriter {
	private Writer writer;
	private XMPPConnection connection;
	private final BlockingQueue<Packet> queue;
	private boolean done;
	private Thread writerThread;
	private Thread keepAliveThread;
	private long lastActive = System.currentTimeMillis();
	private final Map<PacketInterceptor, InterceptorWrapper> interceptors = new ConcurrentHashMap<PacketInterceptor, InterceptorWrapper>();

	public PacketWriter(XMPPConnection xmppConnection) {
		this.connection = xmppConnection;
		queue = new ArrayBlockingQueue<Packet>(500, true);
		init();
	}

	/**
	 * 初始化
	 */
	protected void init() {
		this.writer = this.connection.writer;
		this.done = false;
		this.writerThread = new Thread() {
			@Override
			public void run() {
				super.run();
				PacketWriter.this.writePackets(this);
			}
		};
		this.writerThread.setDaemon(true);
		this.writerThread.setName("Packet Writer Thread ( "
				+ this.connection.connectionCounterValue + " )");
	}

	/**
	 * 启动
	 */
	public void startup() {
		this.writerThread.start();
	}

	/**
	 * 启动保持在线线程
	 */
	public void startKeepAliveProcess() {
		if (XMPPConfig.getKeepAliveInterval() > 0) {
			KeepAliveTask task = new KeepAliveTask(XMPPConfig.getKeepAliveInterval());
			this.keepAliveThread = new Thread(task);
			task.setThread(this.keepAliveThread);
			this.keepAliveThread.setDaemon(true);
			this.keepAliveThread.setName("Keep Alive Thread("
					+ this.connection.connectionCounterValue + ")");
			this.keepAliveThread.start();
		}
	}

	/**
	 * 关闭
	 */
	public void shutdown() {
		this.done = true;
		synchronized (this.queue) {
			this.queue.notifyAll();
		}
	}

	private void writePackets(Thread thisThread) {
		try {
			openStream();

			while ((!this.done) && (this.writerThread == thisThread)) {
				Packet packet = nextPacket();
				if (packet != null) {
					synchronized (this.writer) {
						this.writer.write(packet.toXML());
						this.writer.flush();
						this.lastActive = System.currentTimeMillis();
					}
				}

			}

			try {
				synchronized (this.writer) {
					while (!this.queue.isEmpty()) {
						Packet packet = (Packet) this.queue.remove();
						this.writer.write(packet.toXML());
					}
					this.writer.flush();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			this.queue.clear();
			try {
				this.writer.write("</stream:stream>");
				this.writer.flush();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					this.writer.close(); //关闭流
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			if (!this.done) {
				this.done = true;
				e.printStackTrace();
				this.connection.packetReader.notifyConnectionError(e); //通知错误
			}

		}
	}

	/**
	 * 向XMPP发出打开流的命令
	 * 
	 * @throws IOException
	 */
	void openStream() throws IOException {

		StringBuilder stream = new StringBuilder();
		stream.append("<stream:stream");
		stream.append(" xmlns=\"jabber:client\"");
		stream.append(" xmlns:stream=\"http://etherx.jabber.org/streams\"");
		stream.append(" to=\"").append(this.connection.serviceName)
				.append("\"");
		stream.append(" version=\"1.0\"");
		stream.append(" deviceid=\"")
				.append(this.connection.getDeviceInfo().getDeviceId())
				.append("\"");
		stream.append(" timestamp=\"").append(System.currentTimeMillis() + "")
				.append("\">");
		this.writer.write(stream.toString());
		this.writer.flush();
	}

	/**
	 * 获取下一个数据包，如果没有，则阻塞直到有数据为止
	 * 
	 * @return
	 */
	private Packet nextPacket() {
		Packet packet = null;
		while ((!this.done) && ((packet = (Packet) this.queue.poll()) == null)) {
			try {
				synchronized (this.queue) {
					this.queue.wait();
				}
			} catch (InterruptedException ie) {
				ie.printStackTrace();
			}
		}
		return packet;
	}

	/**
	 * 发送数据包
	 * 
	 * @param packet
	 *            数据包
	 */
	public void sendPacket(Packet packet) {
		if (!this.done) {
			processInterceptors(packet);
			try {
				this.queue.put(packet);
			} catch (InterruptedException ie) {
				ie.printStackTrace();
				return;
			}
			synchronized (this.queue) {
				this.queue.notifyAll();
			}

		}
	}

	/**
	 * 处理拦截器
	 * 
	 * @param packet
	 */
	private void processInterceptors(Packet packet) {
		if (packet != null)
			for (InterceptorWrapper interceptorWrapper : this.interceptors
					.values())
				interceptorWrapper.notifyListener(packet);
	}

	
	/**
	 * 添加数据包拦截器
	 * @param packetInterceptor
	 * @param packetFilter
	 */
	public void addPacketInterceptor(PacketInterceptor packetInterceptor,
			PacketFilter packetFilter) {
		this.interceptors.put(packetInterceptor, new InterceptorWrapper(
				packetInterceptor, packetFilter));
	}

	
	/**
	 * 删除数据包拦截器
	 * @param packetInterceptor
	 */
	public void removePacketInterceptor(PacketInterceptor packetInterceptor) {
		this.interceptors.remove(packetInterceptor);
	}

	/**
	 * 连接器包装者
	 * 
	 * @author Administrator
	 * 
	 */
	private static class InterceptorWrapper {
		private PacketInterceptor packetInterceptor;
		private PacketFilter packetFilter;

		public InterceptorWrapper(PacketInterceptor packetInterceptor,
				PacketFilter packetFilter) {
			this.packetInterceptor = packetInterceptor;
			this.packetFilter = packetFilter;
		}

		public boolean equals(Object object) {
			if (object == null) {
				return false;
			}
			if ((object instanceof InterceptorWrapper)) {
				return ((InterceptorWrapper) object).packetInterceptor
						.equals(this.packetInterceptor);
			}

			if ((object instanceof PacketInterceptor)) {
				return object.equals(this.packetInterceptor);
			}
			return false;
		}

		public void notifyListener(Packet packet) {
			if ((this.packetFilter == null)
					|| (this.packetFilter.accept(packet)))
				this.packetInterceptor.interceptPacket(packet);
		}
	}

	private class KeepAliveTask implements Runnable {
		private int delay;
		private Thread thread;

		public KeepAliveTask(int delay) {
			this.delay = delay;
		}

		protected void setThread(Thread thread) {
			this.thread = thread;
		}

		public void run() {
			try {
				Thread.sleep(15000L);
			} catch (InterruptedException ie) {
			}
			while ((!PacketWriter.this.done)
					&& (PacketWriter.this.keepAliveThread == this.thread)) {
				synchronized (PacketWriter.this.writer) {
					if (System.currentTimeMillis()
							- PacketWriter.this.lastActive >= this.delay
							&& connection.isAuthenticated()) { //必须是已经登录才发送ping包
						try {
							sendPingPacket();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
				try {
					Thread.sleep(this.delay);
				} catch (InterruptedException ie) {
					ie.printStackTrace();
				}
			}
		}


	}
	
	/**
	 * 发送心跳包
	 */
	private void sendPingPacket() {
		IQ heartbeatIQ = new IQ() {
			
			@Override
			public String getChildElementXML() {
				return "<ping xmlns=\"urn:xmpp:ping\"/>";
			}
		};
		heartbeatIQ.setType(Type.SET);
		heartbeatIQ.setTo(this.connection.serviceName);
		this.connection.sendPacket(heartbeatIQ);
	}

	
	void cleanup() {
		this.interceptors.clear();
	}

}
