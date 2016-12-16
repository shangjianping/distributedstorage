package com.nuctech.model;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "imageList")
public class ImageList {
	private List<String> imageId;

	public List<String> getImageId() {
		return imageId;
	}
	@XmlElement(name = "imageId")
	public void setImageId(List<String> imageId) {
		this.imageId = imageId;
	}
}
