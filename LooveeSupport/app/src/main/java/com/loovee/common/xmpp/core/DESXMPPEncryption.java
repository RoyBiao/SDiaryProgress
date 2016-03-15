package com.loovee.common.xmpp.core;

import com.loovee.common.constant.Constant;
import com.loovee.common.xmpp.security.DES;

public class DESXMPPEncryption implements XMPPEncryption {

	@Override
	public String encryption(String content) {
		
		try {
			return DES.encryptDES(content, Constant.getEncryptKey());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
