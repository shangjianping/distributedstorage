package com.nuctech.model;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "iSugGetDevNames")
public class ISugGetDevNames {
	private String result;
	private String devNum;
	private List<Dev> dev;
	


	
	public String getResult() {
		return result;
	}



	@XmlElement(name = "result")
	public void setResult(String result) {
		this.result = result;
	}




	public String getDevNum() {
		return devNum;
	}



	@XmlElement(name = "devNum")
	public void setDevNum(String devNum) {
		this.devNum = devNum;
	}




	public List<Dev> getDev() {
		return dev;
	}


	
	@XmlElement(name = "dev")
	public void setDev(List<Dev> dev) {
		this.dev = dev;
	}


}
