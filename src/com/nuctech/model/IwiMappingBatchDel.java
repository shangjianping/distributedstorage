package com.nuctech.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "iwiMappingBatchDel")
public class IwiMappingBatchDel {
	private String result;
	private String count;

	public String getCount() {
		return count;
	}
	@XmlElement(name = "count")
	public void setCount(String count) {
		this.count = count;
	}
	public String getResult() {
		return result;
	}
	@XmlElement(name = "result")
	public void setResult(String result) {
		this.result = result;
	}

}
