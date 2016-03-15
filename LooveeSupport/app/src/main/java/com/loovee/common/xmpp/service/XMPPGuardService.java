package com.loovee.common.xmpp.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.loovee.common.util.LogUtils;


public class XMPPGuardService extends Service{

	@Override
	public IBinder onBind(Intent intent) {
		
		return null;
	}
	
	
	@Override
	public void onCreate() {
		super.onCreate();
		keepXMPPServiceAlive();
	}

	/**
	 * 保持XMPPService一直在运行
	 */
	private void keepXMPPServiceAlive() {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				while(true) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					if(!XMPPService.isServiceRunning(XMPPGuardService.this, XMPPService.class.getName())) {
						LogUtils.jLog().e("XMPPGuardService .....");
						Intent service = new Intent(XMPPGuardService.this, XMPPService.class);
						startService(service);
					}
				}
				
			}
		}).start();
	}
	
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		reStart();
	}


	private void reStart() {
		Intent service = new Intent(this, XMPPGuardService.class);
		startService(service);
	}
	
	
	

}
