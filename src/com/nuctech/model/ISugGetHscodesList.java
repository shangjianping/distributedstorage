package com.nuctech.model;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "iSugGetHscodesList")
public class ISugGetHscodesList {
	private String result;
	private String hscodesNum;
	private List<String> hscode;


	
	public List<String> getHscode() {
		return hscode;
	}


	@XmlElement(name = "hscode")
	public void setHscode(List<String> hscode) {
		this.hscode = hscode;
	}



	public String getResult() {
		return result;
	}



	@XmlElement(name = "result")
	public void setResult(String result) {
		this.result = result;
	}




	public String getHscodesNum() {
		return hscodesNum;
	}



	@XmlElement(name = "hscodesNum")
	public void setHscodesNum(String hscodesNum) {
		this.hscodesNum = hscodesNum;
	}

	
}
