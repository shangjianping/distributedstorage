
package com.nuctech.algorithm;

public class wiTestJni {
	static{		
		//System.setProperty("java.library.path",".");
		//System.loadLibrary("verilabelJni");
		//System.loadLibrary("wiVerilabel");
		//System.load("/home/share/wiScan_linux64_JNI/test/libwiTestJni.so");
		System.loadLibrary("wiTestJni");
	}
	
	public static native String wiTestIni();
}

