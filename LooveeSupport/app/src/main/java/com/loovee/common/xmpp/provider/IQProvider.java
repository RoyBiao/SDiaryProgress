package com.loovee.common.xmpp.provider;

import com.loovee.common.xmpp.packet.IQ;

import org.xmlpull.v1.XmlPullParser;

public interface IQProvider {
	
	/**
	 * 将XML解析成一个IQ类型
	 * @param paramXmlPullParser xml解析器
	 * @param clazzName javaBean类名
	 * @return
	 * @throws Exception
	 */
	public IQ parseIQ(XmlPullParser paramXmlPullParser, String clazzName)
			throws Exception;
}
