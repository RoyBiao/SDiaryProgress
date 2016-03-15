package com.loovee.common.bean;

import android.text.TextUtils;

import com.loovee.common.util.ALLog;


/**
 * 此类封装了分发器返回的IM服务器ip地址、端口，域名；OCR服务器ip地址、端口； cardboxing客户端当前最新版本、下载地址、更新说明。
 * 所有方法简要说明： ServerInfo: 构造函数 setIMServerIP: 设置IM服务器IP地址 getIMServerIP:
 * 设置IM服务器IP地址 setSocketPort: 设置IM服务器Socket端口 getSocketPort: 获取IM服务器Socket端口
 * setServerName: 设置IM服务器服务域名。不需要带"@" getServerName: 获取IM服务器服务域名 setWhatsNew:
 * 设定最新版本的更新说明 getWhatsNew: 获取最新版本的更新说明 setLatestVer: 设定当前最新的发行版本 getLatestVer:
 * 获取当前最新的发型版本号 setNewVersionUrl: 设定最新版本下载地址 getNewVersionUrl: 返回最新版本下载地址
 * 
 * @author Alonso Lee
 */
public class ServerInfo {

	/* IM服务器IP地址 */
	private String imServerIP = "52.76.64.129";
	/* Socket端口 */
	private String socketPort = "9124";

	private String workserver = "";
	/* 服务器名 */
	private String serverName = "mk";
	/* 当前最新发行版本号 */
	private String latestVer = "";
	/* 版本更新内容 */
	private String whatsNew = "";
	/* 新版本下载地址 */
	private String newVerUrl = "";

	/* RTP服务器地址 */
	private String rtpServerIp = "";
	/* RTP服务器端口 */
	private int rtpServerPort = 0;
	/* 媒体服务器IP */
	private String mediaServerUrl = "";
	/* 媒体服务器IP */
	private String mediaServerIp = "52.76.64.129";
	/* 媒体服务器端口 */
	private String mediaServerPort = "80";
	// 充值回调地址
	private String chargeCallbackUrl = "";
	// 支付宝充值url
	private String alipayUrl = "";
	private String checkAuthCode = "";
	// 银联充值url
	private String uppayUrl = "";
	// 移动MM充值url
	private String mobileUrl = "";
	// 联通充值url
	private String unionUrl = "";
	// 报纸URL
	private String newsUrl = "";
	// 积分商城URL
	private String pointMarketUrl = "";
	// 是否强制更新
	private boolean forceUpdate = false;
	// 经济系统url
	private String extendcharge = "http://52.76.64.129:7280";
	/* HTTP端口 */
	private String httpPort = "";

	/**
	 * ServerInfo类构造函数
	 * 
	 * @author Alonso Lee
	 */
	public ServerInfo() {

	}

	/**
	 * 设置IM服务器IP地址
	 * 
	 * @param ip
	 *            IM服务器IP地址
	 * @author Alonso Lee
	 */
	public void setIMServerIP(String ip) {
		if (TextUtils.isEmpty(ip)) {
			ALLog.d("you input null param,im server ip set to default");
			return;
		}
		this.imServerIP = ip;
	}

	/**
	 * 获取IM服务器IP地址
	 * 
	 * @return IM服务器IP地址
	 * @author Alonso Lee
	 */
	public String getIMServerIP() {
		return this.imServerIP;
	}

	/**
	 * @return the rtpServerIp
	 */
	public String getRtpServerIp() {
		return rtpServerIp;
	}

	/**
	 * @param rtpServerIp
	 *            the rtpServerIp to set
	 */
	public void setRtpServerIp(String rtpServerIp) {
		this.rtpServerIp = rtpServerIp;
	}

	/**
	 * @return the rtpServerPort
	 */
	public int getRtpServerPort() {
		return rtpServerPort;
	}

	/**
	 * @param rtpServerPort
	 *            the rtpServerPort to set
	 */
	public void setRtpServerPort(int rtpServerPort) {
		this.rtpServerPort = rtpServerPort;
	}

