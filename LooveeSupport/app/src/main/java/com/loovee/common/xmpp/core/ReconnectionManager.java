package com.loovee.common.xmpp.core;


import com.loovee.common.app.LooveeApplication;
import com.loovee.common.util.LogUtils;
import com.loovee.common.util.NetUtil;
import com.loovee.common.xmpp.packet.StreamError;

public class ReconnectionManager implements ConnectionListener {
	private static  String THREAD_NAME = "XMPP Reconnection Manager";
	private int secondBetweenReconnection = 300;
	private Thread reconnectionThread;
	private XMPPConnection connection;
	boolean done = false;

	ReconnectionManager(XMPPConnection connection) {
		this.connection = connection;
	}

	/**
	 * 是否允许重连
	 * @return true允许重新连接，否则不能。
	 */
	private boolean isReconnectionAllowed() {
		return (!this.done) && (!this.connection.isConnected())
				&& (this.connection.getConfiguration().isReconnectionAllowed())
				&& (this.connection.packetReader != null
				&& NetUtil.isNetworkAvailable(LooveeApplication.getLocalLoovee()));
	}

	private int getSecondBetweenReconnection() {
		return this.secondBetweenReconnection;
	}

	protected void setSecondBetweenReconnection(int secondBetweenReconnection) {
		this.secondBetweenReconnection = secondBetweenReconnection;
	}

	/**
	 * 开始重新连接XMPP服务器
	 */
	protected void reconnect() {
		if (isReconnectionAllowed()) {
			this.reconnectionThread = new Thread() {

				private int attempts = 0;
				private int firstReconnectionPeriod = 7;
				private int secondReconnectionPeriod = 10 + this.firstReconnectionPeriod;
				private int firstReconnectionTime = 10;
				private int secondReconnectionTime = 60;
				private int lastReconnectionTime = ReconnectionManager.this
						.getSecondBetweenReconnection();

				private int remainingSeconds = 0;
				private int notificationPeriod = 1000;

				private int timeDelay() {
					if (this.attempts > this.secondReconnectionPeriod) {
						return this.lastReconnectionTime;
					}
					if (this.attempts > this.firstReconnectionPeriod) {
						return this.secondReconnectionTime;
					}
					return this.firstReconnectionTime;
				}

				public void run() {
					while (ReconnectionManager.this.isReconnectionAllowed()) {
						LogUtils.jLog().e("xmpp正在重连.....");
						this.remainingSeconds = timeDelay();

						while ((ReconnectionManager.this
								.isReconnectionAllowed())
								&& (this.remainingSeconds > 0)) {
							try {
								Thread.sleep(this.notificationPeriod);
								this.remainingSeconds -= 1;
								ReconnectionManager.this
										.notifyAttemptToReconnectIn(this.remainingSeconds);
							} catch (InterruptedException e1) {
								e1.printStackTrace();
								ReconnectionManager.this
										.notifyReconnectionFailed(e1);
							}

						}

						try {
							if (ReconnectionManager.this
									.isReconnectionAllowed()
									&& !ReconnectionManager.this.connection.isConnected()) {
								
								ReconnectionManager.this.connection.connect();
								if(!ReconnectionManager.this.connection.isAuthenticated()) {
									ReconnectionManager.this.connection.login();
								}
								LogUtils.jLog().e("xmpp重连成功.....");
							}
						} catch (XMPPException e) {
							ReconnectionManager.this
									.notifyReconnectionFailed(e);
						}
					}
				}
			};
			this.reconnectionThread.setName(THREAD_NAME);
			this.reconnectionThread.setDaemon(true);
			this.reconnectionThread.start();
		}
	}


	/**
	 * 通知重连失败
	 * @param exception 错误异常
	 */
	protected void notifyReconnectionFailed(Exception exception) {
		if (isReconnectionAllowed())
			for (ConnectionListener listener : this.connection.packetReader.connectionListeners)
				listener.reconnectionFailed(exception);
	}

	protected void notifyAttemptToReconnectIn(int seconds) {
		if (isReconnectionAllowed())
			for (ConnectionListener listener : this.connection.packetReader.connectionListeners)
				listener.reconnectingIn(seconds);
	}

	@Override
	public void connectionClosed() {
		this.done = true;
	}

	
	@Override
	public void connectionClosedOnError(Exception e) {
		this.done = false;
		if ((e instanceof XMPPException)) {
			XMPPException xmppEx = (XMPPException) e;
			StreamError error = xmppEx.getStreamError();

			if (error != null) {
				String reason = error.getCode();

				if ("conflict".equals(reason)) {
					return;
				}
			}
		}
		
		if (isReconnectionAllowed())
			reconnect();
	}

	@Override
	public void reconnectingIn(int seconds) {
	}

	@Override
	public void reconnectionFailed(Exception e) {
	}

	@Override
	public void reconnectionSuccessful() {
	}


}