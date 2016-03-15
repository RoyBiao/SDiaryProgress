package com.loovee.common.xmpp.core;

import android.text.TextUtils;
import android.util.Xml;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseStream;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.loovee.common.R;
import com.loovee.common.app.LooveeApplication;
import com.loovee.common.net.HttpFactory;
import com.loovee.common.util.APPUtils;
import com.loovee.common.util.DeviceInfoUtils;
import com.loovee.common.util.LogUtils;
import com.loovee.common.xmpp.bean.DeviceInfo;
import com.loovee.common.xmpp.bean.Dispatcher;
import com.loovee.common.xmpp.bean.Imserver;
import com.loovee.common.xmpp.bean.LoginInfo;
import com.loovee.common.xmpp.core.UserAuthentication.LoginType;
import com.loovee.common.xmpp.exception.NoNetworkException;
import com.loovee.common.xmpp.filter.PacketFilter;
import com.loovee.common.xmpp.packet.Packet;
import com.loovee.common.xmpp.packet.Presence;
import com.loovee.common.xmpp.packet.Presence.Type;
import com.loovee.common.xmpp.packet.XMPPError;
import com.loovee.common.xmpp.utils.ObservableReader;
import com.loovee.common.xmpp.utils.ObservableWriter;
import com.loovee.common.xmpp.utils.PacketParserUtils;
import com.loovee.common.xmpp.utils.StringUtils;

import org.apache.http.ParseException;
import org.apache.http.protocol.HTTP;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;

public class XMPPConnection {

    /**
     * 是否启用调试模式
     */
    public static boolean DEBUG_ENABLED = true;
    private static final Set<ConnectionCreationListener> connectionEstablishedListeners = new CopyOnWriteArraySet<ConnectionCreationListener>();

    private static AtomicInteger connectionCounter = new AtomicInteger(0);

    public interface ConnectionListerner {
        public void onConnection();
    }

    private ConnectionListerner connectionListerner;

    String host;
    int port;
    Socket socket;
    int connectionCounterValue = connectionCounter.getAndIncrement();
    User user;
    private DeviceInfo deviceInfo;

    /**
     * 服务器名字
     */
    String serviceName;
    /**
     * 当前与服务器连接的id
     */
    String connectionID = null;
    /**
     * 当前登录的用户的jid
     */
    private String jid = "";

    /**
     * 是否已经连接上服务器
     */
    private boolean connected = false;
    /**
     * 是否完成了授权
     */
    private boolean authenticated = false;

    private static XMPPStore store;
    private SASLAuthentication saslAuthentication = new SASLAuthentication(this);

    private BaseAuthentExt ext;
    private ChatManager chatManager;
    PacketWriter packetWriter;
    PacketReader packetReader;
    Writer writer;
    Reader reader;
    LoginListener loginListener;
    private static Dispatcher dispatcher;

    /**
     * xmpp配置
     */
    XMPPConfig xmppConfig;
    XMPPEncryption encryption;

    /**
     * 注册一个重连管理者
     */
    static {
        XMPPConnection
                .addConnectionCreationListener(new ConnectionCreationListener() {
                    @Override
                    public void connectionCreated(XMPPConnection connection) {
                        connection.addConnectionListener(new ReconnectionManager(connection));
                    }
                });
    }

    /**
     * 构造一个XMPP连接器
     *
     * @throws XMPPException
     * @params configuration
     * 连接的相关配置信息
     * @params deviceInfo
     * 设备的相关信息
     */
    public XMPPConnection() throws XMPPException {

        deviceInfo = new DeviceInfo();
        deviceInfo.setIMEI(DeviceInfoUtils
                .getIMEI(LooveeApplication.getLocalLoovee()));
        deviceInfo.setAndroidId(DeviceInfoUtils
                .getAndroidId(LooveeApplication.getLocalLoovee()));
        deviceInfo.setDeviceModel(DeviceInfoUtils
                .getDeviceModel());
        deviceInfo.setMac(DeviceInfoUtils
                .getWifiMac(LooveeApplication.getLocalLoovee()));
        deviceInfo.setClientLanguage(DeviceInfoUtils
                .getSystemLanguage());

        ext = new MaohuAuthenExt(deviceInfo);
        encryption = new DESXMPPEncryption();
        saslAuthentication.setExt(ext);
    }

