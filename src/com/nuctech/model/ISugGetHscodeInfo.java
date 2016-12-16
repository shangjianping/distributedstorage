package com.nuctech.model;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "iSugGetHscodeInfo")
public class ISugGetHscodeInfo {
	private String result;
	private String threShold;
	private String imageNum;
	private List<String> imageName;


	
	


	public String getThreShold() {
		return threShold;
	}


	@XmlElement(name = "threShold")
	public void setThreShold(String threShold) {
		this.threShold = threShold;
	}



	public String getImageNum() {
		return imageNum;
	}


	@XmlElement(name = "imageNum")
	public void setImageNum(String imageNum) {
		this.imageNum = imageNum;
	}



	public List<String> getImageName() {
		return imageName;
	}


	@XmlElement(name = "imageName")
	public void setImageName(List<String> imageName) {
		this.imageName = imageName;
	}



	public String getResult() {
		return result;
	}



	@XmlElement(name = "result")
	public void setResult(String result) {
		this.result = result;
	}




	

	
}
