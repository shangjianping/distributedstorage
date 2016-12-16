package com.nuctech.algorithm;




public class verilabel {
	static{		
		//System.setProperty("java.library.path",".");
		System.loadLibrary("wiVerilabel");
		System.loadLibrary("verilabelJni");
		
		//System.load("file:///root/lib/libwiVerilabel.so");
		//System.load("file:///root/lib/libverilabelJni.so");					
	}

	public native static String init();
	public native static String destroy(String inputXml);
	public native static String verifylabelAnalyze(String inputXml);
	public native static String updateModel(String inputXml);
	public native static String getThresholdTotal(String inputXml);
	public native static String getAvailableDevice();
	public native static String getNumberHscodes(String inputXml);
	public native static String getHscodeList(String inputXml);
	public native static String getHscodeInfo(String inputXml);
	public native static String setHscodeThreshold(String inputXml);
	public native static String setDelModelSample(String inputXml);	
}











