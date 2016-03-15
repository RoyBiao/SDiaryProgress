package com.loovee.common.xmpp.core;

import com.loovee.common.app.LooveeApplication;
import com.loovee.common.constant.Constant;
import com.loovee.common.util.APPUtils;
import com.loovee.common.util.DeviceInfoUtils;
import com.loovee.common.xmpp.bean.DeviceInfo;
import com.loovee.common.xmpp.security.DES;

public class MaohuAuthenExt extends BaseAuthentExt {
	private String uniqueid;
	private String version = APPUtils.getVersion(true);
	private String os = DeviceInfoUtils.getOS();
	/**
	 * 渠道
	 */
	private String downfrom = APPUtils.getChannel(LooveeApplication.getLocalLoovee());
	private String resource = "android-wifi";
	private int communicationplatform = 0;
	
	private DeviceInfo deviceInfo;
	
	
	public MaohuAuthenExt(DeviceInfo deviceInfo) {
		try {
			this.deviceInfo = deviceInfo;
			this.uniqueid = DES.encryptDES(deviceInfo.getIMEI(),Constant.getEncryptKey());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

	@Override
	public String toAuthenParams() {
		StringBuffer extstr = new StringBuffer();
		
		extstr.append("<resource>");
		extstr.append(resource);
		extstr.append("</resource>");
		
		extstr.append("<uniqueid>");
		extstr.append(this.uniqueid);
		extstr.append("</uniqueid>");
		

		
		extstr.append("<version>");
		extstr.append(this.version);
		extstr.append("</version>");
		
		extstr.append("<mac>");
		extstr.append(deviceInfo.getMac());
		extstr.append("</mac>");
		
		extstr.append("<os>");
		extstr.append(this.os);
		extstr.append("</os>");
		
		extstr.append("<model>");
		extstr.append(deviceInfo.getDeviceModel());
		extstr.append("</model>");
		
		extstr.append("<sdkversion>");
		extstr.append(deviceInfo.getSdkVersion());
		extstr.append("</sdkversion>");
		
		extstr.append("<downfrom>");
		extstr.append(this.downfrom);
		extstr.append("</downfrom>");
		
		extstr.append("<communicationplatform>");
		extstr.append(this.communicationplatform);
		extstr.append("</communicationplatform>");
		
		return extstr.toString();
	}
	
	public void setUniqueid(String uniqueid) {
		this.uniqueid = uniqueid;
	}

}
