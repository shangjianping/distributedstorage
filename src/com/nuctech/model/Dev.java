package com.nuctech.model;

import javax.xml.bind.annotation.XmlElement;

public class Dev {
	
	private String devName;
	


	public String getDevName() {
		return devName;
	}
	@XmlElement(name = "devName")
	public void setDevName(String devName) {
		this.devName = devName;
	}

}
