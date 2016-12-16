package com.nuctech.startup;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import com.nuctech.hdfs.HDFSUtil;
import com.nuctech.utils.Shell;

public class RunnableTest implements Runnable {
	
	@Override
	public void run() {
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("aaaaaaaa");
		
	}

}
