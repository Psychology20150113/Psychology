package com.dcy.psychology.gsonbean;

import java.io.Serializable;

public class SpecificUserBean implements Serializable{
	public long SpecificUserID;
	public String SpecificUserName;
	public String SpecificUserHeadUrl;
	public String SpecificUserAchievement;
	public String SpecificUserLifeMotto;
	public String SpecificUserPhone;
	public String SpecificUserHollendTest;
	public String SpecificUserTemperamentTest;
	public boolean IsFollow;
	public float MatchResult;
	public int applyState = -1;
	
	public static SpecificUserBean valueOf(ApplyInfoBean itemBean){
		SpecificUserBean item = new SpecificUserBean();
		item.SpecificUserID = itemBean.id;
		item.SpecificUserName = itemBean.username;
		item.SpecificUserHeadUrl = itemBean.userheadurl;
		item.SpecificUserAchievement = itemBean.UserAchievement;
		item.SpecificUserPhone = itemBean.doctorphone;
		item.applyState = itemBean.state;
		return item;
	}
}
