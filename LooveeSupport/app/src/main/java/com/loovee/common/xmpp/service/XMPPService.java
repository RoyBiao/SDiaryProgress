package com.loovee.common.xmpp.service;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.loovee.common.app.LooveeApplication;
import com.loovee.common.util.LogUtils;
import com.loovee.common.util.NetUtil;
import com.loovee.common.xmpp.core.ConnectionListener;
import com.loovee.common.xmpp.core.LoginListener;
import com.loovee.common.xmpp.core.PacketListener;
import com.loovee.common.xmpp.core.SharepreferenceXMPPStore;
import com.loovee.common.xmpp.core.User;
import com.loovee.common.xmpp.core.XMPPConnection;
import com.loovee.common.xmpp.core.XMPPConnection.ConnectionListerner;
import com.loovee.common.xmpp.core.XMPPException;
import com.loovee.common.xmpp.core.XMPPStore;
import com.loovee.common.xmpp.filter.IQQueryXmlnsFilter;
import com.loovee.common.xmpp.filter.MessageFilter;
import com.loovee.common.xmpp.packet.Packet;
import com.loovee.common.xmpp.packet.StreamError;

import java.util.List;

/**
 * XMPP服务，提供发送和接收数据的功能
 *
 * @author Jesse
 */
public class XMPPService extends Service implements ConnectionListener,
        LoginListener, PacketListener, ConnectionListerner {

    public final static String ACTION_CONNECTION_CLOSED = "com.loovee.common.action.connection.closed";
    public final static String ACTION_CONNECTION_CLOSED_ERROR = "com.loovee.common.action.connection_closed_error";
    public final static String ACTION_RECONNECTING_IN = "com.loovee.common.action.rreconnection_in";
    public final static String ACTION_RECONNECTION_SUCCESSFUL = "com.loovee.common.action.reconnection_successful";
    public final static String ACTION_RECONNECTION_FAILED = "com.loovee.common.action.reconnection_failed";
    public final static String ACTION_CONNECTION_LOGIN_SUCCESS = "com.loovee.common.action.login_success";
    public final static String ACTION_CONNECTION_LOGIN_FAIL = "com.loovee.common.action.login_fail";


    private NetStatusBrodcastReceiver mBrodcastReceiver;

    private XMPPConnection connection;
    private XMPPBinder mBinder = new XMPPBinder();

    @Override
    public IBinder onBind(Intent intent) {
        LogUtils.jLog().e("onBind");
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        LogUtils.jLog().e("onUnbind");
        return super.onUnbind(intent);
    }

    public class XMPPBinder extends Binder {

        public XMPPConnection getXMPPConnection() {
            return connection;
        }

    }

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtils.jLog().e("XMPPService 正在启动...");
        isNetworkAvailable = NetUtil.isNetworkAvailable(this);
        registerNetReceiver();
        initConnection();
        keepGuardServiceAlive();
    }

    private void registerNetReceiver() {
        mBrodcastReceiver = new NetStatusBrodcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(mBrodcastReceiver, filter);

    }

    /**
     * 保持守护服务一直运行
     */
    private void keepGuardServiceAlive() {
        new Thread(new Runnable() {

            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (!XMPPService.isServiceRunning(XMPPService.this,
                            XMPPGuardService.class.getName())) {
                        Intent service = new Intent(XMPPService.this,
                                XMPPGuardService.class);
                        LooveeApplication.instances.startService(service);
                    }

                }
            }
        }).start();
    }

    /**
     * 初始化XMPP连接
     */
    private void initConnection() {
        try {
            XMPPStore store = new SharepreferenceXMPPStore(XMPPService.this);
            XMPPConnection.setStore(store);

            connection = new XMPPConnection();
            connection.setOnConnectionListerner(XMPPService.this);
            connection.setLoginListener(XMPPService.this);

        } catch (XMPPException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtils.jLog().e("XMPPService 正在关闭...");
        if (connection != null) {
            connection.removeConnectionListener(XMPPService.this);
            connection.disconnect();
        }
        if (mBrodcastReceiver != null) {
            unregisterReceiver(mBrodcastReceiver);
        }
        reStart();
    }

    /**
     * 重启服务
     */
    private void reStart() {
        Intent service = new Intent(this, XMPPService.class);
        startService(service);
    }

    /**
     * 判断某个服务是否正在运行的方法
     *
     * @param mContext
     * @param serviceName
     * @return true代表正在运行，false代表服务没有正在运行
     */
    public static boolean isServiceRunning(Context mContext, String serviceName) {
        boolean isWork = false;
        ActivityManager myAM = (ActivityManager) mContext
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningServiceInfo> list = myAM
                .getRunningServices(Integer.MAX_VALUE);
        if (list.size() <= 0) {
            return false;
        }
        for (int i = 0; i < list.size(); i++) {
            String mName = list.get(i).service.getClassName().toString();
            if (mName.equals(serviceName)) {
                isWork = true;
                break;
            }
        }
        return isWork;
    }

    @Override
    public void connectionClosed() {
        connection.removeConnectionListener(XMPPService.this);
        Intent intent = new Intent(ACTION_CONNECTION_CLOSED);
        this.sendBroadcast(intent);
    }

    @Override
    public void connectionClosedOnError(Exception exception) {
        Intent intent = new Intent(ACTION_CONNECTION_CLOSED_ERROR);
        if ((exception instanceof XMPPException)) {
            XMPPException xmppEx = (XMPPException) exception;
            StreamError error = xmppEx.getStreamError();
            if (error != null) {
                String reason = error.getCode();
                if ("conflict".equals(reason)) {
                    intent.putExtra("conflict", true);
                }
            }
        }
        this.sendBroadcast(intent);

    }


    @Override
    public void reconnectingIn(int time) {
        Intent intent = new Intent(ACTION_RECONNECTING_IN);
        this.sendBroadcast(intent);
    }

    @Override
    public void reconnectionSuccessful() {
        Intent intent = new Intent(ACTION_RECONNECTION_SUCCESSFUL);
        this.sendBroadcast(intent);
    }

    @Override
    public void reconnectionFailed(Exception exception) {
        Intent intent = new Intent(ACTION_RECONNECTION_FAILED);
        this.sendBroadcast(intent);
    }

    @Override
    public void onLoginSuccess(User user) {
        sendLoginSuccessBroadcast(user);
        initCacheData();
    }


    /**
     * 初始化缓存数据
     */
    private void initCacheData() {
       // API.getInstance(PropsLogic.class).getPropsList(null);
    }


    /**
     * 发送登录成功的广播
     * @param user
     */
    private void sendLoginSuccessBroadcast(User user) {
        Intent intent = new Intent(ACTION_CONNECTION_LOGIN_SUCCESS);
        intent.putExtra("user", user);
        //sendCidToServier();
        this.sendBroadcast(intent);
    }

//    /**
//     * 发送第三方个推CID给服务器
//     */
//    private void sendCidToServier() {
//        EditVcard vcard = new EditVcard();
//        final String cid = PushManager.getInstance().getClientid(this);
//        vcard.setCid(cid);
//        API.getInstance(UserInfoLogic.class).motifyVcard(vcard, null);
//    }

    @Override
    public void OnLoginFailed() {
        Intent intent = new Intent(ACTION_CONNECTION_LOGIN_FAIL);
        this.sendBroadcast(intent);
    }


    //接收消息的地方
    @Override
    public void processPacket(Packet packet) {
        //处理消息
//        Message message = (Message) packet;
//        MessageHandler messageHandler = new MessageHandler(this);
//        messageHandler.handleMessage(message);
    }


    @Override
    public void onConnection() {
        connection.addConnectionListener(XMPPService.this);
        //监听消息
        connection.addPacketListener(XMPPService.this, new MessageFilter());
        //监听礼物变化
        connection.addPacketListener(new PacketListener() {

            @Override
            public void processPacket(Packet packet) {
                
            }
        }, new IQQueryXmlnsFilter("jabber:texas:gold:change"));

        connection.addPacketListener(new PacketListener() {

            @Override
            public void processPacket(Packet packet) {

            }
        }, new IQQueryXmlnsFilter("jabber:iq:vrecognize:status:notify"));

    }

    /**
     * 网络是否可用
     */
    private boolean isNetworkAvailable = false;

    private class NetStatusBrodcastReceiver extends BroadcastReceiver {
        private ConnectivityManager connectivityManager;
        private NetworkInfo info;

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                info = connectivityManager.getActiveNetworkInfo();
                if (info != null && info.isAvailable()) {
                    String name = info.getTypeName();
                    Log.e("mark", "当前网络名称：" + name);
                    if (!isNetworkAvailable) {
                        isNetworkAvailable = true;
                        new Thread(new Runnable() {

                            @Override
                            public void run() {
                                try {
                                    if (!connection.isConnected()) {
                                        connection.connect();
                                    }
                                    if (!connection.isAuthenticated()) {
                                        connection.login();
                                    }
                                } catch (XMPPException e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();

                    }
                } else {
                    Log.e("mark", "没有可用网络");
                    if (isNetworkAvailable) {
                        isNetworkAvailable = false;
                        connection.disconnect();
                    }

                }
            }
        }
    }

}
