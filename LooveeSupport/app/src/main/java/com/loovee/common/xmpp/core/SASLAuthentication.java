package com.loovee.common.xmpp.core;


import com.loovee.common.util.LogUtils;

import java.io.IOException;

public class SASLAuthentication implements UserAuthentication {
	private XMPPConnection connection;
	private BaseAuthentExt ext;
	
	private User user;
	
	/**
	 * 服务器是否返回了数据
	 */
	private boolean isRespond = false;
	/**
	 * 是否已经打开XMPP流
	 */
	private boolean openStream;
	/**
	 * SASL授权是否错误
	 */
	private boolean saslFailed = false;
	/**
	 * 授权错误原因
	 */
	private String reason = "";

	public SASLAuthentication(XMPPConnection connection) {
		this.connection = connection;
	}

	@Override
	public User authenticate(String username, String password, String resource, LoginType type)
			throws XMPPException {
		StringBuffer xmlParams = new StringBuffer();
		String authText = getAuthenticationText(username, password);
		try {
			xmlParams.append("<auth xmlns='urn:ietf:params:xml:ns:xmpp-sasl' mechanism='PLAIN'>");
			xmlParams.append("<verify>");
			xmlParams.append(this.connection.encryption.encryption(authText));
			xmlParams.append("</verify>");
			xmlParams.append("<type>");
			xmlParams.append(type.value());
			xmlParams.append("</type>");
			
			if (ext != null) {
				xmlParams.append(ext.toAuthenParams());
			}

			xmlParams.append("</auth>");

			// 没有打开流，等待打开流再去授权
			synchronized (this) {
				while(!this.openStream) {
					wait();
				}
			}
			LogUtils.jLog().e("等待流返回》》");
			this.connection.writer.write(xmlParams.toString());
			this.connection.writer.flush();
			// 等待数据返回
			synchronized (this) {
				while(!this.isRespond) {
					wait(XMPPConfig.getLoginTimeout());
				}
				isRespond = false; //设置为默认值
			}
			
			if(saslFailed) {
				saslFailed = false; //设置为默认值
				throw new XMPPException(reason);
			}
			
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return user;
	}

	protected String getAuthenticationText(String username, String password) {
		StringBuilder text = new StringBuilder();
		text.append("\0");
		text.append(username);
		text.append("\0");
		text.append(password);
		return text.toString();
	}

	public void authenticated(User user) {
		synchronized (this) {
			this.isRespond = true;
			this.user = user;
			notifyAll();
		}
	}

	void authenticationFailed(String reason) {
		synchronized (this) {
			this.isRespond = true;
			this.saslFailed = true;
			this.reason = reason;
			notifyAll();
		}
	}

	void finishOpenStream() {
		synchronized (this) {
			this.openStream = true;
			notifyAll();
		}
	}

	/**
	 * 设置额外的校验数据
	 * @param ext
	 */
	 void setExt(BaseAuthentExt ext) {
		this.ext = ext;
	}

	void init() {
		saslFailed = false;
		openStream = false;
		reason = "";
	}

}
