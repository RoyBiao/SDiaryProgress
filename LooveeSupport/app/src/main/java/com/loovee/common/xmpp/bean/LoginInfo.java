package com.loovee.common.xmpp.bean;

public class LoginInfo {
	private String name;
	private String password;
	private String resource;
	private boolean sendPresence;
	private String type;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getResource() {
		return resource;
	}
	public void setResource(String resource) {
		this.resource = resource;
	}
	public boolean isSendPresence() {
		return sendPresence;
	}
	public void setSendPresence(boolean sendPresence) {
		this.sendPresence = sendPresence;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}

}
