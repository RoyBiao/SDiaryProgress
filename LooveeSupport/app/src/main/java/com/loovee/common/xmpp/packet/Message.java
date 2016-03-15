package com.loovee.common.xmpp.packet;

import android.text.TextUtils;

import com.loovee.common.xmpp.annotation.db.Id;
import com.loovee.common.xmpp.annotation.db.Transient;


public class Message extends Packet {
    @Id
    private String id = System.currentTimeMillis() + "";

    private Type type = Type.normal;
    private String subject = null;
    private String language;
    private String threadID;

    private String nick;
    private String age;

    private String roomid;

    /**
     * 消息类型，例如是文本还是图片还是语音
     */
    private String newstype;

    /**
     * 性别
     */
    private String sex;
    /**
     * 用户头像
     */
    private String avatar;


    private boolean isMessageReceipt;

    /**
     * 保存临时的图片或者语音
     */
    private String tempPath;

    /**
     * 是否是真人认证
     */
    private int vauth;

    /**
     * 修改和或者插入时间
     */
    private String time;

    @Transient
    private Body body;

    /**
     * 礼物ID
     */
    private String giftId;
    /**
     * 礼物名称
     */
    private String giftName;


    private String at;
    private String atNick;

    /**
     * 操作
     */
    private String operation;


    /**
     * 外键
     */
    private String bodyId;
    /**
     * 该条语音信息是否已经阅读
     */
    private boolean isReadVoice = false;

    @Transient
    private boolean isProgress = false;

    /**
     * 是否正在播放语音
     */

    @Transient
    private boolean isPlayingVoice = false;


    public boolean isPlayingVoice() {
        return isPlayingVoice;
    }

    public void setPlayingVoice(boolean isPlayingVoice) {
        this.isPlayingVoice = isPlayingVoice;
    }

    public boolean isReadVoice() {
        return isReadVoice;
    }

    public void setReadVoice(boolean isReadVoice) {
        this.isReadVoice = isReadVoice;
    }

    public boolean isProgress() {
        return isProgress;
    }

    public void setIsProgress(boolean isProgress) {
        this.isProgress = isProgress;
    }

    public Message() {

    }

    public Message(String to) {
        setTo(to);
    }

