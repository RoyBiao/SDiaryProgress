package com.loovee.common.constant;


import com.loovee.common.jni.ConstantJNI;

public class Constant {

	static {
		System.loadLibrary("ConstantJNI");
	}

	public static final String ACTION_SHOW_CHAT_REFRESH_MESSAGE = "com.loovee.common.module.main.fragment.Multiterm";
	public static final String ACTION_SHOW_CHATMAIN_REFRESH_MESSAGE = "com.loovee.common.module.main.chat.reffreshMessage";
	public static final String ACTION_SHOW_CHAT_MESSAGE = "com.loovee.common.module.chat.showMessage";
	public static final String ACTION_SHOW_CHAT_SINGLE_MESSAGE = "com.loovee.common.module.chat.singleMessage";
	public static final String ACTION_SHOW_CHAT_TOAST_MESSAGE = "com.loovee.common.module.chat.toastMessage";
	public static final String ACTION_SHOW_CHAT_UPDATE_MESSAGE = "com.loovee.common.module.chat.updateMessage";
	public static final String ACTION_SHOW_CHAT_READED_MESSAGE = "com.loovee.common.module.chat.msgreadedMessage";
	/*
	 * 语音提示
	 */
	public static final String ACTION_VOICE_REMININD_MESSAGE = "com.loovee.common.module.server.voicereminind";
	

	/**
	 * 得到Des的秘钥
	 * 
	 * @return 返回秘钥
	 */
	public static String getEncryptKey() {
		return new ConstantJNI().getEncryptKeyFromJNI();
	}

}
