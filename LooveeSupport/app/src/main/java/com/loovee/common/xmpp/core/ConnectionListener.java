package com.loovee.common.xmpp.core;

public interface ConnectionListener {
	/**
	 * XMPP连接关闭
	 */
	public abstract void connectionClosed();

	/**
	 * 连接关闭
	 * @param exception 异常
	 */
	public abstract void connectionClosedOnError(Exception exception);

	
	public abstract void reconnectingIn(int paramInt);

	/**
	 * 重连成功
	 */
	public abstract void reconnectionSuccessful();

	/**
	 * 重连失败
	 * @param paramException
	 */
	public abstract void reconnectionFailed(Exception paramException);
}
