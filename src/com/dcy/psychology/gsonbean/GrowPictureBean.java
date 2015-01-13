package com.dcy.psychology.gsonbean;

import java.io.Serializable;

public class GrowPictureBean implements Serializable{
	private int id;
	private String title;
	private String picture;
	private String content;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getPicture() {
		return picture;
	}
	public void setPicture(String picture) {
		this.picture = picture;
	}
	public String getContent() {
		return content;
	}
	public void setCintent(String cintent) {
		this.content = cintent;
	}
}
