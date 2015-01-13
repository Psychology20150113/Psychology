package com.dcy.psychology.gsonbean;

public class CommentBean {
	private int HeartWeiBoID;
	private String UserLoginName;
	private String HeartWeiBo;
	private String PhotoUrl;
	private String CreatDate;
	private int Like;
	private int CommentCount;
	
	public int getHeartWeiBoID() {
		return HeartWeiBoID;
	}
	public void setHeartWeiBoID(int heartWeiBoID) {
		HeartWeiBoID = heartWeiBoID;
	}
	public String getUserLoginName() {
		return UserLoginName;
	}
	public void setUserLoginName(String userLoginName) {
		UserLoginName = userLoginName;
	}
	public String getHeartWeiBo() {
		return HeartWeiBo;
	}
	public void setHeartWeiBo(String heartWeiBo) {
		HeartWeiBo = heartWeiBo;
	}
	public String getPhotoUrl() {
		return PhotoUrl;
	}
	public void setPhotoUrl(String photoUrl) {
		PhotoUrl = photoUrl;
	}
	public String getCreatDate() {
		return CreatDate;
	}
	public void setCreatDate(String creatDate) {
		CreatDate = creatDate;
	}
	public int getLike() {
		return Like;
	}
	public void setLike(int like) {
		Like = like;
	}
	public int getCommentCount() {
		return CommentCount;
	}
	public void setCommentCount(int commentCount) {
		CommentCount = commentCount;
	}
}
