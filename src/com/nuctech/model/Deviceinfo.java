package com.nuctech.model;

import javax.xml.bind.annotation.XmlElement;

public class Deviceinfo {
	private String deviceno ;
	private String hscode;
	private String threshold;
	private String samplename;
	private String energy;
	private String scandirection;
	private String pixelpermeterx;
	private String pixelpermetery;
	
	
	public String getDeviceno() {
		return deviceno;
	}
	
	@XmlElement(name = "DEVICENO") 
	public void setDeviceno(String deviceno) {
		this.deviceno = deviceno;
	}
	
	public String getHscode() {
		return hscode;
	}
	
	@XmlElement(name = "HSCODE") 
	public void setHscode(String hscode) {
		this.hscode = hscode;
	}
	
	public String getThreshold() {
		return threshold;
	}
	
	@XmlElement(name = "THRESHOLD") 
	public void setThreshold(String threshold) {
		this.threshold = threshold;
	}
	
	public String getSamplename() {
		return samplename;
	}
	
	@XmlElement(name = "SAMPLENAME") 
	public void setSamplename(String samplename) {
		this.samplename = samplename;
	}
	
	public String getEnergy() {
		return energy;
	}
	
	@XmlElement(name = "ENERGY") 
	public void setEnergy(String energy) {
		this.energy = energy;
	}
	
	public String getScandirection() {
		return scandirection;
	}
	
	@XmlElement(name = "SCAN_DIRECTION") 
	public void setScandirection(String scandirection) {
		this.scandirection = scandirection;
	}
	
	public String getPixelpermeterx() {
		return pixelpermeterx;
	}
	
	@XmlElement(name = "PIXEL_PER_METER_X") 
	public void setPixelpermeterx(String pixelpermeterx) {
		this.pixelpermeterx = pixelpermeterx;
	}
	
	public String getPixelpermetery() {
		return pixelpermetery;
	}
	
	@XmlElement(name = "PIXEL_PER_METER_Y") 
	public void setPixelpermetery(String pixelpermetery) {
		this.pixelpermetery = pixelpermetery;
	}
	
	

	
	
}
