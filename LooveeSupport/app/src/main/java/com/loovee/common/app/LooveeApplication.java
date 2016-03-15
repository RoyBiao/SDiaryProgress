package com.loovee.common.app;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.loovee.common.xmpp.core.XMPPConnection;
import com.loovee.common.xmpp.service.XMPPService;

public class LooveeApplication extends Application implements ServiceConnection {
    public static LooveeApplication instances;
    private XMPPConnection mConnection;
    // 版本控制
    private VersionConfig mVersionConfig;

    @Override
    public void onCreate() {
        super.onCreate();
        instances = this;
        // 版本控制,true是测试版本
        mVersionConfig = new VersionConfig(this, true);

        //启动im服务
        if (!XMPPService.isServiceRunning(this, XMPPService.class.getName())) {
            startXmppService();
        }
        bindXMppService();
    }

    /**
     * 获取全局的context
     *
     * @return
     */
    public Context getContext() {
        return getApplicationContext();
    }

    /**
     * 获取application对象
     *
     * @return
     */
    public static LooveeApplication getLocalLoovee() {
        return instances;
    }

    /**
     * 绑定XMPP服务
     */
    private void bindXMppService() {
        Intent service = new Intent(this, XMPPService.class);
        this.bindService(service, this, Context.BIND_AUTO_CREATE);
    }


    /**
     * 获取XMPP连接,该方法有可能会发生阻塞。
     *
     * @return 返回一个xmppconnection连接实例
     */
    public XMPPConnection getXMPPConnection() {
        synchronized (LooveeApplication.this) {
            while (mConnection == null) {
                try {
                    LooveeApplication.this.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        return this.mConnection;
    }

    /**
     * 启动XMPP服务
     */
    private void startXmppService() {
        Intent service = new Intent(this, XMPPService.class);
        startService(service);
    }

    @Override
    public void onServiceConnected(ComponentName name, final IBinder service) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    synchronized (LooveeApplication.this) {
                        mConnection = ((XMPPService.XMPPBinder) service).getXMPPConnection();
                        LooveeApplication.this.unbindService(LooveeApplication.this); // 解绑服务
                        LooveeApplication.this.notifyAll();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }).start();

    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
    }

    /**
     * 获取应用配置文件
     *
     * @return
     */
    public VersionConfig getVersionConfig() {
        return mVersionConfig;
    }
}
