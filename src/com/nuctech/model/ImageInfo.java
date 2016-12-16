package com.nuctech.model;

import javax.xml.bind.annotation.XmlElement;

public class ImageInfo {
	private String imageid;
	private String imagefolder;
	private String pathtype;
	private Image image;
	private String goodsnum;
	private String catalog;
	private SelectArea selectarea;


public String getImageid() {
	return imageid;
}
@XmlElement(name = "IMAGEID") 
public void setImageid(String imageid) {
	this.imageid = imageid;
}
public String getImagefolder() {
	return imagefolder;
}
@XmlElement(name = "IMAGEFOLDER") 
public void setImagefolder(String imagefolder) {
	this.imagefolder = imagefolder;
}
public String getPathtype() {
	return pathtype;
}
@XmlElement(name = "PATHTYPE") 
public void setPathtype(String pathtype) {
	this.pathtype = pathtype;
}
public Image getImage() {
	return image;
}
@XmlElement(name = "IMAGE") 
public void setImage(Image image) {
	this.image = image;
}
public String getGoodsnum() {
	return goodsnum;
}
@XmlElement(name = "GOODS_NUM") 
public void setGoodsnum(String goodsnum) {
	this.goodsnum = goodsnum;
}

public String getCatalog() {
	return catalog;
}

@XmlElement(name = "CATALOG") 
public void setCatalog(String catalog) {
	this.catalog = catalog;
}

public SelectArea getSelectarea() {
	return selectarea;
}

@XmlElement(name = "SELECT_AREA") 
public void setSelectarea(SelectArea selectarea) {
	this.selectarea = selectarea;
}

}


