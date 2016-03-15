package com.loovee.common.xmpp.provider;

import com.loovee.common.xmpp.packet.PacketExtension;

import org.xmlpull.v1.XmlPullParser;

public interface PacketExtensionProvider {
	public PacketExtension parseExtension(
			XmlPullParser paramXmlPullParser) throws Exception;
}
