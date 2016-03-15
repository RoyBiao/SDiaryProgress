package com.loovee.common.xmpp.core;

import android.content.Context;
import android.content.SharedPreferences;

public class SharepreferenceXMPPStore implements XMPPStore {
	private final static String SHAREPREFERENCES_NAME = "xmpp_user_info";
	private final static String SHAREPREFERENCES_USER_INFO_KEY = "user_info";
	private final static String SHAREPREFERENCES_LOGIN_INFO_KEY = "login_info";
	private SharedPreferences preferences;
	
	
	public SharepreferenceXMPPStore(Context context) {
		preferences = context.getSharedPreferences(SHAREPREFERENCES_NAME, Context.MODE_PRIVATE);
	}

	@Override
	public void clear() {
		preferences.edit().putString(SHAREPREFERENCES_USER_INFO_KEY, "").commit();
		preferences.edit().putString(SHAREPREFERENCES_LOGIN_INFO_KEY, "").commit();
	}

	@Override
	public void addUser(String user) {
		preferences.edit().putString(SHAREPREFERENCES_USER_INFO_KEY, user).commit();
	}

	@Override
	public String getUser() {
		return preferences.getString(SHAREPREFERENCES_USER_INFO_KEY, "");
	}

	@Override
	public void addLoginInf(String info) {
		preferences.edit().putString(SHAREPREFERENCES_LOGIN_INFO_KEY, info).commit();
	}

	@Override
	public String getLoginInfo() {
		return preferences.getString(SHAREPREFERENCES_LOGIN_INFO_KEY, "");
	}

}
