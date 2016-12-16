package com.nuctech.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "iSugIni")
public class ISugIni {
	private String result;
	private String globalModel;
	
	public String getResult() {
		return result;
	}
	@XmlElement(name = "result")
	public void setResult(String result) {
		this.result = result;
	}
	public String getGlobalModel() {
		return globalModel;
	}
	@XmlElement(name = "globalModel")
	public void setGlobalModel(String globalModel) {
		this.globalModel = globalModel;
	}

}