    /**
     * 返回当前的连接ID
     *
     * @return
     */
    public synchronized String getConnectionID() {
        if (!isConnected()) {
            return null;
        }
        return this.connectionID;
    }

    /**
     * 获取服务器名字
     *
     * @return
     */
    public synchronized String getServiceName() {
        return this.serviceName;
    }

    /**
     * 获取连接的主机地址
     *
     * @return
     */
    public synchronized String getHost() {
        return this.host;
    }

    /**
     * 获取端口号
     *
     * @return
     */
    public synchronized int getPort() {
        return this.port;
    }

    /**
     * 获取当前用户的jid
     *
     * @return
     */
    public synchronized String getJid() {
        if (!isAuthenticated()) {
            return null;
        }
        return this.jid;
    }


    /**
     * 如果上次已经登录过，那么自动登录，
     *
     * @throws XMPPException
     * @throws NoNetworkException
     */
    public synchronized void login() throws XMPPException {
        LoginInfo loginInfo = getLoginInfo();
        if (loginInfo != null) {
            login(loginInfo.getName(), loginInfo.getPassword(), LoginType.ValueOf(loginInfo.getType()));
        }
    }

    /**
     * 登录XMPP服务器
     *
     * @param username 用户名
     * @param password 密码
     * @throws XMPPException
     * @throws NoNetworkException
     */
    public synchronized void login(String username, String password, LoginType type) throws XMPPException {
        login(username, password, "phone", type);
    }

    /**
     * 登录XMPP服务器
     *
     * @param username 用户名
     * @param password 密码
     * @param resource
     * @throws XMPPException
     * @throws NoNetworkException
     */
    public synchronized void login(String username, String password,
                                   String resource, LoginType type) throws XMPPException {
        login(username, password, resource, true, type);
    }

    /**
     * 登录XMPP服务器
     *
     * @param username     用户名
     * @param password     密码
     * @param resource
     * @param sendPresence 是否发送出席
     * @throws XMPPException
     * @throws NoNetworkException
     */
    public synchronized void login(String username, String password,
                                   String resource, boolean sendPresence, LoginType type) throws XMPPException {
        if (!isConnected()) {
            connect();
        }
        if (this.authenticated) {
            throw new IllegalStateException("Already logged in to server.");
        }
        if (TextUtils.isEmpty(username)) {
            throw new IllegalArgumentException("username is empty.");
        }
        if (TextUtils.isEmpty(password)) {
            throw new IllegalArgumentException("password is empty.");
        }

        user = saslAuthentication.authenticate(username, password,
                resource, type);
        this.jid = user.jid;
        this.serviceName = StringUtils.parseServer(this.jid);

        if (sendPresence) {
            Presence presence = new Presence(Type.available);
            presence.setPriority(50);
            sendPacket(presence);
        }
        getConfiguration().setLoginInfo(username, password, resource, sendPresence);
        saveLoginInfo(username, password, resource, sendPresence, type.value()); //保存登录信息
        saveUserInfo(user); //保存用户信息
        this.authenticated = true;
        if (loginListener != null) {
            loginListener.onLoginSuccess(user);
        }

    }

    public static void saveUserInfo(User user) {
        if (store != null) {
            Gson gson = new Gson();
            String json = gson.toJson(user);
            store.addUser(json);
        }
    }

