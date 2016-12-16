package com.nuctech.model;

public class AlgorithmResult {

	private ISuggest ISuggest;
	private ISearchPic ISearchPic;
	private ICigDetect ICigDetect;
	private ILiquorDetect ILiquorDetect;
	private IWasteDetect IWasteDetect;
	private byte[] jpg;
	private byte[] sug;
	private byte[] waste;
	public byte[] getJpg() {
		return jpg;
	}
	public void setJpg(byte[] jpg) {
		this.jpg = jpg;
	}
	public byte[] getSug() {
		return sug;
	}
	public void setSug(byte[] sug) {
		this.sug = sug;
	}
	public byte[] getWaste() {
		return waste;
	}
	public void setWaste(byte[] waste) {
		this.waste = waste;
	}
	public IWasteDetect getIWasteDetect() {
		return IWasteDetect;
	}
	public void setIWasteDetect(IWasteDetect iWasteDetect) {
		IWasteDetect = iWasteDetect;
	}
	public ISuggest getISuggest() {
		return ISuggest;
	}
	public void setISuggest(ISuggest iSuggest) {
		ISuggest = iSuggest;
	}
	public ISearchPic getISearchPic() {
		return ISearchPic;
	}
	public void setISearchPic(ISearchPic iSearchPic) {
		ISearchPic = iSearchPic;
	}
	public ICigDetect getICigDetect() {
		return ICigDetect;
	}
	public void setICigDetect(ICigDetect iCigDetect) {
		ICigDetect = iCigDetect;
	}
	public ILiquorDetect getILiquorDetect() {
		return ILiquorDetect;
	}
	public void setILiquorDetect(ILiquorDetect iLiquorDetect) {
		ILiquorDetect = iLiquorDetect;
	}
	
}
