package com.nuctech.model;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "iSearchPic")
public class ISearchPic {
	private String picNum;
	private List<Pic> pic;

	public String getPicNum() {
		return picNum;
	}
	@XmlElement(name = "picNum")
	public void setPicNum(String picNum) {
		this.picNum = picNum;
	}

	public List<Pic> getPic() {
		return pic;
	}
	@XmlElement(name = "pic")
	public void setPic(List<Pic> pic) {
		this.pic = pic;
	}


	
}
