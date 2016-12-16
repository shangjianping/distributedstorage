package com.nuctech.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement(name = "ARGS")
public class Args {
	private String globalModel;
	private ImageInfo imageInfo;
	private Custominfo custominfo;
	private Deviceinfo deviceinfo;
	private Meta meta;
	private DelDataInfo delDataInfo;
	private String wasteModePath;
	public String getWasteModePath() {
		return wasteModePath;
	}
	@XmlElement(name = "WASTEMODELDATA")
	public void setWasteModePath(String wasteModePath) {
		this.wasteModePath = wasteModePath;
	}
	public DelDataInfo getDelDataInfo() {
		return delDataInfo;
	}
	@XmlElement(name = "DEL_DATAINFO")
	public void setDelDataInfo(DelDataInfo delDataInfo) {
		this.delDataInfo = delDataInfo;
	}
	public Meta getMeta() {
		return meta;
	}
	@XmlTransient
	public void setMeta(Meta meta) {
		this.meta = meta;
	}

	public String getGlobalModel() {
		return globalModel;
	}

	@XmlElement(name = "GLOBALMODEL")
	public void setGlobalModel(String globalModel) {
		this.globalModel = globalModel;
	}

	public ImageInfo getImageInfo() {
		return imageInfo;
	}

	@XmlElement(name = "IMAGEINFO")
	public void setImageInfo(ImageInfo imageInfo) {
		this.imageInfo = imageInfo;
	}

	public Custominfo getCustominfo() {
		return custominfo;
	}

	@XmlElement(name = "CUSTOMINFO")
	public void setCustominfo(Custominfo custominfo) {
		this.custominfo = custominfo;
	}

	public Deviceinfo getDeviceinfo() {
		return deviceinfo;
	}

	@XmlElement(name = "DEVICEINFO")
	public void setDeviceinfo(Deviceinfo deviceinfo) {
		this.deviceinfo = deviceinfo;
	}
}
