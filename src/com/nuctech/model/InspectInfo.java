package com.nuctech.model;


public class InspectInfo {
	private String Id;
	public String getId() {
		return Id;
	}
	public void setId(String id) {
		Id = id;
	}
	private Meta meta;
	private AlgorithmResult algorithmResult;
	public Meta getMeta() {
		return meta;
	}
	public void setMeta(Meta meta) {
		this.meta = meta;
	}
	public AlgorithmResult getAlgorithmResult() {
		return algorithmResult;
	}
	public void setAlgorithmResult(AlgorithmResult algorithmResult) {
		this.algorithmResult = algorithmResult;
	}


}
