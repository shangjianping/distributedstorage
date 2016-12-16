package com.nuctech.model;

import javax.xml.bind.annotation.XmlElement;

public class EntryItems {
	private String name;
	private String weight;
	private String packageNumber;
	private String packageType;
	private String hscode;
	
	public String getName() {
		return name;
	}
	@XmlElement(name = "NAME") 
	public void setName(String name) {
		this.name = name;
	}
	public String getWeight() {
		return weight;
	}
	@XmlElement(name = "WEIGHT") 
	public void setWeight(String weight) {
		this.weight = weight;
	}
	public String getPackageNumber() {
		return packageNumber;
	}
	@XmlElement(name = "PACKAGE_NUMBER") 
	public void setPackageNumber(String packageNumber) {
		this.packageNumber = packageNumber;
	}
	public String getPackageType() {
		return packageType;
	}
	@XmlElement(name = "PACKAGE_TYPE") 
	public void setPackageType(String packageType) {
		this.packageType = packageType;
	}
	public String getHscode() {
		return hscode;
	}
	@XmlElement(name = "HSCODE") 
	public void setHscode(String hscode) {
		this.hscode = hscode;
	}
	
	
}
