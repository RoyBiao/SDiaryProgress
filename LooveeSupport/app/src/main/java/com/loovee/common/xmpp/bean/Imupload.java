package com.loovee.common.xmpp.bean;

import com.loovee.common.xmpp.annotation.XMLElement;

@XMLElement
public class Imupload {
	@XMLElement
	private String url;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
