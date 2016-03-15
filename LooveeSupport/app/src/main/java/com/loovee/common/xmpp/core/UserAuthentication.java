package com.loovee.common.xmpp.core;


public interface UserAuthentication {
	
	/**
	 * 登录方式
	 * @author Jesse
	 *
	 */
	public enum LoginType {
		uid("uid"),tel("tel");
		
		private String value;
		private LoginType(String value) {
			this.value = value;
		}
		
		public String value() {
			return this.value;
		}
		public static LoginType ValueOf(String type) {
			if(type.equals("uid")) {
				return LoginType.uid;
			}else if(type.equals("tel")) {
				return LoginType.tel;
			}
			return null;
		}
	}
	
	
	/**
	 * 授权用户
	 * @param username
	 * @param password
	 * @param resource
	 * @return
	 * @throws XMPPException
	 */
	public abstract User authenticate(String username, String password, String resource, LoginType type)
		    throws XMPPException;
	
	public void authenticated(User user);
	
}
