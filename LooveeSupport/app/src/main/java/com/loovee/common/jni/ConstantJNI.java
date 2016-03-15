package com.loovee.common.jni;

public class ConstantJNI {
	
	
	static {
		System.loadLibrary("ConstantJNI");
	}
	
	/**
	 * 获取DES的秘钥
	 * @return
	 */
	public native String getEncryptKeyFromJNI();

}
