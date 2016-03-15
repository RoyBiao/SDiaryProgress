package com.loovee.common.xmpp.bean;

import com.loovee.common.xmpp.annotation.XMLAttr;

public class Unioncharge {
	@XMLAttr
	private String url;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
