package com.loovee.common.xmpp.packet;


public class ElementIQ extends IQ {
	private Query query;
	
	
	public ElementIQ(Query query) {
		this.query = query;
	}
	

	@Override
	public String getChildElementXML() {
		return query == null ? "":query.toXML();
	}

}
