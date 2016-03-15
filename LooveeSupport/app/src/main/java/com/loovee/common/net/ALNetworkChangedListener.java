package com.loovee.common.net;



/**
 * 
 * @author alonso lee
 *
 */
public interface ALNetworkChangedListener {

	/**
	 * 数据连接已建立
	 * 
	 * @param type
	 *            当前数据连接类型
	 */
	public void onDataConnectionEstablished(ALConnectionManager.ALConnectionType type);

	/**
	 * 数据连接已丢失
	 */
	public void onDataConnectionLost();
}
