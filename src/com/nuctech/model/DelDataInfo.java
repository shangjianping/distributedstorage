package com.nuctech.model;

import javax.xml.bind.annotation.XmlElement;

public class DelDataInfo {
	private String number;
	private Image_List imageList;
	public String getNumber() {
		return number;
	}
	@XmlElement(name = "IMAGE_NUMBER")
	public void setNumber(String number) {
		this.number = number;
	}
	public Image_List getImageList() {
		return imageList;
	}
	@XmlElement(name = "IMAGE_LIST")
	public void setImageList(Image_List imageList) {
		this.imageList = imageList;
	}
}
