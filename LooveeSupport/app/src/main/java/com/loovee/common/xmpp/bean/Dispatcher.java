package com.loovee.common.xmpp.bean;

import com.loovee.common.xmpp.annotation.XMLElement;

public class Dispatcher {
	@XMLElement
	private Imserver imserver;
	@XMLElement
	private Pocserver pocserver;
	@XMLElement
	private Mediaserver mediaserver;
	@XMLElement
	private Extendcharge extendcharge;
	@XMLElement
	private Imupload imupload;
	@XMLElement
	private Version version;
	@XMLElement
	private Unioncharge unioncharge;
	
	
	
	
	public Imserver getImserver() {
		return imserver;
	}
	public void setImserver(Imserver imserver) {
		this.imserver = imserver;
	}
	public Pocserver getPocserver() {
		return pocserver;
	}
	public void setPocserver(Pocserver pocserver) {
		this.pocserver = pocserver;
	}
	public Mediaserver getMediaserver() {
		return mediaserver;
	}
	public void setMediaserver(Mediaserver mediaserver) {
		this.mediaserver = mediaserver;
	}
	public Extendcharge getExtendcharge() {
		return extendcharge;
	}
	public void setExtendcharge(Extendcharge extendcharge) {
		this.extendcharge = extendcharge;
	}
	public Imupload getImupload() {
		return imupload;
	}
	public void setImupload(Imupload imupload) {
		this.imupload = imupload;
	}
	public Version getVersion() {
		return version;
	}
	public void setVersion(Version version) {
		this.version = version;
	}
	public Unioncharge getUnioncharge() {
		return unioncharge;
	}
	public void setUnioncharge(Unioncharge unioncharge) {
		this.unioncharge = unioncharge;
	}
	
}
