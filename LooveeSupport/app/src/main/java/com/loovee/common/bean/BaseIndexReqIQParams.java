package com.loovee.common.bean;

import com.loovee.common.xmpp.annotation.XMLElement;

@XMLElement("query")
public class BaseIndexReqIQParams extends BaseReqIQParams{
	@XMLElement
	private Index index;

	public Index getIndex() {
		return index;
	}

	public void setIndex(Index index) {
		this.index = index;
	}
}
