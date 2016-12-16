package com.nuctech.model;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;

public class Image_List {
	private List<String> imageId;

	public List<String> getImageId() {
		return imageId;
	}
	@XmlElement(name = "IMAGEID")
	public void setImageId(List<String> imageId) {
		this.imageId = imageId;
	}
}
