package com.loovee.common.xmpp.core;

public interface LoginListener {
	
	/**
	 * 登录成功
	 */
	public void onLoginSuccess(User user);
	
	/**
	 * 登录失败
	 */
	public void OnLoginFailed();
}
