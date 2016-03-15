package com.loovee.common.xmpp.bean;

import android.text.TextUtils;

/**
 * 本类用于描述手机设备信息以及客户端版本号,封装了imei、型号、软件下载来源、软件版本号、手机当前语言、 手机操作系统类型、sdk版本号、当前网络类型。
 * 
 * 
 */
public class DeviceInfo {

	// 手机imei号
	private String imei = "";
	// 手机型号
	private String model = "";
	// 客户端下载来源
	private String downUrl = "";
	// 客户端版本号
	private String version = "";
	// 客户端当前语言
	private String language = "";
	// 客户端手机系统
	private String system = "android";
	// 客户端sdk 版本号
	private String sdkVersion = "";
	// 当前网络类型
	private String netType = "";
	// 获取uuid
	private String uuid = "";
	// mac地址
	private String mac = "";
	// android唯一标识符
	private String androidId = "";

	public DeviceInfo() {
		
	}

	/**
	 * 设定手机IMEI
	 * 
	 * @param imei
	 * @author alonso lee
	 */
	public void setIMEI(String imei) {
		this.imei = imei;
	}

	/**
	 * 获取手机IMEI号。
	 * 
	 * @return 手机IMEI号
	 * @author alonso lee
	 */
	public String getIMEI() {
		if (imei != null) {
			return imei;
		} else {
			imei = "";
			return imei;
		}
	}

	/**
	 * 设定手机设备型号信息.如"GT-I9100"
	 * 
	 * @param model
	 *            手机设备型号信息
	 * @author alonso lee
	 */
	public void setDeviceModel(String model) {
		this.model = model;
	}

	/**
	 * 获取手机设备型号信息.如"GT-I9100"
	 * 
	 * @return 手机设备型号信息
	 * @author alonso lee
	 */
	public String getDeviceModel() {
		return this.model;
	}

	/**
	 * 设定客户端软件下载来源
	 * 
	 * @param downUrl
	 *            客户端软件下载来源
	 * @author alonso lee
	 */
	public void setDownloadSource(String downUrl) {
		this.downUrl = downUrl;
	}

	/**
	 * 获取客户端软件下载来源
	 * 
	 * @return 客户端软件下载来源
	 * @author alonso lee
	 */
	public String getDownloadSource() {
		return this.downUrl;
	}

	/**
	 * 设定客户端版本号
	 * 
	 * @param version
	 *            客户端版本号
	 * @author alonso lee
	 */
	public void setClientVersion(String version) {
		this.version = version;
	}

	/**
	 * 获取客户端版本号
	 * 
	 * @return 客户端版本号
	 * @author alonso lee
	 */
	public String getClientVersion() {
		return this.version;
	}

	/**
	 * 设定客户端当前语言类型
	 * 
	 * @param language
	 *            客户端当前语言类型
	 * @author alonso lee
	 */
	public void setClientLanguage(String language) {
		this.language = language;
	}

	/**
	 * 获取客户端当前语言类型
	 * 
	 * @return 客户端当前语言类型
	 * @author alonso lee
	 */
	public String getClientLanguage() {
		return this.language;
	}

	/**
	 * 设置当前操作系统版本号
	 * 
	 * @param system
	 *            操作系统版本号
	 * @author alonso lee
	 */
	public void setOS(String system) {
		this.system = system;
	}

	/**
	 * 获取当前操作系统版本号
	 * 
	 * @return 操作系统版本号
	 * @author alonso lee
	 */
	public String getOS() {
		return this.system;
	}

	/**
	 * 设定手机SDK版本号
	 * 
	 * @param sdkVersion
	 *            手机SDK版本号
	 * @author alonso lee
	 */
	public void setSdkVersion(String sdkVersion) {
		this.sdkVersion = sdkVersion;
	}

	/**
	 * 获取手机SDK版本号
	 * 
	 * @return 手机SDK版本号
	 * @author alonso lee
	 */
	public String getSdkVersion() {
		return this.sdkVersion;
	}

	/**
	 * 设定手机当前网络类型
	 * 
	 * @param networkType
	 *            手机当前网络类型
	 * @author alonso lee
	 */
	public void setNetworkType(String networkType) {
		this.netType = networkType;
	}

	/**
	 * 获取手机当前网络类型
	 * 
	 * @return 手机当前网络类型
	 * @author alonso lee
	 */
	public String getNetworkType() {
		return this.netType;
	}

	/**
	 * 设置UUID
	 * 
	 * @param uuid
	 */
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	/**
	 * 获取UUID
	 * 
	 * @return
	 */
	public String getUuid() {
		return uuid;
	}

	public void setMac(String s) {
		if (TextUtils.isEmpty(s)) {
			return;
		}
		this.mac = s;
	}

	public String getMac() {
		return this.mac;
	}

	public void setAndroidId(String id) {
		if (TextUtils.isEmpty(id)) {
			return;
		}
		this.androidId = id;
	}

	public String getAndroidId() {
		return this.androidId;
	}

	
	public String getDeviceId() {
		String deviceid ="";
		if(TextUtils.isEmpty(getIMEI())){
			deviceid = getUuid();
		}else {
			deviceid = getIMEI();
		}
		return deviceid;
	}
	

}
