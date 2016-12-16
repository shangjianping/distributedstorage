package com.nuctech.model;

import javax.xml.bind.annotation.XmlElement;

public class Pic {

	private String similarity;
	private String imageId;
	
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
