package com.loovee.common.xmpp.core;

import android.text.TextUtils;
import android.util.Xml;

import com.loovee.common.xmpp.filter.PacketFilter;
import com.loovee.common.xmpp.packet.IQ;
import com.loovee.common.xmpp.packet.IQ.Type;
import com.loovee.common.xmpp.packet.Packet;
import com.loovee.common.xmpp.packet.Presence;
import com.loovee.common.xmpp.packet.StreamError;
import com.loovee.common.xmpp.packet.XMPPError;
import com.loovee.common.xmpp.provider.IQProvider;
import com.loovee.common.xmpp.provider.ProviderManager;
import com.loovee.common.xmpp.utils.PacketParserUtils;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * 数据包读取器
 *
 * @author Jesse
 */
public class PacketReader {
    private XMPPConnection connection;
    private boolean done;
    private String connectionID = null;
    private Thread readerThread;
    private ExecutorService listenerExecutor;
    private XmlPullParser parser;
    private Collection<PacketCollector> collectors = new ConcurrentLinkedQueue<PacketCollector>();
    protected final Map<PacketListener, ListenerWrapper> listeners = new ConcurrentHashMap<PacketListener, ListenerWrapper>();
    protected final Collection<ConnectionListener> connectionListeners = new CopyOnWriteArrayList<ConnectionListener>();


    public PacketReader(XMPPConnection xmppConnection) {
        this.connection = xmppConnection;
        init();
    }

    public void init() {
        this.done = false;
        this.connectionID = null;
        readerThread = new Thread() {
            @Override
            public void run() {
                super.run();
                PacketReader.this.parsePackets(this);
            }
        };
        this.readerThread.setName("Packet Reader Thread("
                + this.connection.connectionCounterValue + ")");
        this.readerThread.setDaemon(true);
        this.readerThread.setPriority(Thread.MAX_PRIORITY);
        listenerExecutor = Executors
                .newSingleThreadExecutor(new ThreadFactory() {

                    @Override
                    public Thread newThread(Runnable r) {
                        Thread thread = new Thread(
                                r,
                                "Listener Processor Thread("
                                        + PacketReader.this.connection.connectionCounterValue
                                        + ")");
                        thread.setDaemon(true);
                        return thread;
                    }
                });
        resetParser();
    }

    private void resetParser() {
        try {
            this.parser = Xml.newPullParser();
            this.parser
                    .setFeature(
                            "http://xmlpull.org/v1/doc/features.html#process-namespaces",
                            true);
            this.parser.setInput(this.connection.reader);
        } catch (XmlPullParserException xppe) {
            xppe.printStackTrace();
        }
    }

    private void parsePackets(Thread thread) {
        try {
            int eventType = parser.getEventType();
            do {
                switch (eventType) {

                    case XmlPullParser.START_TAG:
                        final String startTagName = parser.getName();
                        if (startTagName.equals("stream")) { // 收到服务器的流信息
                            parseStartStream();
                            this.connection.getSASLAuthentication().finishOpenStream();
                        } else if (startTagName.equals("message")) { //处理消息
                            processPacket(PacketParserUtils.parseMessage(parser));
                        } else if (startTagName.equals("iq")) {     //处理IQ
                            processPacket(parseIQ(parser));
                        } else if (startTagName.equals("presence")) { //处理出席
                            processPacket(PacketParserUtils.parsePresence(parser));
                        } else if (startTagName.equals("success")) { //处理登录成功
                            User user = parseStreamSuccess(this.parser);
                            this.connection.getSASLAuthentication().authenticated(user);
                        } else if (startTagName.equals("failed")) { //处理失败
                            String reason = parseStreamFailed(this.parser);
                            this.connection.getSASLAuthentication().authenticationFailed(reason);
                        } else if (startTagName.equals("error")) { //处理错误
                            String errStr = parseStreamError(this.parser);
                            throw new XMPPException(new StreamError(errStr));

                        }
                        break;

                    case XmlPullParser.END_TAG:
                        if (this.parser.getName().equals("stream")) {
                            this.connection.disconnect(); //断开连接
                        }
                        break;
                }
                eventType = parser.next();

                // 如果任务已经完成或者XML文档已经结束，则退出循环
                if ((this.done) || (eventType == XmlPullParser.END_DOCUMENT))
                    break;
            } while (thread == this.readerThread);
        } catch (Exception e) {
            e.printStackTrace();
            if (!this.done) {
                notifyConnectionError(e);
            }
        }
    }

    /**
     * 通知连接错误
     *
     * @param e
     */
    void notifyConnectionError(Exception e) {
        this.done = true;
        this.connection.shutdown(new Presence(Presence.Type.unavailable));
        e.printStackTrace();
        for (ConnectionListener listener : this.connectionListeners) {
            listener.connectionClosedOnError(e);
        }
    }

