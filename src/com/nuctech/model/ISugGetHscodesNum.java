package com.nuctech.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "iSugGetHscodesNum")
public class ISugGetHscodesNum {
	private String result;
	private String hscodesNum;



	
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
