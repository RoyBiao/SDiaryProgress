package com.loovee.common.bean;

import com.loovee.common.xmpp.annotation.XMLAttr;
import com.loovee.common.xmpp.annotation.XMLElement;

@XMLElement("query")
public class BaseReqIQParams {
	@XMLAttr
	public String xmlns;

	public String getXmlns() {
		return xmlns;
	}

	public void setXmlns(String xmlns) {
		this.xmlns = xmlns;
	}
}
