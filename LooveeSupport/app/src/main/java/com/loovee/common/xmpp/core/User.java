package com.loovee.common.xmpp.core;

import java.io.Serializable;

public class User implements Serializable{
	public String username;
	public String token;
	public String jid;
	public boolean forceprefectinfo;
	public String sex;
	private int vipversion;
	/**
	 * 礼物版本
	 */
	private int giftversion;
	/**
	 * 发现页配置版本
	 */
	private int searchpageversion;
	/**
	 * 包房主题版本
	 */
	private int roomthemeversion;
	/**
	 * 音乐主题配置版本
	 */
	private int musicthemeversion;
	/**
	 * 表情版本版本
	 */
	private int faceversion;

	/**
	 * 道具版本
	 */
	private int propsversion;


	public int getPropsversion() {
		return propsversion;
	}

	public void setPropsversion(int propsversion) {
		this.propsversion = propsversion;
	}

	public int getFaceversion() {
		return faceversion;
	}

	public void setFaceversion(int faceversion) {
		this.faceversion = faceversion;
	}

	public int getGiftversion() {
		return giftversion;
	}

	public void setGiftversion(int giftversion) {
		this.giftversion = giftversion;
	}

	public int getMusicthemeversion() {
		return musicthemeversion;
	}

	public void setMusicthemeversion(int musicthemeversion) {
		this.musicthemeversion = musicthemeversion;
	}

	public int getRoomthemeversion() {
		return roomthemeversion;
	}

	public void setRoomthemeversion(int roomthemeversion) {
		this.roomthemeversion = roomthemeversion;
	}

	public int getSearchpageversion() {
		return searchpageversion;
	}

	public void setSearchpageversion(int searchpageversion) {
		this.searchpageversion = searchpageversion;
	}

	public int getVipversion() {
		return vipversion;
	}

	public void setVipversion(int vipversion) {
		this.vipversion = vipversion;
	}

	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getJid() {
		return jid;
	}
	public void setJid(String jid) {
		this.jid = jid;
	}
	public boolean isForceprefectinfo() {
		return forceprefectinfo;
	}
	public void setForceprefectinfo(boolean forceprefectinfo) {
		this.forceprefectinfo = forceprefectinfo;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}

}
