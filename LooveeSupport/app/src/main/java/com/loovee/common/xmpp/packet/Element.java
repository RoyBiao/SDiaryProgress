package com.loovee.common.xmpp.packet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Element {
	private String tagName;
	private Map<String,String> atts = new HashMap<String,String>();
	private String content = null;
	private List<Element> childElements = new ArrayList<Element>();
	
	public Element(String tagName) {
		this.tagName = tagName;
	}
	
	public Element(String tagName,String content) {
		this.tagName = tagName;
		this.content = content;
	}
	
	public void addAtt(String key,String value) {
		atts.put(key, value);
	}

	public void setContent(String content) {
		this.content = content;
	}

	public void addChildElement(Element childElement) {
		childElements.add(childElement);
	}
	
	public void addChildElements(List<Element> childElements) {
		this.childElements.addAll(childElements);
	}
	
	
	public String toXML() {
		StringBuffer stringBuffer = new StringBuffer();

			stringBuffer.append("<"+tagName);
			for(Entry<String, String> entry:atts.entrySet()) {
				stringBuffer.append(" "+entry.getKey());
				stringBuffer.append("=");
				stringBuffer.append("\""+entry.getValue()+"\"");
			}
			if(content == null && childElements.isEmpty()) {
				stringBuffer.append(" />");
			}else if(content !=null && childElements.isEmpty()){
				stringBuffer.append(" >");
				stringBuffer.append(content);
				stringBuffer.append("</"+tagName+">");
			}else if(content == null && !childElements.isEmpty()) {
				stringBuffer.append(" >");
				for(Element e:childElements) {
					stringBuffer.append(e.toXML());
				}
				stringBuffer.append("</"+tagName+">");
			}else {
				throw new RuntimeException("不能转换为xml字符串");
			}
		return stringBuffer.toString();
	}
	

}
