package com.nuctech.hdfs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import com.nuctech.utils.ConnectionCxn;


public class HDFSUtil {
	
	
	public static void createFile(String fileName,File file) throws Exception{
		Configuration conf = ConnectionCxn.getHDFSConf();
		FileSystem fs =FileSystem.get(conf);
		FSDataOutputStream os= fs.create(new Path("/"+fileName),true);
		FileInputStream  in = new FileInputStream(file);
		byte[] data=new byte[4096];
    	int count=0;
    	 while ((count = in.read(data)) != -1){
				os.write(data, 0, count);
				os.flush();
			}
    	 in.close();
    	 os.close();
	}
	
	public static InputStream readFile(String fileName) throws Exception{
		Configuration conf = ConnectionCxn.getHDFSConf();
		FileSystem fs = FileSystem.get(conf);
		InputStream is = fs.open(new Path("/"+fileName));
		return is;
	}

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
//		try {
//			InputStream is = HDFSUtil.readFile("test1");
//			FileOutputStream os = new FileOutputStream(new File("E:/test.txt"));
//			int count = 0;
//			byte[] buffer = new byte[4096];
//			while((count = is.read(buffer,0,4096))!=-1){
//				os.write(buffer,0,count);
//			}
//			os.close();
//			is.close();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		HDFSUtil.createFile("tmp/test1", new File("F:/aaa.txt"));
	}

}
