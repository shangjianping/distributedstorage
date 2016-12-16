package com.nuctech.model;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement(name = "iLiqDetect")
public class ILiquorDetect {
	private String result;
	private String path;
	private String areaNum;
	private List<DetectArea> detectArea;
	private String detectAreaNum;


	public String getDetectAreaNum() {
		return detectAreaNum;
	}
	@XmlTransient
	public void setDetectAreaNum(String detectAreaNum) {
		this.detectAreaNum = detectAreaNum;
	}

	public List<DetectArea> getDetectArea() {
		return detectArea;
	}
	
	@XmlElement(name = "area")
	public void setDetectArea(List<DetectArea> detectArea) {
		this.detectArea = detectArea;
	}

	public String getResult() {
		return result;
	}

	@XmlElement(name = "result")
	public void setResult(String result) {
		this.result = result;
	}


	public String getPath() {
		return path;
	}

	@XmlElement(name = "path")
	public void setPath(String path) {
		this.path = path;
	}


	public String getAreaNum() {
		return areaNum;
	}

	@XmlElement(name = "areaNum")
	public void setAreaNum(String areaNum) {
		this.areaNum = areaNum;
	}
	
}
