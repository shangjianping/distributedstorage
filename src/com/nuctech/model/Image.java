package com.nuctech.model;

import javax.xml.bind.annotation.XmlElement;

public class Image {
	private String path;
	private String type;
	public String getPath() {
		return path;
	}
	@XmlElement(name = "PATH") 
	public void setPath(String path) {
		this.path = path;
	}
	
	public String getType() {
		return type;
	}
	@XmlElement(name = "IDR_IMAGE") 
	public void setType(String type) {
		this.type = type;
	}
	
}

