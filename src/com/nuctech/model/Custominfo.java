package com.nuctech.model;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;

public class Custominfo {
	
	private List<Entry>  entry;

	public List<Entry> getEntry() {
		return entry;
	}
	
	@XmlElement(name = "ENTRY")
	public void setEntry(List<Entry> entry) {
		this.entry = entry;
	}

	
	
	
	
}
