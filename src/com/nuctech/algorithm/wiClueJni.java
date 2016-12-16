
package com.nuctech.algorithm;

public class wiClueJni {
	static{		
		System.loadLibrary("wiClueJni");
	}
	public native static String wiCigDetect(String inputXml);
	public native static String wiLiquorDetect(String inputXml);
}

