package com.nuctech.model;

import javax.xml.bind.annotation.XmlElement;

public class Result {
	private ISugIni iSugIni;
	private ISuggest iSuggest;
	private ISugDestroy iSugDestroy;
	public ISugIni getISugIni() {
		return iSugIni;
	}
	@XmlElement(name = "iSugIni")
	public void setISugIni(ISugIni iSugIni) {
		this.iSugIni = iSugIni;
	}
	public ISuggest getISuggest() {
		return iSuggest;
	}
	@XmlElement(name = "iSuggest")
	public void setISuggest(ISuggest iSuggest) {
		this.iSuggest = iSuggest;
	}
	public ISugDestroy getISugDestroy() {
		return iSugDestroy;
	}
	@XmlElement(name = "iSugDestroy")
	public void setISugDestroy(ISugDestroy iSugDestroy) {
		this.iSugDestroy = iSugDestroy;
	}
	




}
