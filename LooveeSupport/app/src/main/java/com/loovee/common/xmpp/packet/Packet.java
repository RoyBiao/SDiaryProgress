package com.loovee.common.xmpp.packet;

import android.util.Base64;

import com.loovee.common.xmpp.annotation.db.Property;
import com.loovee.common.xmpp.annotation.db.Transient;
import com.loovee.common.xmpp.utils.StringUtils;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

public abstract class Packet {
	@Transient
	protected static final String DEFAULT_LANGUAGE = Locale.getDefault().getLanguage().toLowerCase(Locale.ENGLISH);

	@Transient
	private static String DEFAULT_XML_NS = null;
	@Transient
	public static final String ID_NOT_AVAILABLE = "ID_NOT_AVAILABLE";
	@Transient
	private static String prefix = StringUtils.randomString(5) + "-";

	private static long id = 0L;

	@Transient
	private String xmlns = DEFAULT_XML_NS;

	private String packetID = null;
	@Property(column = "_to" )
	private String to = null;
	@Property(column = "_from")
	private String from = null;
	
	/**
	 * 可扩展消息集合
	 */
	private final List<PacketExtension> packetExtensions = new CopyOnWriteArrayList<PacketExtension>();
	
	/**
	 * 消息属性集合
	 */
	private final Map<String, Object> properties = new HashMap<String, Object>();

	/**
	 * XMPP错误信息
	 */
	private XMPPError error = null;

	public static synchronized String nextID() {
		return prefix + Long.toString(id++);
	}

	public static void setDefaultXmlns(String defaultXmlns) {
		DEFAULT_XML_NS = defaultXmlns;
	}

	public String getPacketID() {
		if ("ID_NOT_AVAILABLE".equals(this.packetID)) {
			return null;
		}

		if (this.packetID == null) {
			this.packetID = nextID();
		}
		return this.packetID;
	}

	public void setPacketID(String packetID) {
		this.packetID = packetID;
	}

