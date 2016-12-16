/**
 * 
 */
package com.nuctech.algorithm;

public class wiWasteClueJni {
	static{		
		System.loadLibrary("wiWasteClueJni");
	}
	public native static String wiWasteInit(String inputXml);
	public native static String wiWasteDetect(String inputXml);
	public native static String wiWasteDestory();
}
