package com.nuctech.model;

import javax.xml.bind.annotation.XmlElement;

public class Sim {
	
	private String similarityId;
	private String similaritySampleNum;

	
	public String getSimilarityId() {
		return similarityId;
	}

	@XmlElement(name = "similarityId")
	public void setSimilarityId(String similarityId) {
		this.similarityId = similarityId;
	}


	public String getSimilaritySampleNum() {
		return similaritySampleNum;
	}

	@XmlElement(name = "similaritySampleNum")
	public void setSimilaritySampleNum(String similaritySampleNum) {
		this.similaritySampleNum = similaritySampleNum;
	}
	
}