	public String getTo() {
		return this.to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getFrom() {
		return this.from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	/**
	 * 获取错误信息
	 * 
	 * @return
	 */
	public XMPPError getError() {
		return this.error;
	}

	public void setError(XMPPError error) {
		this.error = error;
	}

	public synchronized Collection<PacketExtension> getExtensions() {
		if (this.packetExtensions == null) {
			return Collections.emptyList();
		}
		return Collections
				.unmodifiableList(new ArrayList<PacketExtension>(this.packetExtensions));
	}

	public PacketExtension getExtension(String namespace) {
		return getExtension(null, namespace);
	}

	public PacketExtension getExtension(String elementName, String namespace) {
		if (namespace == null) {
			return null;
		}
		for (PacketExtension ext : this.packetExtensions) {
			if (((elementName == null) || (elementName.equals(ext
					.getElementName())))
					&& (namespace.equals(ext.getNamespace()))) {
				return ext;
			}
		}
		return null;
	}

	public void addExtension(PacketExtension extension) {
		this.packetExtensions.add(extension);
	}

	public void removeExtension(PacketExtension extension) {
		this.packetExtensions.remove(extension);
	}

	public synchronized Object getProperty(String name) {
		if (this.properties == null) {
			return null;
		}
		return this.properties.get(name);
	}

	public synchronized void setProperty(String name, Object value) {
		if (!(value instanceof Serializable)) {
			throw new IllegalArgumentException("Value must be serialiazble");
		}
		this.properties.put(name, value);
	}

	public synchronized void deleteProperty(String name) {
		if (this.properties == null) {
			return;
		}
		this.properties.remove(name);
	}

	public synchronized Collection<String> getPropertyNames() {
		if (this.properties == null) {
			return Collections.emptySet();
		}
		return Collections
				.unmodifiableSet(new HashSet<String>(this.properties.keySet()));
	}

	public abstract String toXML();

	/**
	 * 获取可扩展的XML
	 * @return
	 */
	protected synchronized String getExtensionsXML() {
		StringBuilder buf = new StringBuilder();

		for (PacketExtension extension : getExtensions()) {
			buf.append(extension.toXML());
		}

		if ((this.properties != null) && (!this.properties.isEmpty())) {
			buf.append("<properties xmlns=\"http://www.jivesoftware.com/xmlns/xmpp/properties\">");

			for (String name : getPropertyNames()) {
				Object value = getProperty(name);
				buf.append("<property>");
				buf.append("<name>").append(StringUtils.escapeForXML(name))
						.append("</name>");
				buf.append("<value type=\"");
				if ((value instanceof Integer)) {
					buf.append("integer\">").append(value).append("</value>");
				} else if ((value instanceof Long)) {
					buf.append("long\">").append(value).append("</value>");
				} else if ((value instanceof Float)) {
					buf.append("float\">").append(value).append("</value>");
				} else if ((value instanceof Double)) {
					buf.append("double\">").append(value).append("</value>");
				} else if ((value instanceof Boolean)) {
					buf.append("boolean\">").append(value).append("</value>");
				} else if ((value instanceof String)) {
					buf.append("string\">");
					buf.append(StringUtils.escapeForXML((String) value));
					buf.append("</value>");
				} else {
					ByteArrayOutputStream byteStream = null;
					ObjectOutputStream out = null;
					try {
						byteStream = new ByteArrayOutputStream();
						out = new ObjectOutputStream(byteStream);
						out.writeObject(value); //把值写到byteStream中
						buf.append("java-object\">");
						String encodedVal = Base64.encodeToString(byteStream.toByteArray(), Base64.DEFAULT);
						buf.append(encodedVal).append("</value>");
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						if (out != null) {
							try {
								out.close();
							} catch (Exception e) {
							}
						}
						if (byteStream != null) {
							try {
								byteStream.close();
							} catch (Exception e) {
							}
						}
					}
				}
				buf.append("</property>");
			}
			buf.append("</properties>");
		}
		return buf.toString();
	}

	public String getXmlns() {
		return this.xmlns;
	}

	protected static String parseXMLLang(String language) {
		if ((language == null) || ("".equals(language))) {
			language = DEFAULT_LANGUAGE;
		}
		return language;
	}

	protected static String getDefaultLanguage() {
		return DEFAULT_LANGUAGE;
	}

	public boolean equals(Object o) {
		if (this == o)
			return true;
		if ((o == null) || (getClass() != o.getClass()))
			return false;

		Packet packet = (Packet) o;

		if (this.error != null ? !this.error.equals(packet.error)
				: packet.error != null)
			return false;
		if (this.from != null ? !this.from.equals(packet.from)
				: packet.from != null)
			return false;
		if (!this.packetExtensions.equals(packet.packetExtensions))
			return false;
		if (this.packetID != null ? !this.packetID.equals(packet.packetID)
				: packet.packetID != null) {
			return false;
		}
		if (this.properties != null ? !this.properties
				.equals(packet.properties) : packet.properties != null) {
			return false;
		}
		if (this.to != null ? !this.to.equals(packet.to) : packet.to != null)
			return false;
		return this.xmlns != null ? this.xmlns.equals(packet.xmlns)
				: packet.xmlns == null;
	}

	public int hashCode() {
		int result = this.xmlns != null ? this.xmlns.hashCode() : 0;
		result = 31 * result
				+ (this.packetID != null ? this.packetID.hashCode() : 0);
		result = 31 * result + (this.to != null ? this.to.hashCode() : 0);
		result = 31 * result + (this.from != null ? this.from.hashCode() : 0);
		result = 31 * result + this.packetExtensions.hashCode();
		result = 31 * result + this.properties.hashCode();
		result = 31 * result + (this.error != null ? this.error.hashCode() : 0);
		return result;
	}

}