    /**
     * 解析IQ
     *
     * @param parser
     * @return
     * @throws Exception
     */
    private IQ parseIQ(XmlPullParser parser) throws Exception {
        IQ iqPacket = null;

        String id = parser.getAttributeValue("", "id");
        String to = parser.getAttributeValue("", "to");
        String from = parser.getAttributeValue("", "from");
        IQ.Type type = IQ.Type.fromString(parser.getAttributeValue("", "type"));
        XMPPError error = null;

        boolean done = false;
        while (!done) {
            int eventType = parser.next();

            if (eventType == XmlPullParser.START_TAG) {
                String elementName = parser.getName();
                final String namespace = parser.getNamespace();
                if (elementName.equals("error")) {
                    error = PacketParserUtils.parseError(parser);
                } else if (elementName.equals("ping")
                        && namespace.equals("urn:xmpp:ping")
                        && type == Type.GET) { //处理服务器请求的心跳
                    replyHeartbeatIQ(id, to, from, namespace);
                    return null; //结束
                } else {
                    Object provider = ProviderManager.getInstance()
                            .getIQProvider(elementName, namespace);
                    String clazzName = ProviderManager.getInstance()
                            .getJavaBean(elementName, namespace);
                    if (provider != null) {
                        if ((provider instanceof IQProvider)) {
                            iqPacket = ((IQProvider) provider).parseIQ(parser, clazzName);
                        }
                    }
                    if (iqPacket != null) {
                        iqPacket.setQueryXmlns(namespace); //设置命名空间
                    }
                }

            } else if ((eventType == XmlPullParser.END_TAG)
                    && (parser.getName().equals("iq"))) {
                done = true;
            }

        }

        if (iqPacket == null) {
            if ((IQ.Type.GET == type) || (IQ.Type.SET == type)) {
                iqPacket = new IQ() {
                    public String getChildElementXML() {
                        return null;
                    }
                };
                iqPacket.setPacketID(id);
                iqPacket.setTo(from);
                iqPacket.setFrom(to);
                iqPacket.setType(IQ.Type.RESULT);
                this.connection.sendPacket(iqPacket); // 发生错误，把错误信息发送回给服务器
                return null; //结束
            }
            iqPacket = new IQ() {
                public String getChildElementXML() {
                    return null;
                }

            };
        }

        iqPacket.setPacketID(id);
        iqPacket.setTo(to);
        iqPacket.setFrom(from);
        iqPacket.setType(type);
        iqPacket.setError(error);

        return iqPacket;
    }

    /**
     * 回复一条心跳IQ
     *
     * @param id
     * @param to
     * @param from
     * @param namespace
     */
    private void replyHeartbeatIQ(String id, String to, String from,
                                  final String namespace) {
        IQ heartbeatIQ = new IQ() {

            @Override
            public String getChildElementXML() {
                return "<ping xmlns=" + "\"" + namespace + "\"" + "/>";
            }
        };
        heartbeatIQ.setPacketID(id);
        heartbeatIQ.setTo(from);
        heartbeatIQ.setFrom(to);
        heartbeatIQ.setType(Type.RESULT);
        this.connection.sendPacket(heartbeatIQ);
    }


    /**
     * 解析XML流中的失败信息
     *
     * @param parser
     * @return
     * @throws XmlPullParserException
     * @throws IOException
     */
    private String parseStreamFailed(XmlPullParser parser)
            throws XmlPullParserException, IOException {
        boolean done = false;
        String reason = "";
        while (!done) {
            int eventType = parser.next();
            String tagName = parser.getName();
            if ((eventType == XmlPullParser.END_TAG)
                    && tagName.equals("failed")) {
                done = true; // 结束循环
            } else if (eventType == XmlPullParser.START_TAG) {
                reason = tagName;
            }
        }
        return reason;

    }

    /**
     * 解析XML流中的成功信息
     *
     * @param parser
     * @throws IOException
     * @throws XmlPullParserException
     */
    private User parseStreamSuccess(XmlPullParser parser)
            throws XmlPullParserException, IOException {
        User user = new User();
        boolean done = false;
        while (!done) {
            int eventType = parser.next();
            String tagName = parser.getName();
            if ((eventType == XmlPullParser.START_TAG)
                    && tagName.equals("token")) {
                user.token = parser.nextText();
            } else if ((eventType == XmlPullParser.START_TAG)
                    && tagName.equals("jid")) {
                user.jid = parser.nextText();
            } else if ((eventType == XmlPullParser.START_TAG)
                    && tagName.equals("android")) {

            } else if (eventType == XmlPullParser.START_TAG
                    && tagName.equals("sex")) {
                user.sex = parser.nextText();
            } else if (eventType == XmlPullParser.START_TAG
                    && tagName.equals("forceprefectinfo")) {
                String value = parser.nextText();
                if(!TextUtils.isEmpty(value)) {
                    user.forceprefectinfo = Boolean.valueOf(value);
                }
            } else if(eventType == XmlPullParser.START_TAG
                    && tagName.equals("searchpageversion")){
                String value = parser.nextText();
                if(!TextUtils.isEmpty(value)) {
                    user.setSearchpageversion(Integer.valueOf(value));
                }
            } else  if(eventType == XmlPullParser.START_TAG
                        &&tagName.equals("giftversion")){
                String value = parser.nextText();
                if(!TextUtils.isEmpty(value)) {
                    user.setGiftversion(Integer.valueOf(value));
                }
            } else if (eventType == XmlPullParser.START_TAG
                    &&tagName.equals("propsversion")) {
                String value = parser.nextText();
                if(!TextUtils.isEmpty(value)) {
                    user.setPropsversion(Integer.valueOf(value));
                }
            } else if (eventType == XmlPullParser.END_TAG
                    && tagName.equals("success")) {
                done = true; // 结束循环
            }
        }
        return user;
    }

