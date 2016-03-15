package com.loovee.common.bean;

import com.loovee.common.xmpp.annotation.XMLAttr;
import com.loovee.common.xmpp.annotation.XMLElement;

@XMLElement
public class Index {
	@XMLAttr
	private int start;
	@XMLAttr
	private int end;
	
	public Index() {
	}
	
	public Index(int start, int end) {
		this.start = start;
		this.end = end;
	}
	
	public int getStart() {
		return start;
	}
	public void setStart(int start) {
		this.start = start;
	}
	public int getEnd() {
		return end;
	}
	public void setEnd(int end) {
		this.end = end;
	}

}
