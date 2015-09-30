package com.dcy.psychology.gsonbean;

import java.io.Serializable;
import java.util.ArrayList;

public class ApplyInfoBean implements Serializable{
	public long id;
	public String doctorphone;
	public String userphone;
	public String username;
	public String userheadurl;
	public String usersex;
	public int userage;
	public String useremail;
	public String constellation;
	public String hometownp;
	public String hometownc;
	public String university;
	public String diploma;
	public String major;
	public int graduationyear;
	public String currentstate;
	public String workingcity;
	public String industry;
	public ArrayList<TimeClass> time;
	public int state;
	public String UserAchievement;
	
	public class TimeClass implements Serializable{
		public String starttime;
		public String endtime;
	}
}
