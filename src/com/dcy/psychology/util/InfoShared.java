package com.dcy.psychology.util;

import android.content.Context;
import android.content.SharedPreferences;

public class InfoShared {
	private final String InfoSharedName = "app_info_1.0";
	private SharedPreferences mShared;
	
	private final String UserName = "info_name";
	private final String UserPwd = "info_pwd";
	private final String UserNick = "info_nick";
	private final String UserRole = "info_role";
	
	public String ThemeFormat = "Theme%s_%d";
	
	public InfoShared(Context mContext) {
		mShared = mContext.getSharedPreferences(InfoSharedName, Context.MODE_PRIVATE);
	}
	
	public void setUserName(String userName){
		mShared.edit().putString(UserName, userName).commit();
	}
	
	public String getUserName(){
		return mShared.getString(UserName, "");
	}
	
	public void setUserPwd(String pwd){
		mShared.edit().putString(UserPwd, pwd).commit();
	}
	
	public String getUserPwd(){
		return mShared.getString(UserPwd, "");
	}
	
	public void setUserNick(String nick){
		mShared.edit().putString(UserNick, nick).commit();
	}
	
	public String getUserNick(){
		return mShared.getString(UserNick, "");
	}
	
	public void setUserRole(String role){
		mShared.edit().putString(UserRole, role).commit();
	}
	
	public String getUserRole(){
		return mShared.getString(UserRole, "");
	}
	
	public void putInt(String key , int value){
		mShared.edit().putInt(key, value).commit();
	}
	
	public int getInt(String key){
		return mShared.getInt(key, 0);
	}
	
	public void clearInfo(){
		mShared.edit().clear().commit();
	}
}
