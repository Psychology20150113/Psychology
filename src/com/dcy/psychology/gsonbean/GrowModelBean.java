package com.dcy.psychology.gsonbean;

import java.io.Serializable;
import java.util.ArrayList;

public class GrowModelBean implements Serializable{
	private int id;
	private String type;
	private String title;
	private String comment;
	private String mission;
	private int count;
	private String checkTitle;
	private ArrayList<String> checkItem;
	private int dailyMax;
	private ArrayList<String> missionTips;
	private ArrayList<String> missionDetail;
	
	public static final String Type_Link = "link";
	public static final String Type_Write = "write";
	public static final String Type_MutiMission = "mutiMission";
	public static final String Type_MutiWrite = "mutiWrite";
	public static final String Type_SingleMission = "singleMission";
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getMission() {
		return mission;
	}
	public void setMission(String mission) {
		this.mission = mission;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public String getCheckTitle() {
		return checkTitle;
	}
	public void setCheckTitle(String checkTitle) {
		this.checkTitle = checkTitle;
	}
	public ArrayList<String> getCheckItem() {
		return checkItem;
	}
	public void setCheckItem(ArrayList<String> checkItem) {
		this.checkItem = checkItem;
	}
	public int getDailyMax() {
		return dailyMax;
	}
	public void setDailyMax(int dailyMax) {
		this.dailyMax = dailyMax;
	}
	public ArrayList<String> getMissionTips() {
		return missionTips;
	}
	public void setMissionTips(ArrayList<String> missionTips) {
		this.missionTips = missionTips;
	}
	public ArrayList<String> getMissionDetail() {
		return missionDetail;
	}
	public void setMissionDetail(ArrayList<String> missionDetail) {
		this.missionDetail = missionDetail;
	}
}
