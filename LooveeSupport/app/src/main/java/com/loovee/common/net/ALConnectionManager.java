package com.loovee.common.net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

import java.util.ArrayList;

/**
 * 本类用于封装网络事件管理。
 * 
 * @author alonso lee
 * 
 */
public class ALConnectionManager {

	private static NetworkInfo mActivieNetworkInfo;
	private static NetworkInfo mLastActiveNetworkInfo;
	private static ALConnectionType mNetworkType = ALConnectionType.TYPE_NONE;
	private static ArrayList<ALNetworkChangedListener> listeners = new ArrayList<ALNetworkChangedListener>();
	
	public static void reset(){
		mActivieNetworkInfo = null;
		mLastActiveNetworkInfo = null;
		mNetworkType = ALConnectionType.TYPE_NONE;
		if(listeners != null){
			listeners.clear();
			listeners = null;
		}
		listeners = new ArrayList<ALNetworkChangedListener>();
	}

	/**
	 * 更新网络连接状态
	 * 
	 * @param newNetworkInfo
	 */
	public static void updateConnectionInfo(NetworkInfo newNetworkInfo) {
		mLastActiveNetworkInfo = mActivieNetworkInfo;
		mActivieNetworkInfo = newNetworkInfo;
		if (getConnectionType() != ALConnectionType.TYPE_NONE) {
			if (mLastActiveNetworkInfo == null) {
				notifyDataConnectionEstablished(getConnectionType());
			} else {
				if (getLastConnectionType() == getConnectionType()) {
				} else {
					// 需要重连
					notifyDataConnectionEstablished(getConnectionType());

				}
			}

		} else {
			notifyDataConnectionLost();
			mLastActiveNetworkInfo = null;
		}
	}


	/**
	 * 检测网络是否可用
	 * 
	 * @return
	 */
	public static boolean isNetWorkConnected(Context context) {
		boolean isConnected = false;
		ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo activeInfo = connMgr.getActiveNetworkInfo();
		if (activeInfo != null && activeInfo.isConnected()) {
			isConnected = true;
		} else {
		}
		return isConnected;
	}

	/**
	 * 获取当前网络类型
	 * 
	 * @return
	 */
	public static ALConnectionType getConnectionType() {
		if (mActivieNetworkInfo != null) {
			if (mActivieNetworkInfo.isConnected()) {
				if (mActivieNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
					mNetworkType = ALConnectionType.TYPE_WIFI;
				} else {
					mNetworkType = ALConnectionType.TYPE_MOBILE;
				}
				return mNetworkType;
			}
		}
		mNetworkType = ALConnectionType.TYPE_NONE;
		return mNetworkType;
	}

	/**
	 * 获取字符串形式的当前网络类型
	 * 
	 * @return
	 */
	public static String getConnectionTypeString() {
		if (mActivieNetworkInfo != null) {
			if (mActivieNetworkInfo.isConnected()) {
				if("WIFI".equals(mActivieNetworkInfo.getTypeName())){
					return "wifi";
				}else{
					return getNetTypeToString();
				}
			}
		}
		return "";
	}
	
	private static String getNetTypeToString(){
		String netType = "";
		switch (mActivieNetworkInfo.getType()) {
		case ConnectivityManager.TYPE_MOBILE:
            switch (mActivieNetworkInfo.getSubtype()) {
                case TelephonyManager.NETWORK_TYPE_GPRS: //联通2g
                case TelephonyManager.NETWORK_TYPE_CDMA: //电信2g
                case TelephonyManager.NETWORK_TYPE_EDGE: //移动2g
                case TelephonyManager.NETWORK_TYPE_1xRTT:
                case TelephonyManager. NETWORK_TYPE_IDEN:
                	netType = "2G";
                    break;
                case TelephonyManager.NETWORK_TYPE_EVDO_A: //电信3g
                case TelephonyManager.NETWORK_TYPE_UMTS:
                case TelephonyManager.NETWORK_TYPE_EVDO_0:
                case TelephonyManager.NETWORK_TYPE_HSDPA:
                case TelephonyManager.NETWORK_TYPE_HSUPA:
                case TelephonyManager.NETWORK_TYPE_HSPA:
                case TelephonyManager.NETWORK_TYPE_EVDO_B:
                case TelephonyManager.NETWORK_TYPE_EHRPD:
                case TelephonyManager.NETWORK_TYPE_HSPAP:
                	netType = "3G";
                    break;
                case TelephonyManager.NETWORK_TYPE_LTE:
                	netType = "4G";
                    break;
                default:
                	netType = "";
            }
		}
		
		return netType;
	}

	/**
	 * 获取上一个网络状态
	 * 
	 * @return
	 */
	public static ALConnectionType getLastConnectionType() {

		if (mLastActiveNetworkInfo != null) {
			ALConnectionType lastNetworkType = ALConnectionType.TYPE_NONE;
			if (mLastActiveNetworkInfo.isConnected()) {
				if (mLastActiveNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
					lastNetworkType = ALConnectionType.TYPE_WIFI;
				} else {
					lastNetworkType = ALConnectionType.TYPE_MOBILE;
				}

				return lastNetworkType;
			}
		}
		return ALConnectionType.TYPE_NONE;
	}

	public static void AddNetworkChangedListener(
			ALNetworkChangedListener listener) {
		if (!listeners.contains(listener)) {
			listeners.add(listener);
		}
	}

	public static void removeNetworkChangedListener(
			ALNetworkChangedListener listener) {
		if (listeners.contains(listener)) {
			listeners.remove(listener);
		}
	}

	private static void notifyDataConnectionEstablished(ALConnectionType type) {
		for (ALNetworkChangedListener l : listeners) {
			l.onDataConnectionEstablished(type);
		}
	}

	private static void notifyDataConnectionLost() {
		for (ALNetworkChangedListener l : listeners) {
			l.onDataConnectionLost();
		}
	}

	public enum ALConnectionType {
		TYPE_NONE, TYPE_WIFI, TYPE_MOBILE,
	}

}
