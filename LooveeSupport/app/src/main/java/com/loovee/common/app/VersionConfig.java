/**
 * 
 */
package com.loovee.common.app;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Environment;
import com.loovee.common.bean.ServerInfo;
import com.loovee.common.util.ALLog;

import java.io.File;
import java.io.IOException;

/**
 * 应用程序配置文件。可配置应用是国内版还是国际版，登陆方式。服务器配置信息，调试信息
 * 
 * @author alonso lee
 * 
 */
public class VersionConfig {

	// 图片保存路径
	public static String IMAGE_PATH = "images";
	// 语音保存路径
	public static String AUDIO_PATH = "audio";

	// 更新安装包
	public static String UPDATE_APK_PATH = "apks";
	// 启动界面
	public static String SPLASH_PATH = "splash";

	public static final String LOOVEE_DIR = "hotchat";

	// 应用版本号
	private final String appVersion;

	// 是否为测试版
	private boolean isDebugVersion;
	// 是否输出日志
	private boolean isShowLog = false;
	// 服务器配置文件
	private ServerInfo mServerInfo;

	public String disp;
	// 分发器端口
	public String port;

	// 上传图片
	public static final String UPLOAD_PIC_SERVERLET = "/fastdfs/oversea/PhotoServlet.php";
	// 上传图片
	public static final String UPLOAD_PIC_SERVERLET_BATE = "/fastdfs/oversea/PhotoServlet.php";
	// 上传语音
	public static final String UPLOAD_AUDIO_SERVERLET = "/fastdfs/oversea/audio.php";
	// 上传语音
	public static final String UPLOAD_AUDIO_SERVERLET_BATE = "/fastdfs/oversea/audio.php";
	
	/**
	 * 
	 */
	public VersionConfig(Context context, boolean isDebugVersion) {
		// TODO Auto-generated constructor stub
		// 获取版本号
		appVersion = getVersion(context);
		this.isDebugVersion = isDebugVersion;
		mServerInfo = new ServerInfo();
		loadServerInfo();
		checkDir(context);
		isShowLog = this.isDebugVersion;
		// 设置日志输出功能
		ALLog.setDebug(isShowLog);
	}

	/**
	 * 获取服务器配置文件
	 */
	public ServerInfo loadServerInfo() {

		if (isDebugVersion) {

			/* 测试分发器地址 */
			disp = "116.254.203.49";
			port = "9325";

			// 缺省测试服务器地址
			mServerInfo.setIMServerIP("116.254.203.49");
			// 缺省测试服务器端口
			mServerInfo.setIMSocketPort("8965");

			// 缺省测试RTP服务器地址
			mServerInfo.setRtpServerIp("116.254.203.49");
			// 缺省测试RTP服务器端口
			mServerInfo.setRtpServerPort(7552);
			// 域名
			mServerInfo.setServerName("mk");
			// 媒体服务器地址
			mServerInfo.setMediaServerIp("img1.imeach.com");
			// 媒体服务器端口
			mServerInfo.setMediaServerPort("9090");
			// im服务器http 端口
			mServerInfo.setHttpPort("5559");

			mServerInfo.setExtendChargeUrl("http://211.151.60.213:16000");

			mServerInfo
					.setChargeCallbackUrl("http://211.151.60.213:16000/IMExtendWebService/servlet/YeePayServlet");

		} else {
			disp = "dispatcher.meeyooapp.com";
			port = "9325";

			// 缺省运营服务器地址
			mServerInfo.setIMServerIP("121.199.36.138");
			// 缺省运营服务器端口
			mServerInfo.setIMSocketPort("16555");

			// 缺省运营RTP服务器地址
			mServerInfo.setRtpServerIp("121.199.36.138");
			// 缺省运营RTP服务器端口
			mServerInfo.setRtpServerPort(7552);

			// 域名
			mServerInfo.setServerName("mk");
			// im服务器http 端口
			mServerInfo.setHttpPort("165559");
			// 媒体服务器地址
			mServerInfo.setMediaServerIp("121.40.74.182");
			// 媒体服务器端口
			mServerInfo.setMediaServerPort("8001");
			// 经济系统
			mServerInfo.setExtendChargeUrl("http://121.199.36.138:9280");

		}
		return mServerInfo;

	}

	/**
	 * 获取服务器配置信息
	 * 
	 * @return
	 */
	public ServerInfo getServerInfo() {
		return mServerInfo;
	}

	/**
	 * 设置服务器配置信息
	 * 
	 * @param mServerInfo
	 *            the info to set
	 */
	public void setServerInfo(ServerInfo info) {
		this.mServerInfo = info;
	}

	/**
	 * 获取应用版本号
	 * 
	 * @return the appVersion
	 */
	public String getAppVersion() {
		return appVersion;
	}

	/**
	 * 设置是否为调试版本
	 * 
	 * @param isDebugVersion
	 *            the isDebugVersion to set
	 */
	public void setDebugVersion(boolean isDebugVersion) {
		this.isDebugVersion = isDebugVersion;
	}

	/**
	 * 设置是否输出日志
	 * 
	 * @param isShowLog
	 *            the isShowLog to set
	 */
	public void setShowLog(boolean isShowLog) {
		this.isShowLog = isShowLog;
		ALLog.setDebug(isShowLog);
	}

	/**
	 * 检测是否为调试版本
	 * 
	 * @return the isDebugVersion
	 */
	public boolean isDebugVersion() {
		return isDebugVersion;
	}

	/**
	 * 检测是否输出日志
	 * 
	 * @return the isShowLog
	 */
	public boolean isShowLog() {
		return isShowLog;
	}

	/**
	 * 获取应用版本号
	 * 
	 * @param context
	 * @return
	 */
	private String getVersion(Context context) {
		try {
			PackageInfo pi = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0);
			return pi.versionName;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
	}

	private void checkDir(Context context) {
		IMAGE_PATH = getExternalCacheDir(context, LOOVEE_DIR, IMAGE_PATH);
		AUDIO_PATH = getExternalCacheDir(context, LOOVEE_DIR, AUDIO_PATH);
		UPDATE_APK_PATH = getExternalCacheDir(context, LOOVEE_DIR,
				UPDATE_APK_PATH);

		SPLASH_PATH = getExternalCacheDir(context, LOOVEE_DIR, SPLASH_PATH);

	}

	private String getExternalCacheDir(Context context, String path, String dir) {
		File dataDir = new File(Environment.getExternalStorageDirectory(), path);
		File appCacheDir = new File(dataDir, dir);
		if (!appCacheDir.exists()) {
			if (!appCacheDir.mkdirs()) {
				ALLog.w("Unable to create external cache directory");
				return null;
			}
			try {
				new File(appCacheDir, ".nomedia").createNewFile();
			} catch (IOException e) {
				ALLog.i("Can't create \".nomedia\" file in application external cache directory");
			}
		}
		return appCacheDir.getAbsolutePath();
	}

	public static String getUploadServerUrl(String serverlet) {
		String servletUrl = "http://"
				+ LooveeApplication.getLocalLoovee().getVersionConfig()
						.getServerInfo().getMediaServerIp()
				+ ":"
				+ LooveeApplication.getLocalLoovee().getVersionConfig()
						.getServerInfo().getMediaServerPort() + serverlet;
		return servletUrl;
	}

}
