package com.nuctech.model;

import javax.xml.bind.annotation.XmlElement;

public class DetectArea {
	private Rect rect;
	private String color;
	private String credibility;
	
	
	public Rect getRect() {
		return rect;
	}

	@XmlElement(name = "rect")
	public void setRect(Rect rect) {
		this.rect = rect;
	}


	public String getColor() {
		return color;
	}

	@XmlElement(name = "color")
	public void setColor(String color) {
		this.color = color;
	}


	public String getCredibility() {
		return credibility;
	}

	@XmlElement(name = "credibility")
	public void setCredibility(String credibility) {
		this.credibility = credibility;
	}

	
}
