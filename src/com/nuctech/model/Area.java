package com.nuctech.model;

import javax.xml.bind.annotation.XmlElement;

public class Area {
	private String no;
	private String color;
	private String hscode;
	private String similarity;
	private String imageId;
	
	public String getNo() {
		return no;
	}
	
	@XmlElement(name = "no")
	public void setNo(String no) {
		this.no = no;
	}
	public String getColor() {
		return color;
	}
	
	@XmlElement(name = "color")
	public void setColor(String color) {
		this.color = color;
	}
	public String getHscode() {
		return hscode;
	}
	
	@XmlElement(name = "hscode")
	public void setHscode(String hscode) {
		this.hscode = hscode;
	}
	public String getSimilarity() {
		return similarity;
	}
	
	@XmlElement(name = "similarity")
	public void setSimilarity(String similarity) {
		this.similarity = similarity;
	}
	public String getImageId() {
		return imageId;
	}
	
	@XmlElement(name = "imageId")
	public void setImageId(String imageId) {
		this.imageId = imageId;
	}
	
}