    /**
     * 解析XML流中的错误信息
     *
     * @param parser
     * @return
     * @throws IOException
     * @throws XmlPullParserException
     */
    private String parseStreamError(XmlPullParser parser) throws IOException,
            XmlPullParserException {
        String streamError = null;
        boolean done = false;
        while (!done) {
            int eventType = parser.next();

            if (eventType == XmlPullParser.START_TAG) {
                if (streamError == null) {
                    streamError = parser.getName();
                }
            } else if ((eventType == XmlPullParser.END_TAG)
                    && (parser.getName().equals("error"))) {
                done = true; // 结束循环
            }
        }
        return streamError;
    }

    private void parseStartStream() {
        if ("jabber:client".equals(this.parser.getNamespace(null))) {
            for (int i = 0; i < this.parser.getAttributeCount(); i++)
                if (this.parser.getAttributeName(i).equals("id")) {
                    this.connectionID = this.parser.getAttributeValue(i);
                    this.connection.connectionID = this.connectionID;
                } else if (this.parser.getAttributeName(i).equals("from")) {
                    this.connection.serviceName = this.parser
                            .getAttributeValue(i);
                }
        }
    }

    /**
     * 启动数据包读取
     */
    public void startup() throws XMPPException {
        readerThread.start();
    }

    private static class ListenerWrapper {
        private PacketListener packetListener;
        private PacketFilter packetFilter;

        public ListenerWrapper(PacketListener packetListener,
                               PacketFilter packetFilter) {
            this.packetListener = packetListener;
            this.packetFilter = packetFilter;
        }

        public void notifyListener(Packet packet) {
            if ((this.packetFilter == null)
                    || (this.packetFilter.accept(packet)))
                this.packetListener.processPacket(packet);
        }
    }

    /**
     * 通知重连成功
     */
    public void notifyReconnection() {
        for (ConnectionListener listener : this.connectionListeners)
            try {
                listener.reconnectionSuccessful();
            } catch (Exception e) {
                e.printStackTrace();
            }
    }

    /**
     * 处理接收到的数据包
     *
     * @param packet
     */
    private void processPacket(Packet packet) {
        if (packet == null) {
            return;
        }
        this.listenerExecutor.submit(new ListenerNotification(packet));
    }

    /**
     * 关闭
     */
    public void shutdown() {
        if (!this.done) {
            for (ConnectionListener listener : this.connectionListeners) {
                try {
                    listener.connectionClosed();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        this.done = true;
        this.listenerExecutor.shutdown();
    }

    /**
     * 添加一个数据包监听者
     *
     * @param packetListener 数据包监听者
     * @param packetFilter   数据包过滤器
     */
    public void addPacketListener(PacketListener packetListener,
                                  PacketFilter packetFilter) {
        ListenerWrapper wrapper = new ListenerWrapper(packetListener,
                packetFilter);
        this.listeners.put(packetListener, wrapper);
    }

    /**
     * 删除一个数据包监听者
     *
     * @param packetListener
     */
    public void removePacketListener(PacketListener packetListener) {
        this.listeners.remove(packetListener);
    }

    private class ListenerNotification implements Runnable {
        private Packet packet;

        public ListenerNotification(Packet packet) {
            this.packet = packet;
        }

        public void run() {
            for (PacketReader.ListenerWrapper listenerWrapper : PacketReader.this.listeners
                    .values())
                listenerWrapper.notifyListener(this.packet);
        }

    }

    /**
     * 清理所有监听器
     */
    void cleanup() {
        this.connectionListeners.clear();
        this.listeners.clear();
        this.collectors.clear();
    }

    public PacketCollector createPacketCollector(PacketFilter packetFilter) {
        PacketCollector collector = new PacketCollector(this, packetFilter);
        this.collectors.add(collector);
        return collector;
    }

    protected void cancelPacketCollector(PacketCollector packetCollector) {
        this.collectors.remove(packetCollector);
    }

}
