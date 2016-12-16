package com.nuctech.model;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement(name = "iSuggest")
public class ISuggest {
	private String path;
	private List<Area> area;
	private String areaNum;
	public String getAreaNum() {
		return areaNum;
	}
	@XmlTransient
	public void setAreaNum(String areaNum) {
		this.areaNum = areaNum;
	}
	public List<Area> getArea() {
		return area;
	}
	@XmlElement(name = "area")
	public void setArea(List<Area> area) {
		this.area = area;
	}
	public String getPath() {
		return path;
	}
	@XmlElement(name = "path")
	public void setPath(String path) {
		this.path = path;
	}

	
}
