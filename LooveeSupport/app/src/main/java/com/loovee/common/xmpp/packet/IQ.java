package com.loovee.common.xmpp.packet;

import com.loovee.common.xmpp.utils.StringUtils;

import java.util.Locale;

public abstract class IQ extends Packet {
	
  private Type type;
  private String queryXmlns = null;

  public IQ()
  {
    this.type = Type.GET;
  }

  public Type getType()
  {
    return this.type;
  }

  public void setType(Type type)
  {
    if (type == null) {
      this.type = Type.GET;
    }
    else
      this.type = type;
  }

  public String toXML()
  {
    StringBuilder buf = new StringBuilder();
    buf.append("<iq ");
    if (getPacketID() != null) {
      buf.append("id=\"" + getPacketID() + "\" ");
    }
    if (getTo() != null) {
      buf.append("to=\"").append(StringUtils.escapeForXML(getTo())).append("\" ");
    }
    if (getFrom() != null) {
      buf.append("from=\"").append(StringUtils.escapeForXML(getFrom())).append("\" ");
    }
    
    if (this.type == null) {
      buf.append("type=\"get\">");
    }else {
      buf.append("type=\"").append(getType()).append("\">");
    }

    String queryXML = getChildElementXML();
    if (queryXML != null) {
      buf.append(queryXML);
    }

    XMPPError error = getError();
    if (error != null) {
      buf.append(error.toXML());
    }
    buf.append("</iq>");
    return buf.toString();
  }

  /**
   * 写入子XML参数
   * @return
   */
  public abstract String getChildElementXML();

  public static class Type
  {
    public static final Type GET = new Type("get");
    public static final Type SET = new Type("set");
    public static final Type RESULT = new Type("result");
    public static final Type ERROR = new Type("error");
    private String value;

    public static Type fromString(String type)
    {
      if (type == null) {
        return null;
      }
      type = type.toLowerCase(Locale.ENGLISH);
      if (GET.toString().equals(type)) {
        return GET;
      }
      if (SET.toString().equals(type)) {
        return SET;
      }
      if (ERROR.toString().equals(type)) {
        return ERROR;
      }
      if (RESULT.toString().equals(type)) {
        return RESULT;
      }

      return null;
    }

    private Type(String value)
    {
      this.value = value;
    }

    public String toString() {
      return this.value;
    }
  }

public String getQueryXmlns() {
	return queryXmlns;
}

public void setQueryXmlns(String queryXmlns) {
	this.queryXmlns = queryXmlns;
}
}