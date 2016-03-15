package com.loovee.common.xmpp.bean;

import com.loovee.common.xmpp.annotation.XMLAttr;
import com.loovee.common.xmpp.annotation.XMLElement;

@XMLElement("pocserver")
public class Pocserver {
	@XMLAttr
	private int nodeid;
	@XMLAttr
	private int ctrlport;
	@XMLAttr
	private int audioport;
	@XMLAttr
	private String ip;
	
	public int getNodeid() {
		return nodeid;
	}
	public void setNodeid(int nodeid) {
		this.nodeid = nodeid;
	}
	public int getCtrlport() {
		return ctrlport;
	}
	public void setCtrlport(int ctrlport) {
		this.ctrlport = ctrlport;
	}
	public int getAudioport() {
		return audioport;
	}
	public void setAudioport(int audioport) {
		this.audioport = audioport;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
}
