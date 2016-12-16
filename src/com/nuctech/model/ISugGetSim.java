package com.nuctech.model;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "iSugGetSim")
public class ISugGetSim {
	private String result;
	private String curThre;
	private String simDIM;
	private List<Sim> sim;
	


	public List<Sim> getSim() {
		return sim;
	}

	@XmlElement(name = "sim")
	public void setSim(List<Sim> sim) {
		this.sim = sim;
	}


	public String getResult() {
		return result;
	}


	@XmlElement(name = "result")
	public void setResult(String result) {
		this.result = result;
	}



	public String getCurThre() {
		return curThre;
	}


	@XmlElement(name = "curThre")
	public void setCurThre(String curThre) {
		this.curThre = curThre;
	}



	public String getSimDIM() {
		return simDIM;
	}


	@XmlElement(name = "simDIM")
	public void setSimDIM(String simDIM) {
		this.simDIM = simDIM;
	}


}
