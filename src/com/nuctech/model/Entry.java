package com.nuctech.model;

import java.util.List;
import javax.xml.bind.annotation.XmlElement;

public class Entry {
	private String importForm;
	private String exportto;
	private String grossWeight;
	private String netWeight;
	private List<EntryItems> EntryItems;
	public String getImportForm() {
		return importForm;
	}
	@XmlElement(name = "IMPORT_FORM") 
	public void setImportForm(String importForm) {
		this.importForm = importForm;
	}
	public String getExportto() {
		return exportto;
	}
	@XmlElement(name = "EXPORT_TO") 
	public void setExportto(String exportto) {
		this.exportto = exportto;
	}
	public String getGrossWeight() {
		return grossWeight;
	}
	@XmlElement(name = "GROSS_WEIGHT") 
	public void setGrossWeight(String grossWeight) {
		this.grossWeight = grossWeight;
	}
	public String getNetWeight() {
		return netWeight;
	}
	@XmlElement(name = "NET_WEIGHT") 
	public void setNetWeight(String netWeight) {
		this.netWeight = netWeight;
	}
	public List<EntryItems> getEntryItems() {
		return EntryItems;
	}
	@XmlElement(name = "ENTRY_ITEMS") 
	public void setEntryItems(List<EntryItems> entryItems) {
		EntryItems = entryItems;
	}

}
