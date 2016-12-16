package com.nuctech.model;






/**
 * 
 * @类功能说明：
 * @作者：shangjianping
 * @创建时间：2015-5-12
 * @版本：V1.0
 */
public class FileInfo {

private String id;
/**
 * 图像存储路径，即保存扫描图像的文件夹路径
 */
private String scanImageFolderPath;
private String type;

/**
 * 图像来源：查验系统、交互式系统
 */
private String source;

private String checkConclusion;
private String checkTime;
public String getCheckConclusion() {
	return checkConclusion;
}

public void setCheckConclusion(String checkConclusion) {
	this.checkConclusion = checkConclusion;
}

public String getCheckTime() {
	return checkTime;
}

public void setCheckTime(String checkTime) {
	this.checkTime = checkTime;
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

public String getCreateTime() {
	return createTime;
}

public void setCreateTime(String createTime) {
	this.createTime = createTime;
}

public String getDeclarationCompany() {
	return declarationCompany;
}

public void setDeclarationCompany(String declarationCompany) {
	this.declarationCompany = declarationCompany;
}

public String getDeclarationNo() {
	return declarationNo;
}

public void setDeclarationNo(String declarationNo) {
	this.declarationNo = declarationNo;
}

public String getDescription() {
	return description;
}

public void setDescription(String description) {
	this.description = description;
}

public String getLastUpdateTime() {
	return lastUpdateTime;
}

public void setLastUpdateTime(String lastUpdateTime) {
	this.lastUpdateTime = lastUpdateTime;
}

public String getOperatingCompany() {
	return operatingCompany;
}

public void setOperatingCompany(String operatingCompany) {
	this.operatingCompany = operatingCompany;
}

public String getReleaseTime() {
	return releaseTime;
}

public void setReleaseTime(String releaseTime) {
	this.releaseTime = releaseTime;
}

public String getRemark() {
	return remark;
}

public void setRemark(String remark) {
	this.remark = remark;
}

public String getScanTime() {
	return scanTime;
}

public void setScanTime(String scanTime) {
	this.scanTime = scanTime;
}

private String commodityCategory;
private String commodityName;
private String createTime;
private String declarationCompany;
private String declarationNo;
private String description;
private String lastUpdateTime;
private String operatingCompany;
private String releaseTime;
private String remark;
private String scanTime;
private String createUser;
private String lastUpdateUser;
private String releaseUser;
private byte[] xmlFile;
private String state;
private String date;



public String getDate() {
	return date;
}

public void setDate(String date) {
	this.date = date;
}

public String getState() {
	return state;
}

public void setState(String state) {
	this.state = state;
}

public byte[] getXmlFile() {
	return xmlFile;
}

public void setXmlFile(byte[] xmlFile) {
	this.xmlFile = xmlFile;
}

private String analysisTips;

public String getAnalysisTips() {
	return analysisTips;
}

public void setAnalysisTips(String analysisTips) {
	this.analysisTips = analysisTips;
}


public String getCreateUser() {
	return createUser;
}

public void setCreateUser(String createUser) {
	this.createUser = createUser;
}

public String getLastUpdateUser() {
	return lastUpdateUser;
}

public void setLastUpdateUser(String lastUpdateUser) {
	this.lastUpdateUser = lastUpdateUser;
}

public String getReleaseUser() {
	return releaseUser;
}

public void setReleaseUser(String releaseUser) {
	this.releaseUser = releaseUser;
}

public String getId() {
	return id;
}

public void setId(String id) {
	this.id = id;
}

public String getScanImageFolderPath() {
	return scanImageFolderPath;
}

public void setScanImageFolderPath(String scanImageFolderPath) {
	this.scanImageFolderPath = scanImageFolderPath;
}

public String getType() {
	return type;
}

public void setType(String type) {
	this.type = type;
}

public String getSource() {
	return source;
}

public void setSource(String source) {
	this.source = source;
}



}
