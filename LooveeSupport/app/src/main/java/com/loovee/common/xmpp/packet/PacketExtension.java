package com.loovee.common.xmpp.packet;

public  interface PacketExtension {
	
	/**
	 * 获取元素名称
	 * @return
	 */
	public  String getElementName();

	/**
	 * 获取XML命名空间
	 * @return
	 */
	public  String getNamespace();

	/**
	 * 把当前的数据转换为XML字符串
	 * @return
	 */
	public  String toXML();
}