	/**
	 * 设定媒体服务器IP地址
	 * 
	 * @param ip
	 *            媒体服务器IP地址
	 * @author Alonso Lee
	 */
	public void setMediaServerIp(String ip) {
		if (TextUtils.isEmpty(ip)) {
			ALLog.d("you input null param,media server ip set to default");
			return;
		}
		this.mediaServerIp = ip;
	}

	/**
	 * 获取媒体服务器IP地址
	 * 
	 * @return 媒体服务器IP地址
	 * @author Alonso Lee
	 */
	public String getMediaServerIp() {
		return this.mediaServerIp;
	}

	/**
	 * 设定媒体服务器端口号
	 * 
	 * @param port
	 *            媒体服务器端口号
	 * @author Alonso Lee
	 */
	public void setMediaServerPort(String port) {
		if (TextUtils.isEmpty(port)) {
			ALLog.d("you input null param,media server port set to default");
			return;
		}
		this.mediaServerPort = port;
	}

	/**
	 * 获取媒体服务器端口号
	 * 
	 * @return 媒体服务器端口号
	 * @author Alonso Lee
	 */
	public String getMediaServerPort() {
		return this.mediaServerPort;
	}

	/**
	 * 设置IM服务器Socket端口
	 * 
	 * @param port
	 *            IM服务器Socket端口
	 * @author Alonso Lee
	 */
	public void setIMSocketPort(String port) {
		if (TextUtils.isEmpty(port)) {
			ALLog.d("you input null param,im server port set to default");
			return;
		}
		this.socketPort = port;
	}

	/**
	 * 获取IM服务器Socket端口
	 * 
	 * @return IM服务器Socket端口
	 * @author Alonso Lee
	 */
	public String getIMSocketPort() {
		return this.socketPort;
	}

	/**
	 * 设置IM服务器服务域名。不需要带"@"
	 * 
	 * @param serverName
	 *            IM服务器服务域名
	 * @author Alonso Lee
	 */
	public void setServerName(String serverName) {
		if (TextUtils.isEmpty(serverName)) {
			ALLog.d("you input null param,im server name set to default");
			return;
		}
		this.serverName = serverName;
	}

	/**
	 * 获取IM服务器服务域名
	 * 
	 * @return IM服务器服务域名。不带"@"
	 * @author Alonso Lee
	 */
	public String getServerName() {
		return this.serverName;
	}

	/**
	 * 设定最新版本的更新说明
	 * 
	 * @param tips
	 *            更新说明
	 * @author Alonso Lee
	 */
	public void setWhatsNew(String tips) {
		this.whatsNew = tips;
	}

	/**
	 * 获取最新版本的更新说明
	 * 
	 * @return 更新说明
	 * @author Alonso Lee
	 */
	public String getWhatsNew() {
		return this.whatsNew;
	}

	/**
	 * 设定当前最新的发行版本
	 * 
	 * @param version
	 * @author Alonso Lee
	 */
	public void setLatestVer(String version) {
		this.latestVer = version;
	}

	/**
	 * 获取当前最新的发型版本号
	 * 
	 * @return 最新发行版本号
	 * @author Alonso Lee
	 */
	public String getLatestVer() {
		return this.latestVer;
	}

	/**
	 * 设定最新版本下载地址
	 * 
	 * @param url
	 *            最新版本下载地址
	 * @author Alonso Lee
	 */
	public void setNewVersionUrl(String url) {
		this.newVerUrl = url;
	}

	/**
	 * 返回最新版本下载地址
	 * 
	 * @return 最新版本下载地址
	 * @author Alonso Lee
	 */
	public String getNewVersionUrl() {
		return this.newVerUrl;
	}

	/**
	 * 设置新闻地址URL
	 * 
	 * @param u
	 *            新闻地址URL
	 * @author Alonso Lee
	 */
	public void setNewsUrl(String u) {
		if (TextUtils.isEmpty(u)) {
			ALLog.d("you input null param,news url set to default");
			return;
		}
		this.newsUrl = u;
	}

	/**
	 * 获取新闻地址URL
	 * 
	 * @return 新闻地址URL
	 * @author Alonso Lee
	 */
	public String getNewsUrl() {
		return this.newsUrl;
	}

