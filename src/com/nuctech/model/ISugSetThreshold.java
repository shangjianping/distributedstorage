package com.nuctech.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "iSugSetThreshold")
public class ISugSetThreshold {
	private String result;



	
	public String getResult() {
		return result;
	}



	@XmlElement(name = "result")
	public void setResult(String result) {
		this.result = result;
	}

	
}
