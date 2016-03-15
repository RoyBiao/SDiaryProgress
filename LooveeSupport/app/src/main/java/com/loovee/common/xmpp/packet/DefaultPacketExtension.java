package com.loovee.common.xmpp.packet;


import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class DefaultPacketExtension
  implements PacketExtension
{
  private String elementName;
  private String namespace;
  private Map<String, String> map;

  public DefaultPacketExtension(String elementName, String namespace)
  {
    this.elementName = elementName;
    this.namespace = namespace;
  }

  public String getElementName()
  {
    return this.elementName;
  }

  public String getNamespace()
  {
    return this.namespace;
  }

  public String toXML() {
    StringBuilder buf = new StringBuilder();
    buf.append("<").append(this.elementName).append(" xmlns=\"").append(this.namespace).append("\">");
    for (String name : getNames()) {
      String value = getValue(name);
      buf.append("<").append(name).append(">");
      buf.append(value);
      buf.append("</").append(name).append(">");
    }
    buf.append("</").append(this.elementName).append(">");
    return buf.toString();
  }

  public synchronized Collection<String> getNames()
  {
    if (this.map == null) {
      return Collections.emptySet();
    }
    return Collections.unmodifiableSet(new HashMap<String,String>(this.map).keySet());
  }

  public synchronized String getValue(String name)
  {
    if (this.map == null) {
      return null;
    }
    return (String)this.map.get(name);
  }

  public synchronized void setValue(String name, String value)
  {
    if (this.map == null) {
      this.map = new HashMap();
    }
    this.map.put(name, value);
  }
}
