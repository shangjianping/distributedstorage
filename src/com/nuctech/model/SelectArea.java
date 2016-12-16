package com.nuctech.model;

import javax.xml.bind.annotation.XmlElement;

public class SelectArea {
	private String left;
	private String top;
	private String bottom;
	private String right;
	
	public String getLeft() {
		return left;
	}
	
	@XmlElement(name = "LEFT") 
	public void setLeft(String left) {
		this.left = left;
	}
	public String getTop() {
		return top;
	}
	
	@XmlElement(name = "TOP") 
	public void setTop(String top) {
		this.top = top;
	}
	public String getBottom() {
		return bottom;
	}
	@XmlElement(name = "BOTTOM") 
	public void setBottom(String bottom) {
		this.bottom = bottom;
	}
	public String getRight() {
		return right;
	}
	@XmlElement(name = "RIGHT") 
	public void setRight(String right) {
		this.right = right;
	}
}
