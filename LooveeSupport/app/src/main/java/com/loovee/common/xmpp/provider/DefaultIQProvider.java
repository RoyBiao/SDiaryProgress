package com.loovee.common.xmpp.provider;

import com.loovee.common.xmpp.packet.DefaultIQ;
import com.loovee.common.xmpp.packet.IQ;
import com.loovee.common.xmpp.utils.PacketParserUtils;

import org.xmlpull.v1.XmlPullParser;

/**
 * 通用的IQProvider，适用于绝大多数情况下的XML解析
 * @author Jesse
 *
 */
public class DefaultIQProvider implements IQProvider {

	@Override
	public IQ parseIQ(XmlPullParser parser,String className) throws Exception {
		DefaultIQ<Object> iq = new DefaultIQ<Object>();
		Object data = PacketParserUtils.xmlToJavaBean(parser, Class.forName(className));
		iq.setData(data);
		return iq;
	}

}
