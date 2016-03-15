package com.loovee.common.xmpp.packet;

import java.util.ArrayList;
import java.util.List;

public class Query {
	private String xmlns;
	private List<Element> elements = new ArrayList<Element>();

	
	public Query(String xmlns) {
		this.xmlns = xmlns;
	}

	public String getXmlns() {
		return xmlns;
	}
	
	/**
	 * 添加一个节点
	 * @param element
	 */
	public void addElement(Element element) {
		this.elements.add(element);
	}
	
	/**
	 * 添加多个节点
	 * @param elements
	 */
	public void addElements(List<Element> elements) {
		this.elements.addAll(elements);
	}

	/**
	 * 转换为xml字符串
	 * @return
	 */
	public String toXML() {
		StringBuffer bufferString = new StringBuffer();
		bufferString.append("<query ");
		bufferString.append(" xmlns="+"\""+xmlns+"\"");
		if(elements.isEmpty()) {
			bufferString.append("/>");
		}else {
			bufferString.append(">");
			for(Element e:elements) {
				bufferString.append(e.toXML());
			}
			bufferString.append("</query>");
		}
		return bufferString.toString();
	}

}
