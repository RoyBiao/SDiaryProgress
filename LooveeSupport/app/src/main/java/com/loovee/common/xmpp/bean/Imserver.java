package com.loovee.common.xmpp.bean;

import com.loovee.common.xmpp.annotation.XMLAttr;
import com.loovee.common.xmpp.annotation.XMLElement;

@XMLElement("imserver")
public class Imserver {
	@XMLAttr
	private String ip;
	@XMLAttr
	private String domain;
	@XMLAttr
	private int httpport;
	@XMLAttr
	private int port;
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getDomain() {
		return domain;
	}
	public void setDomain(String domain) {
		this.domain = domain;
	}
	public int getHttpport() {
		return httpport;
	}
	public void setHttpport(int httpport) {
		this.httpport = httpport;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
}