	/**
	 * 设置积分商城地址URL
	 * 
	 * @param u
	 *            积分商城地址URL
	 * @author ccx
	 */
	public void setPointMarketUrl(String u) {
		if (TextUtils.isEmpty(u)) {
			ALLog.d("you input null param,point market set to default");
			return;
		}
		this.pointMarketUrl = u;
	}

	/**
	 * 获取积分商城地址URL
	 * 
	 * @return 积分商城地址URL
	 * @author ccx
	 */
	public String getPointMarketUrl() {
		return this.pointMarketUrl;
	}

	/**
	 * 设置充值地址URL
	 * 
	 * @param u
	 *            充值地址URL
	 * @author Alonso Lee
	 */
	public void setChargeCallbackUrl(String u) {
		if (TextUtils.isEmpty(u)) {
			ALLog.d("you input null param,charge callback set to default");
			return;
		}
		this.chargeCallbackUrl = u;
	}

	/**
	 * 获取充值地址URL
	 * 
	 * @return 充值地址URL
	 * @author Alonso Lee
	 */
	public String getChargeCallbackUrl() {
		return this.chargeCallbackUrl;
	}

	/**
	 * 甚至是否需要强制更新
	 * 
	 * @param forceUpdate
	 *            true 需要强制更新
	 */
	public void setForceUpdateState(boolean forceUpdate) {
		this.forceUpdate = forceUpdate;
	}

	/**
	 * 检查是否需要强制更新
	 * 
	 * @return
	 */
	public boolean isForceUpdate() {
		return forceUpdate;
	}

	public void setExtendChargeUrl(String url) {
		if (TextUtils.isEmpty(url)) {
			ALLog.d("you input null param,extend charger url set to default");
			return;
		}
		this.extendcharge = url;
	}

	public String getExtendChargeUrl() {
		return this.extendcharge;
	}

	/**
	 * 设定IM服务器http端口
	 * 
	 * @param port
	 *            IM服务器http端口
	 * @author Alonso Lee
	 */
	public void setHttpPort(String port) {
		if (port == null) {
			return;
		}
		this.httpPort = port;
	}

	/**
	 * 获取http端口号
	 * 
	 * @return http端口
	 * @author Alonso Lee
	 */
	public String getHttpPort() {
		return this.httpPort;
	}

	public void setAlipayUrl(String url) {
		this.alipayUrl = url;
	}

	/**
	 * @return the cHECKE
	 */
	public String getChecke() {
		return checkAuthCode;
	}

	public void setChecke(String code) {
		this.checkAuthCode = code;
	}

	public String getAlipayUrl() {
		return this.alipayUrl;
	}

	/**
	 * @return the uppayUrl
	 */
	public String getUppayUrl() {
		return uppayUrl;
	}

	/**
	 * @param uppayUrl
	 *            the uppayUrl to set
	 */
	public void setUppayUrl(String uppayUrl) {
		this.uppayUrl = uppayUrl;
	}

	/**
	 * @return the mobileUrl
	 */
	public String getMobileUrl() {
		return mobileUrl;
	}

	/**
	 * @param mobileUrl
	 *            the mobileUrl to set
	 */
	public void setMobileUrl(String mobileUrl) {
		this.mobileUrl = mobileUrl;
	}

	/**
	 * @return the unionUrl
	 */
	public String getUnionUrl() {
		return unionUrl;
	}

	/**
	 * @param unionUrl
	 *            the unionUrl to set
	 */
	public void setUnionUrl(String unionUrl) {
		this.unionUrl = unionUrl;
	}

	/**
	 * @return the mediaServerUrl
	 */
	public String getMediaServerUrl() {
		return mediaServerUrl;
	}

	/**
	 * @param mediaServerUrl
	 *            the mediaServerUrl to set
	 */
	public void setMediaServerUrl(String mediaServerUrl) {
		this.mediaServerUrl = mediaServerUrl;
	}

	/**
	 * @return the workserver
	 */
	public String getWorkserver() {
		return workserver;
	}

	/**
	 * @param workserver
	 *            the workserver to set
	 */
	public void setWorkserver(String workserver) {
		this.workserver = workserver;
	}

}