    /**
     * 保存用户登录信息,将数据保存到本地
     *
     * @param username 用户名
     * @param password 用户密码
     * @param resource 资源
     * @params token
     * 登录标示
     */
    private void saveLoginInfo(String username, String password,
                               String resource, boolean sendPresence, String type) {
        if (store != null) {
            LoginInfo info = new LoginInfo();
            info.setName(username);
            info.setPassword(password);
            info.setResource(resource);
            info.setSendPresence(sendPresence);
            info.setType(type);
            Gson gson = new Gson();
            store.addLoginInf(gson.toJson(info));
        }
    }

    /**
     * 是否已经连接上XMPP服务器
     *
     * @return 返回true表示连接上了，否则没有连接上
     */
    public synchronized boolean isConnected() {
        return this.connected && socket != null && socket.isConnected();
    }

    /**
     * 连接是否已经授权
     *
     * @return
     */
    public synchronized boolean isAuthenticated() {

        return this.authenticated;
    }

    /**
     * 关闭XMPP连接
     *
     * @param unavailablePresence
     */
    protected synchronized void shutdown(Presence unavailablePresence) {

        this.packetWriter.sendPacket(unavailablePresence);

        this.authenticated = false;
        this.connected = false;

        this.packetReader.shutdown();
        this.packetWriter.shutdown();
        try {
            Thread.sleep(40L);
        } catch (Exception e) {
        }

        if (this.reader != null) {
            try {
                this.reader.close();
            } catch (Throwable ignore) {
            }
            this.reader = null;
        }
        if (this.writer != null) {
            try {
                this.writer.close();
            } catch (Throwable ignore) {
            }
            this.writer = null;
        }
        try {
            this.socket.close(); // 关闭流
            this.socket = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.saslAuthentication.init();
        LogUtils.jLog().e("XMPP连接已经关闭");

    }

    /**
     * 断开XMPP连接
     */
    public synchronized void disconnect() {
        disconnect(new Presence(Presence.Type.unavailable));
    }

    /**
     * 断开连接
     *
     * @param unavailablePresence
     */
    public synchronized void disconnect(Presence unavailablePresence) {
        if ((this.packetReader == null) || (this.packetWriter == null)) {
            return;
        }
        shutdown(unavailablePresence);
        this.packetWriter.cleanup();
        this.packetWriter = null;
        this.packetReader.cleanup();
        this.packetReader = null;
    }

    /**
     * 向XMPP服务器发送数据包
     *
     * @param packet
     */
    public void sendPacket(Packet packet) {
        if (!isConnected()) {
            try {
                connect();
            } catch (XMPPException e) {
                e.printStackTrace();
            }
            if (!isConnected()) {
                throw new IllegalStateException("Not connected to server.");
            }
        }
        if (packet == null) {
            throw new NullPointerException("Packet is null.");
        }
        this.packetWriter.sendPacket(packet);
    }


    /**
     * 添加一个连接监听者
     *
     * @param connectionCreationListener
     */
    public static void addConnectionCreationListener(
            ConnectionCreationListener connectionCreationListener) {
        connectionEstablishedListeners.add(connectionCreationListener);
    }

    /**
     * 删除一个连接监听者
     *
     * @param connectionCreationListener
     */
    public static void removeConnectionCreationListener(
            ConnectionCreationListener connectionCreationListener) {
        connectionEstablishedListeners.remove(connectionCreationListener);
    }


    private synchronized void connectUsingConfiguration(ConnectionConfiguration config)
            throws XMPPException {
        this.host = config.getHost();
        this.port = config.getPort();
        try {
            if (config.getSocketFactory() == null) {
                this.socket = new Socket(this.host, this.port);
            } else {
                this.socket = config.getSocketFactory().createSocket(this.host,
                        this.port);
            }
        } catch (UnknownHostException uhe) {
            String errorMessage = "Could not connect to " + this.host + ":"
                    + this.port + ".";
            throw new XMPPException(errorMessage, new XMPPError(
                    XMPPError.Condition.remote_server_timeout, errorMessage),
                    uhe);
        } catch (IOException ioe) {
            String errorMessage = "XMPPError connecting to " + this.host + ":"
                    + this.port + ".";

            throw new XMPPException(errorMessage, new XMPPError(
                    XMPPError.Condition.remote_server_error, errorMessage), ioe);
        }

        this.serviceName = config.getServiceName();
        initConnection();
    }

    /**
     * 初始化连接
     *
     * @throws XMPPException
     */
    private synchronized void initConnection() throws XMPPException {
        boolean isFirstInitialization = (this.packetReader == null)
                || (this.packetWriter == null);

        initReaderAndWriter();
        if (isFirstInitialization) {
            this.packetWriter = new PacketWriter(this);
            this.packetReader = new PacketReader(this);
        } else {
            this.packetWriter.init();
            this.packetReader.init();
        }

        this.packetWriter.startup();

        this.packetReader.startup();

        this.connected = true;

        this.packetWriter.startKeepAliveProcess();

        if (isFirstInitialization) {
            for (ConnectionCreationListener listener : connectionEstablishedListeners) {
                listener.connectionCreated(this);
            }
        } else {
            this.packetReader.notifyReconnection();
        }
        if (connectionListerner != null) {
            connectionListerner.onConnection();
        }
    }

    /**
     * 初始化读写器
     *
     * @throws XMPPException
     */
    private synchronized void initReaderAndWriter() throws XMPPException {
        try {

            this.reader = new BufferedReader(new InputStreamReader(
                    this.socket.getInputStream(), "UTF-8"));

            this.writer = new BufferedWriter(new OutputStreamWriter(
                    this.socket.getOutputStream(), "UTF-8"));

            if (DEBUG_ENABLED) {
                this.reader = new ObservableReader(this.reader);
                this.writer = new ObservableWriter(this.writer);
            }

        } catch (IOException ioe) {
            throw new XMPPException(
                    "XMPPError establishing connection with server.",
                    new XMPPError(XMPPError.Condition.remote_server_error,
                            "XMPPError establishing connection with server."),
                    ioe);
        }
    }

    /**
     * 获取连接配置
     *
     * @return
     */
    protected ConnectionConfiguration getConfiguration() {
        return this.xmppConfig.getConnectionConfiguration();
    }

    /**
     * 注册连接监听者
     *
     * @param connectionListener 连接监听者
     */
    public synchronized void addConnectionListener(ConnectionListener connectionListener) {
        if (!isConnected()) {
            throw new IllegalStateException("Not connected to server.");
        }
        if (connectionListener == null) {
            return;
        }
        if (!this.packetReader.connectionListeners.contains(connectionListener))
            this.packetReader.connectionListeners.add(connectionListener);
    }

    public synchronized void removeConnectionListener(ConnectionListener connectionListener) {
        this.packetReader.connectionListeners.remove(connectionListener);
    }

    /**
     * 注册一个数据包监听者
     *
     * @param packetListener
     */
    public void addPacketListener(PacketListener packetListener,
                                  PacketFilter packetFilter) {
        if (!isConnected()) {
            throw new IllegalStateException("Not connected to server.");
        }
        this.packetReader.addPacketListener(packetListener, packetFilter);
    }

    /**
     * 删除一个数据包监听者
     *
     * @param packetListener
     */
    public void removePacketListener(PacketListener packetListener) {
        if (packetReader == null) return;
        this.packetReader.removePacketListener(packetListener);
    }

    /**
     * 连接XMPP服务器,请在子线程执行该方法。
     *
     * @throws NoNetworkException
     */
    public synchronized void connect() throws XMPPException {
        dispatcher = initDispatcher();
        if (dispatcher == null) {
            return;
        }
        if(isConnected()) {
            throw new RuntimeException("connection has connected");
        }
        Imserver imserver = dispatcher.getImserver();
        xmppConfig = new XMPPConfig(imserver.getIp(), imserver.getPort());
        connectUsingConfiguration(this.xmppConfig.getConnectionConfiguration());

    }


    /**
     * 初始化分发器
     */
    private synchronized Dispatcher initDispatcher() {
        Dispatcher dispatcher = null;
        final String url1 = LooveeApplication.getLocalLoovee().getString(R.string.dispatcher1);
        dispatcher = getDispatcher(url1);
        if (dispatcher == null) {
            String url2 = LooveeApplication.getLocalLoovee().getString(R.string.dispatcher2);
            dispatcher = getDispatcher(url2);
        }
        return dispatcher;
    }


    /**
     * 获取分发器
     * 打印方法 ： LogUtils.jLog().e(StreamUtils.getStreamString(response.getBaseStream()));
     *
     * @param url url地址
     * @return
     */
    private synchronized Dispatcher getDispatcher(String url) {
        Dispatcher dispatcher = null;
        try {
            HttpUtils httpUtils = HttpFactory.createDefault(LooveeApplication.getLocalLoovee(), true);
            RequestParams params = new RequestParams(HTTP.UTF_8);
            params.addQueryStringParameter("platform", DeviceInfoUtils.getOS());
            params.addQueryStringParameter("version", APPUtils.getVersion(true));
            ResponseStream response = httpUtils.sendSync(HttpMethod.GET, url, params);
            if (response.getStatusCode() == HttpURLConnection.HTTP_OK) {
                XmlPullParser parser = Xml.newPullParser();
                parser.setInput(response.getBaseStream(), HTTP.UTF_8);
                dispatcher = PacketParserUtils.xmlToJavaBean(parser, Dispatcher.class);
            }
        } catch (HttpException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        return dispatcher;
    }

    /**
     * 返回一个授权管理
     *
     * @return
     */
    public SASLAuthentication getSASLAuthentication() {
        return saslAuthentication;
    }

    /**
     * 设置密码的校验登录的加密方式
     *
     * @param encryption
     */
    public void setEncryption(XMPPEncryption encryption) {
        this.encryption = encryption;
    }

    public PacketCollector createPacketCollector(PacketFilter packetFilter) {
        return this.packetReader.createPacketCollector(packetFilter);
    }

    /**
     * 获取聊天管理者
     *
     * @return
     */
    public synchronized ChatManager getChatManager() {
        if (this.chatManager == null) {
            this.chatManager = new ChatManager(this);
        }
        return this.chatManager;
    }

    /**
     * 设置用户信息的持久化方式，默认不持久化
     *
     * @param store
     */
    public static void setStore(XMPPStore store) {
        XMPPConnection.store = store;
    }

    /**
     * 获取登录信息,如果没有登录信息，返回NULL
     *
     * @return
     */
    public static LoginInfo getLoginInfo() {
        if (store != null) {
            String info = store.getLoginInfo();
            if (!TextUtils.isEmpty(info)) {
                Gson gson = new Gson();
                return gson.fromJson(info, LoginInfo.class);
            }
        }
        return null;
    }


    public static User getUser() {
        if (store != null) {
            String userStr = store.getUser();
            if (!TextUtils.isEmpty(userStr)) {
                Gson gson = new Gson();
                return gson.fromJson(userStr, User.class);
            }
        }
        return null;
    }


    /**
     * 清除登录信息
     */
    public synchronized static void cleanLoginInfo() {
        if (store != null) {
            store.clear();
        }
    }


    /**
     * 设定登录监听者
     *
     * @param loginListener
     */
    public void setLoginListener(LoginListener loginListener) {
        this.loginListener = loginListener;
    }


    /**
     * 获取一个分发器
     * @return
     */
    public synchronized static Dispatcher getDispatcher() {
        return XMPPConnection.dispatcher;
    }

    public void setOnConnectionListerner(ConnectionListerner connectionListerner) {
        this.connectionListerner = connectionListerner;
    }

    public DeviceInfo getDeviceInfo() {
        return deviceInfo;
    }


}
