package com.dcy.psychology.model;

import java.util.ArrayList;

import com.dcy.psychology.gsonbean.SpecificUserBean;

import android.R.string;

public class MessageModel {
	public String MessageType="通知";
	public String MessageAchieve="最近约聊的导师已有反馈";
	public String MessageDetails;
	public String SpecificUserHeadUrl;
	
	public void setSpecificUserHeadUrl(SpecificUserBean item) 
	{
		this.SpecificUserHeadUrl=item.SpecificUserHeadUrl;
	}
	public void setMessageDetails(SpecificUserBean item) 
	{
		this.MessageDetails=item.SpecificUserAchievement;
	}
	

}
