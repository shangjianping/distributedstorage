package com.nuctech.model;

import java.util.List;


public class MultiIndex {
	/**
	 * 扫描流水号
	 */
	private String scanNumber;
	/**
	 * 机检结论
	 */
	private String idrConclusion;
	/**
	 * 机检结论说明
	 */
	private String conclusionContent;
	/**
	 * 布控要求
	 */
	private String imp;
	/**
	 * 集装箱号
	 */
	private String containerNo;
	/** IDR_SII_EDI_ENTRY */
	private List<Entry> entryList;

	public String getScanNumber() {
		return scanNumber;
	}

	public void setScanNumber(String scanNumber) {
		this.scanNumber = scanNumber;
	}

	public String getIdrConclusion() {
		return idrConclusion;
	}

	public void setIdrConclusion(String idrConclusion) {
		this.idrConclusion = idrConclusion;
	}

	public String getConclusionContent() {
		return conclusionContent;
	}

	public void setConclusionContent(String conclusionContent) {
		this.conclusionContent = conclusionContent;
	}

	public String getImp() {
		return imp;
	}

	public void setImp(String imp) {
		this.imp = imp;
	}

	public String getContainerNo() {
		return containerNo;
	}

	public void setContainerNo(String containerNo) {
		this.containerNo = containerNo;
	}

	public List<Entry> getEntryList() {
		return entryList;
	}

	public void setEntryList(List<Entry> entryList) {
		this.entryList = entryList;
	}

}
