package com.nuctech.model;

public class SuspectImage {
	private String id;
	private byte[] jpg;
	private byte[] sug;
	private byte[] waste;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public byte[] getJpg() {
		return jpg;
	}
	public void setJpg(byte[] jpg) {
		this.jpg = jpg;
	}
	public byte[] getSug() {
		return sug;
	}
	public void setSug(byte[] sug) {
		this.sug = sug;
	}
	public byte[] getWaste() {
		return waste;
	}
	public void setWaste(byte[] waste) {
		this.waste = waste;
	}
	

}
