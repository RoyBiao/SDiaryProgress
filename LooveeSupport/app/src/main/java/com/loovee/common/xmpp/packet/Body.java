package com.loovee.common.xmpp.packet;

import android.text.TextUtils;

/**
 * Created by Administrator on 2016/2/18 0018.
 */
public class Body {
    private String id = System.currentTimeMillis()+"";
    private String message;
    private String langauge;
    private String smallpic;
    private String largepic;
    private String len;

    public Body() {

    }

    public Body(String language, String message) {
        if (message == null) {
            throw new NullPointerException("Message cannot be null.");
        }
        this.langauge = language;
        this.message = message;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Body(String message) {
        this(null, message);
    }

    public String getLangauge() {
        return langauge;
    }

    public void setLangauge(String langauge) {
        this.langauge = langauge;
    }

    public String getLargepic() {
        return largepic;
    }

    public void setLargepic(String largepic) {
        this.largepic = largepic;
    }

    public String getLen() {
        return len;
    }

    public void setLen(String len) {
        this.len = len;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSmallpic() {
        return smallpic;
    }

    public void setSmallpic(String smallpic) {
        this.smallpic = smallpic;
    }


    public String getLanguage() {
        if (Packet.DEFAULT_LANGUAGE.equals(this.langauge)) {
            return null;
        }

        return this.langauge;
    }

    public String getMessage() {
        return this.message;
    }

    public boolean equals(Object o) {
        if (this == o)
            return true;
        if ((o == null) || (getClass() != o.getClass()))
            return false;

        Body body = (Body) o;
        if (this.id.equals(body.getId())) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        int result = this.message.hashCode();
        result = 31 * result
                + (this.langauge != null ? this.langauge.hashCode() : 0);
        return result;
    }

    public String toXML() {
        StringBuilder buf = new StringBuilder();
        buf.append("<body");
        if (!TextUtils.isEmpty(len)) {
            buf.append(" len=\"").append(this.len).append("\"");
        }
        buf.append(">");
        if (!TextUtils.isEmpty(message)) {
            buf.append(message);
        }
        if (!TextUtils.isEmpty(this.smallpic)) {
            buf.append("<smallpic>").append(this.smallpic).append("</smallpic>");
        }
        if (!TextUtils.isEmpty(this.largepic)) {
            buf.append("<largepic>").append(this.largepic).append("</largepic>");
        }

        buf.append("</body>");
        return buf.toString();
    }
}
