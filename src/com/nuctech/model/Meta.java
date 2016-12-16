package com.nuctech.model;


public class Meta {
	private String scanTime;
	private String containerNo;
	private String vesselNo;
	private String articleNo;
	private String declarationNo;
	private String commodityCategory;
	private String commodityName;
	private String uploadDate;
	private String uploadTimestamp;
	private String ladingBillNo;
	private String declarationCompany;
	private String operatingCompany;
	private String state;
	private String riskLevel;
	private String manualResult;
	private byte[] icon;
	public byte[] getIcon() {
		return icon;
	}
	public void setIcon(byte[] icon) {
		this.icon = icon;
	}
	
	public String getUploadTimestamp() {
		return uploadTimestamp;
	}
	public void setUploadTimestamp(String uploadTimestamp) {
		this.uploadTimestamp = uploadTimestamp;
	}
	public String getUploadDate() {
		return uploadDate;
	}
	public void setUploadDate(String uploadDate) {
		this.uploadDate = uploadDate;
	}
	public String getScanTime() {
		return scanTime;
	}
	public void setScanTime(String scanTime) {
		this.scanTime = scanTime;
	}
	public String getContainerNo() {
		return containerNo;
	}
	public void setContainerNo(String containerNo) {
		this.containerNo = containerNo;
	}
	public String getVesselNo() {
		return vesselNo;
	}
	public void setVesselNo(String vesselNo) {
		this.vesselNo = vesselNo;
	}
	public String getArticleNo() {
		return articleNo;
	}
	public void setArticleNo(String articleNo) {
		this.articleNo = articleNo;
	}
	public String getDeclarationNo() {
		return declarationNo;
	}
	public void setDeclarationNo(String declarationNo) {
		this.declarationNo = declarationNo;
	}
	public String getCommodityCategory() {
		return commodityCategory;
	}
	public void setCommodityCategory(String commodityCategory) {
		this.commodityCategory = commodityCategory;
	}
	public String getCommodityName() {
		return commodityName;
	}
	public void setCommodityName(String commodityName) {
		this.commodityName = commodityName;
	}
	public String getLadingBillNo() {
		return ladingBillNo;
	}
	public void setLadingBillNo(String ladingBillNo) {
		this.ladingBillNo = ladingBillNo;
	}
	public String getDeclarationCompany() {
		return declarationCompany;
	}
	public void setDeclarationCompany(String declarationCompany) {
		this.declarationCompany = declarationCompany;
	}
	public String getOperatingCompany() {
		return operatingCompany;
	}
	public void setOperatingCompany(String operatingCompany) {
		this.operatingCompany = operatingCompany;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getRiskLevel() {
		return riskLevel;
	}
	public void setRiskLevel(String riskLevel) {
		this.riskLevel = riskLevel;
	}
	
	public String getManualResult() {
		return manualResult;
	}
	public void setManualResult(String manualResult) {
		this.manualResult = manualResult;
	}



}


