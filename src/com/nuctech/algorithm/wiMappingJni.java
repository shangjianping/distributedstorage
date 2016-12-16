/**
 * 
 */
package com.nuctech.algorithm;

public class wiMappingJni {
	static{		
		//System.loadLibrary("wiVerilabel");
		System.loadLibrary("wiMappingJni");				
	}

	public native static String init();
	public native static String search(String inputXml);
	public native static String updateSearchModelAdd(String inputXml);
	public native static String updateSearchModelDel(String inputXml);
	public native static String getImageNames();
	public native static String batchDeleteData(String inputXml);
}

