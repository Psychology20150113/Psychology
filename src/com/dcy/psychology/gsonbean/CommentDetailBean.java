package com.dcy.psychology.gsonbean;

public class CommentDetailBean {
	private int ReviewHID;
	private int HeartWeiBoID;
	private String ReviewUserLoginName;
	private String ReviewContent;
	private String ReviewDate;
	private int ReadState;
	
	public int getReviewHID() {
		return ReviewHID;
	}
	public void setReviewHID(int reviewHID) {
		ReviewHID = reviewHID;
	}
	public int getHeartWeiBoID() {
		return HeartWeiBoID;
	}
	public void setHeartWeiBoID(int heartWeiBoID) {
		HeartWeiBoID = heartWeiBoID;
	}
	public String getReviewUserLoginName() {
		return ReviewUserLoginName;
	}
	public void setReviewUserLoginName(String reviewUserLoginName) {
		ReviewUserLoginName = reviewUserLoginName;
	}
	public String getReviewContent() {
		return ReviewContent;
	}
	public void setReviewContent(String reviewContent) {
		ReviewContent = reviewContent;
	}
	public String getReviewDate() {
		return ReviewDate;
	}
	public void setReviewDate(String reviewDate) {
		ReviewDate = reviewDate;
	}
	public int getReadState() {
		return ReadState;
	}
	public void setReadState(int readState) {
		ReadState = readState;
	}
}
