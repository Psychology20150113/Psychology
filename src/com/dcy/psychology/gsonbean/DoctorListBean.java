package com.dcy.psychology.gsonbean;

public class DoctorListBean {
	private long DoctorID;
	private String DoctorName;
	private String DoctorHeadUrl;
	
	public long getDoctorID() {
		return DoctorID;
	}
	public void setDoctorID(long doctorID) {
		DoctorID = doctorID;
	}
	public String getDoctorName() {
		return DoctorName;
	}
	public void setDoctorName(String doctorName) {
		DoctorName = doctorName;
	}
	public String getDoctorHeadUrl() {
		return DoctorHeadUrl;
	}
	public void setDoctorHeadUrl(String doctorHeadUrl) {
		DoctorHeadUrl = doctorHeadUrl;
	}
}
