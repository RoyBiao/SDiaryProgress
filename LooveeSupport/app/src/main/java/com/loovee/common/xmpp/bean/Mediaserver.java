package com.loovee.common.xmpp.bean;

import com.loovee.common.xmpp.annotation.XMLAttr;

public class Mediaserver {
	@XMLAttr
	private String ip; 
	@XMLAttr
	private int port;
	
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
}
