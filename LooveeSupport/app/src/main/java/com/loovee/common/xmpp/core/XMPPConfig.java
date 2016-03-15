package com.loovee.common.xmpp.core;

/**
 * XMPP的相关配置信息
 * 
 * @author Jesse
 * 
 */
public class XMPPConfig {

	private static int packetReplyTimeout = 500;
	private static int keepAliveInterval = 60*1000;//一分钟
	private static int loginTimeout = 30*1000; //登录超时30秒

	/**
	 * 连接相关的配置
	 */
	private ConnectionConfiguration connectionConfiguration;

	// 用户名
	private String username;
	// 密码
	private String password;
	// 资源名
	private String resource;


	public XMPPConfig(String host, int port) {
		this.connectionConfiguration = new ConnectionConfiguration(host, port);
	}

	public static int getPacketReplyTimeout() {
		if (packetReplyTimeout <= 0) {
			packetReplyTimeout = 000;
		}
		return packetReplyTimeout;
	}

	public static void setPacketReplyTimeout(int timeout) {
		if (timeout <= 0) {
			throw new IllegalArgumentException();
		}
		packetReplyTimeout = timeout;
	}

	public static int getKeepAliveInterval() {
		return keepAliveInterval;
	}

	public static void setKeepAliveInterval(int interval) {
		keepAliveInterval = interval;
	}

	/**
	 * 设定用户名
	 * 
	 * @param name
	 *            用户名
	 * @author alonso lee
	 */
	public void setUsername(String name) {
		this.username = name;
	}

	/**
	 * 获取用户名
	 * 
	 * @return 用户名
	 * @author alonso lee
	 */
	public String getUsername() {
		return this.username;
	}

	/**
	 * 设定密码
	 * 
	 * @param pwd
	 *            密码
	 * @author alonso lee
	 */
	public void setPassword(String pwd) {
		this.password = pwd;
	}

	/**
	 * 获取密码
	 * 
	 * @return 密码
	 * @author alonso lee
	 */
	public String getPassword() {
		return this.password;
	}



	
	/**
	 * 设定资源名
	 * 
	 * @param resource
	 *            资源名
	 * @author alonso lee
	 */
	public void setResource(String resource) {
		this.resource = resource;
	}

	/**
	 * 获取资源名
	 * 
	 * @return 资源名
	 * @author alonso lee
	 */
	public String getResource() {
		return this.resource;
	}


	

	public ConnectionConfiguration getConnectionConfiguration() {
		return connectionConfiguration;
	}

	public void setConnectionConfiguration(
			ConnectionConfiguration connectionConfiguration) {
		this.connectionConfiguration = connectionConfiguration;
	}

	/**
	 * 获取登录超时的时间
	 * @return
	 */
	public static int getLoginTimeout() {
		return loginTimeout;
	}
}
