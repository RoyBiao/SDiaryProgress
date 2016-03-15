package com.loovee.common.xmpp.packet;


import com.loovee.common.xmpp.utils.StringUtils;

/**
 * 出席实例
 * @author Jesse
 *
 */
public class Presence extends Packet
{
  private Type type = Type.available;
  private String status = null;
  private int priority = -2147483648;
  private Mode mode = null;
  private String language;

  public Presence(Type type)
  {
    setType(type);
  }

  public Presence(Type type, String status, int priority, Mode mode)
  {
    setType(type);
    setStatus(status);
    setPriority(priority);
    setMode(mode);
  }

  public boolean isAvailable()
  {
    return this.type == Type.available;
  }

  public boolean isAway()
  {
    return (this.type == Type.available) && ((this.mode == Mode.away) || (this.mode == Mode.xa) || (this.mode == Mode.dnd));
  }

  public Type getType()
  {
    return this.type;
  }

  public void setType(Type type)
  {
    if (type == null) {
      throw new NullPointerException("Type cannot be null");
    }
    this.type = type;
  }

  public String getStatus()
  {
    return this.status;
  }

  public void setStatus(String status)
  {
    this.status = status;
  }

  public int getPriority()
  {
    return this.priority;
  }

  public void setPriority(int priority)
  {
    if ((priority < -128) || (priority > 128)) {
      throw new IllegalArgumentException("Priority value " + priority + " is not valid. Valid range is -128 through 128.");
    }

    this.priority = priority;
  }

  public Mode getMode()
  {
    return this.mode;
  }

  public void setMode(Mode mode)
  {
    this.mode = mode;
  }

  private String getLanguage()
  {
    return this.language;
  }

  public void setLanguage(String language)
  {
    this.language = language;
  }

  public String toXML() {
    StringBuilder buf = new StringBuilder();
    buf.append("<presence");
    if (getXmlns() != null) {
      buf.append(" xmlns=\"").append(getXmlns()).append("\"");
    }
    if (this.language != null) {
      buf.append(" xml:lang=\"").append(getLanguage()).append("\"");
    }
    if (getPacketID() != null) {
      buf.append(" id=\"").append(getPacketID()).append("\"");
    }
    if (getTo() != null) {
      buf.append(" to=\"").append(StringUtils.escapeForXML(getTo())).append("\"");
    }
    if (getFrom() != null) {
      buf.append(" from=\"").append(StringUtils.escapeForXML(getFrom())).append("\"");
    }
    if (this.type != Type.available) {
      buf.append(" type=\"").append(this.type).append("\"");
    }
    buf.append(">");
    
    if (this.status != null) {
      buf.append("<status>").append(StringUtils.escapeForXML(this.status)).append("</status>");
    }
    if (this.priority != -2147483648) {
      buf.append("<priority>").append(this.priority).append("</priority>");
    }
    if ((this.mode != null) && (this.mode != Mode.available)) {
      buf.append("<show>").append(this.mode).append("</show>");
    }

    String extensionXML = getExtensionsXML();
    if(extensionXML != null) {
    	  buf.append(extensionXML);
    }
    XMPPError error = getError();
    if (error != null) {
      buf.append(error.toXML());
    }
    buf.append("</presence>");

    return buf.toString();
  }
  
  

  public String toString() {
    StringBuilder buf = new StringBuilder();
    buf.append(this.type);
    if (this.mode != null) {
      buf.append(": ").append(this.mode);
    }
    if (getStatus() != null) {
      buf.append(" (").append(getStatus()).append(")");
    }
    return buf.toString();
  }

  public static enum Mode
  {
    chat, 

    available, 

    away, 

    xa, 

    dnd;
  }

  public static enum Type
  {
    available, 

    unavailable, 

    subscribe, 

    subscribed, 

    unsubscribe, 

    unsubscribed, 

    error;
  }
}
