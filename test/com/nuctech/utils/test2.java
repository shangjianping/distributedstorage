package com.nuctech.utils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.io.IOUtils;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import com.nuctech.hbase.StorageImageImpl;
import com.nuctech.model.AlgorithmResult;
import com.nuctech.model.Area;
import com.nuctech.model.ICigDetect;
import com.nuctech.model.ISuggest;
import com.nuctech.model.ImgInfo;
import com.nuctech.model.Rect;
import com.nuctech.model.InspectInfo;

public class test2 {

	/**
	 * @param args
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 */
	static public String generateDigest(String idPassword)
			throws NoSuchAlgorithmException {
		String parts[] = idPassword.split(":", 2);
		byte digest[] = MessageDigest.getInstance("SHA1").digest(
				idPassword.getBytes());
		return parts[0] + ":" + new BASE64Encoder().encode((digest));
	}

	static public String generateDigest1(String idPassword)
			throws NoSuchAlgorithmException {
		String parts[] = idPassword.split(":", 2);
		byte digest[] = MessageDigest.getInstance("SHA1").digest(
				idPassword.getBytes());
		return parts[0] + ":" + new BASE64Encoder().encode((digest));
	}

	public static String getFromBase64(String s) {
		byte[] b = null;
		String result = null;
		if (s != null) {
			BASE64Decoder decoder = new BASE64Decoder();
			try {
				b = decoder.decodeBuffer(s);
				result = new String(b);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	public static Map<String, Object> unZip(InputStream in) {

		if (in == null)
			return null;

		ZipEntry zipEntry = null;
		FileOutputStream out = null;
		String iconUrl = null;
		Map<String, Object> map = new HashMap<String, Object>();
		ZipInputStream zipIn = new ZipInputStream(in);
		try {
			System.out.println("unzip begin:"+new SimpleDateFormat("yyyyMMdd HH:mm:ss SSS").format(new Date()));
			while ((zipEntry = zipIn.getNextEntry()) != null) {
				// 如果是文件夹路径方式，本方法内暂时不提供操作
				System.out.println("entry "+zipEntry.getName()+" begin:"+new SimpleDateFormat("yyyyMMdd HH:mm:ss SSS").format(new Date()));
				String name = zipEntry.getName();
				if (zipEntry.isDirectory()) {
					System.out.println("=======direct========" + name);
					if (!new File("D:/test/"+name).exists()) {
						new File("D:/test/" + name).mkdir();
					}
				} else {
					System.out.print(name);
					System.out.println(new Date().toLocaleString());
					iconUrl = "D:/test/" + name;
					File file = new File(iconUrl);
					  out = new FileOutputStream(file);
					   byte[] temp = new byte[8196];
					   int byteread=0;
					   while((byteread = zipIn.read(temp, 0, 8196))!=-1){
						   out.write(temp,0,byteread);
					   }
					   out.close();
					map.put(zipEntry.getName(), iconUrl);
				}
			}
			return map;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			IOUtils.closeQuietly(zipIn);
			IOUtils.closeQuietly(in);
			IOUtils.closeQuietly(out);
		}
	}

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("download begin:"+new SimpleDateFormat("yyyyMMdd HH:mm:ss SSS").format(new Date()));
		StorageImageImpl imageHbase= new StorageImageImpl("image");
		imageHbase.connect();
		ImgInfo imgInfo = imageHbase.read("86574MB01201306202012");
		imageHbase.close();
	System.out.println("download end:"+new SimpleDateFormat("yyyyMMdd HH:mm:ss SSS").format(new Date()));
		InputStream stream= new ByteArrayInputStream(imgInfo.getZips());
		test2.unZip(stream);
		System.out.println("end:"+new SimpleDateFormat("yyyyMMdd HH:mm:ss SSS").format(new Date()));
//		System.out.println(test2.generateDigest("accumulo:hadoop"));
//		System.out.println(test2.getFromBase64("diZNqb4D71cy0fGxC3meE2ZYWyE="));
//		String timetype = "20160134";
//		if (timetype.length() == 6) {
//			timetype = timetype.substring(0, 4) + "-" + timetype.substring(4);
//		} else {
//			timetype = timetype.substring(0, 4) + "-"
//					+ timetype.substring(4, 6) + "-" + timetype.substring(6);
//		}
//		System.out.println(timetype);
	}

}
