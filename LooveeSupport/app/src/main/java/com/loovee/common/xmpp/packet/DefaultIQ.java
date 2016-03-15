package com.loovee.common.xmpp.packet;


public class DefaultIQ<T> extends IQ {
	
	private T data = null;

	@Override
	public String getChildElementXML() {
		return null;
	}

	/**
	 * 获取到数据
	 * @return
	 */
	public T getData() {
		return data;
	}

	/**
	 * 设置数据
	 * @param data
	 */
	public void setData(T data) {
		this.data = data;
	}
	
	

}
