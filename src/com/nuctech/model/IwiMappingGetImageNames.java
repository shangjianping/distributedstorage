package com.nuctech.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "iwiMappingGetImageNames")
public class IwiMappingGetImageNames {
	private String result;
	private String imageNum;
	private ImageList imageList;

	public String getImageNum() {
		return imageNum;
	}
	@XmlElement(name = "imageNum")
	public void setImageNum(String imageNum) {
		this.imageNum = imageNum;
	}
	public ImageList getImageList() {
		return imageList;
	}
	@XmlElement(name = "imageList")
	public void setImageList(ImageList imageList) {
		this.imageList = imageList;
	}
	public String getResult() {
		return result;
	}
	@XmlElement(name = "result")
	public void setResult(String result) {
		this.result = result;
	}

}
