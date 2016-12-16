package com.nuctech.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "iWasteDetect")
public class IWasteDetect {
	private String result;
	private String path;
	private String metal;
	private String paper;
	private String plastic;
	private String noMPP;
	
	public String getPath() {
		return path;
	}


	@XmlElement(name = "path")
	public void setPath(String path) {
		this.path = path;
	}



	public String getMetal() {
		return metal;
	}


	@XmlElement(name = "metal")
	public void setMetal(String metal) {
		this.metal = metal;
	}



	public String getPaper() {
		return paper;
	}


	@XmlElement(name = "paper")
	public void setPaper(String paper) {
		this.paper = paper;
	}



	public String getPlastic() {
		return plastic;
	}


	@XmlElement(name = "plastic")
	public void setPlastic(String plastic) {
		this.plastic = plastic;
	}



	public String getNoMPP() {
		return noMPP;
	}


	@XmlElement(name = "noMPP")
	public void setNoMPP(String noMPP) {
		this.noMPP = noMPP;
	}


	public String getResult() {
		return result;
	}



	@XmlElement(name = "result")
	public void setResult(String result) {
		this.result = result;
	}

	
}
