package com.loovee.common.xmpp.core;

/**
 * 连接创建监听者
 * @author Jesse
 *
 */
public abstract interface ConnectionCreationListener {
	/**
	 *	连接已经创建
	 * @param connection
	 */
	public abstract void connectionCreated(XMPPConnection connection);
}