    public Message(String to, Type type) {
        setTo(to);
        this.type = type;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public Type getType() {
        return this.type;
    }

    public void setType(Type type) {
        if (type == null) {
            throw new IllegalArgumentException("Type cannot be null.");
        }
        this.type = type;
    }

    public String getSubject() {
        return this.subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }


    public String getRoomid() {
        return roomid;
    }

    public void setRoomid(String roomid) {
        this.roomid = roomid;
    }

    public String getGiftId() {
        return giftId;
    }

    public void setGiftId(String giftId) {
        this.giftId = giftId;
    }

    public String getGiftName() {
        return giftName;
    }

    public void setGiftName(String giftName) {
        this.giftName = giftName;
    }

    private String getLanguage() {
        return this.language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String toXML() {
        StringBuilder buf = new StringBuilder();
        buf.append("<message");
        if (isMessageReceipt()) { // 属于消息回执
            buf.append(" to=\"mk\" from=\"").append(getTo())
                    .append("\"><x xmlns=\"jabber:x:event\"><id>");
            buf.append(getPacketID()).append("</id><delivered/></x>");
        } else {
            //属性开始
            buf.append(" id=\"").append(getPacketID()).append("\"");

            if (!TextUtils.isEmpty(getTo())) {
                buf.append(" to=\"").append(getTo()).append("\"");
            }
            if (!TextUtils.isEmpty(getFrom())) {
                buf.append(" from=\"").append(getFrom()).append("\"");
            }
            if (!TextUtils.isEmpty(getLanguage())) {
                buf.append(" xml:lang=\"").append(getLanguage()).append("\"");
            }
            if (getType() != null) {
                buf.append(" type=\"").append(getType()).append("\"");
            }
            if (!TextUtils.isEmpty(getNick())) {
                buf.append(" nick=\"").append(getNick()).append("\"");
            }
            if (!TextUtils.isEmpty(getAvatar())) {
                buf.append(" avatar=\"").append(getAvatar()).append("\"");
            }
            if (!TextUtils.isEmpty(getTime())) {
                buf.append(" time=\"").append(getTime()).append("\"");
            }
            if (!TextUtils.isEmpty(getSex())) {
                buf.append(" sex=\"").append(getSex()).append("\"");
            }
            if (!TextUtils.isEmpty(getAge())) {
                buf.append(" age=\"").append(getAge()).append("\"");
            }
            if (!TextUtils.isEmpty(getRoomid())) {
                buf.append(" roomid=\"").append(getRoomid()).append("\"");
            }
            if (!TextUtils.isEmpty(newstype)) {
                buf.append(" newstype=\"").append(getNewstype()).append("\"");
            }
            if (!TextUtils.isEmpty(at)) {
                buf.append(" at=\"").append(getAt()).append("\"");
            }
            if (!TextUtils.isEmpty(atNick)) {
                buf.append(" atNick=\"").append(getAtNick()).append("\"");
            }

            buf.append(">");
            //属性结束

            //body内容
            if (body != null) {
                buf.append(body.toXML());
            }

            //礼物部分
            if (!TextUtils.isEmpty(giftId)) {
                buf.append("<gift");
                buf.append(" giftid=\"").append(getGiftId()).append("\"");
                buf.append(" name=\"").append(getGiftName() == null ? "" : getGiftName()).append("\"");
                buf.append("/>");
            }

            buf.append(getExtensionsXML());
        }

        buf.append("</message>");
        return buf.toString();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if ((o == null) || (getClass() != o.getClass()))
            return false;

        Message message = (Message) o;
        if (this.id.equals(message.getId()) && this.type == message.type) {
            return true;
        }

        return false;
    }

    @Override
    public int hashCode() {
        int result = this.type != null ? this.type.hashCode() : 0;
        result = 31 * result
                + (this.subject != null ? this.subject.hashCode() : 0);
        result = 31 * result
                + (this.language != null ? this.language.hashCode() : 0);
        result = 31 * result + this.body.hashCode();
        return result;
    }

    public static enum Type {
        normal("normal"),

        chat("chat"), //普通消息

        groupchat("groupchat"), //群消息

        headline("headline"), //系统消息

        error("error"); //错误消息

        private String value;

        private Type(String value) {
            this.value = value;
        }

        public static Type fromString(String name) {
            try {
                return valueOf(name);
            } catch (Exception e) {
            }
            return normal;
        }

        @Override
        public String toString() {
            return value;
        }
    }


    public static enum Operation {
        noticeJoin("noticeJoin"), //通知房主有人申请加入包房
        noticeJoinResponse("noticeJoinResponse"), //通知申请人申请加入成功或失败
        noticeOutMember("noticeOutMember"), //通知房主有成员退出包房
        noticeKickMember("noticeKickMember");//通知被踢出成员用户
        private String operation;

        private Operation(String operation) {
            this.operation = operation;
        }

        public static Operation fromString(String name) {
            try {
                return valueOf(name);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return noticeJoin;
        }

        @Override
        public String toString() {
            return operation;
        }
    }


    /**
     * 消息类型
     */
    public enum NewsType {
        audio("audio"),
        text("text"),
        pic("pic"),
        cartoon("cartoon"), // 动画魔法表情
        toast("toast"),// toast
        gift("gift"),//送礼
        favoritelike("favoritelike"), // 喜欢消息
        button("button"),
        room("room");


        private String value;

        private NewsType(String value) {
            this.value = value;
        }

        public static NewsType fromString(String name) {
            try {
                return valueOf(name);
            } catch (Exception e) {
            }
            return text;
        }

        @Override
        public String toString() {
            return value;
        }
    }


    public static enum Sex {
        MALE("male"), FEMALE("female");
        private String value;

        private Sex(String value) {
            this.value = value;
        }

        public String value() {
            return value;
        }
    }


    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }


    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }


    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    /**
     * @return the avatar
     */
    public String getAvatar() {
        return avatar;
    }

    /**
     * @param avatar the avatar to set
     */
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }


    /**
     * @return the threadID
     */
    public String getThread() {
        return threadID;
    }

    /**
     * @param threadID the threadID to set
     */
    public void setThread(String threadID) {
        this.threadID = threadID;
    }


    public int getVauth() {
        return vauth;
    }

    public void setVauth(int vauth) {
        this.vauth = vauth;
    }


    public String getThreadID() {
        return threadID;
    }

    public void setThreadID(String threadID) {
        this.threadID = threadID;
    }

    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }


    public boolean isMessageReceipt() {
        return isMessageReceipt;
    }

    public void setIsMessageReceipt(boolean isMessageReceipt) {
        this.isMessageReceipt = isMessageReceipt;
    }

    public String getBodyId() {
        return bodyId;
    }

    public void setBodyId(String bodyId) {
        this.bodyId = bodyId;
    }

    public String getTempPath() {
        return tempPath;
    }

    public void setTempPath(String tempPath) {
        this.tempPath = tempPath;
    }

    public String getNewstype() {
        return newstype;
    }

    public void setNewstype(String newstype) {
        this.newstype = newstype;
    }

    public String getAt() {
        return at;
    }

    public void setAt(String at) {
        this.at = at;
    }

    public String getAtNick() {
        return atNick;
    }

    public void setAtNick(String atNick) {
        this.atNick = atNick;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }
}