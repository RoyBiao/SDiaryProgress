package com.loovee.common.xmpp.core;

public interface XMPPStore {
	
	/**
	 * 清除用户数据
	 */
	public void clear();
	
	
	/**
	 * 添加用户数据
	 * @param user
	 */
	public void addUser(String user);
	
	/**
	 * 添加登录信息
	 * @param info
	 */
	public void addLoginInf(String info);
	
	/**
	 * 获取登录信息
	 * @return
	 */
	public String getLoginInfo();
	
	
	/**
	 * 获取用户信息
	 * @return
	 */
	public String getUser();
	

}
