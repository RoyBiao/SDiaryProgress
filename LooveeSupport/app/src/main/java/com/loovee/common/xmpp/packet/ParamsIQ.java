package com.loovee.common.xmpp.packet;

import com.loovee.common.xmpp.utils.PacketParserUtils;

/**
 * 用作IQ的请求参数
 * @author Jesse
 *
 */
public class ParamsIQ extends IQ {
	private Object params = null;
	
	
	public ParamsIQ(Object params) {
		this.params = params;
	}
	

	@Override
	public String getChildElementXML() {
		if(params == null) return null;
		
		StringBuffer bufferString = new StringBuffer();
		bufferString.append(PacketParserUtils.javaBeanToXML(params));
		return bufferString.toString();
	}


	/**
	 * 设置请求参数
	 * @param params
	 */
	public void setParams(Object params) {
		this.params = params;
	}

}
