
package com.nuctech.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @类功能说明：
 * @作者：shangjianping
 * @创建时间：2015-5-12
 * @版本：V1.0
 */
public class PropertiesUtil {
	private static final Logger logger = LoggerFactory
			.getLogger(PropertiesUtil.class);

	private static Properties pros = new Properties();

	/**
	 * 初始化配置文件信息
	 * 
	 * @param configFile
	 *            配置文件名
	 */
	public static void initConfig(String configFile) {
		InputStream is = null;
		try {
			is = PropertiesUtil.class.getClassLoader().getResourceAsStream(
					configFile);
			// 加载属性文件
			pros.load(is);

		} catch (IOException e) {
			logger.error("加载属性文件出错：", e);
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					logger.error("关闭文件流失败！", e);
				}
			}
		}
	}

	/**
	 * 根据key获取属性值
	 * 
	 * @param key
	 *            对应的key
	 * @return 返回String类型的value
	 */
	public static String getStringProperty(String key) {
		return pros.getProperty(key);
	}

	/**
	 * 根据key获取属性值
	 * 
	 * @param key
	 *            对应的key
	 * @return 返回int类型的value
	 */
	public static int getIntProperty(String key) {
		int value = 0;
		try {
			value = Integer.parseInt(pros.getProperty(key));
		} catch (ClassCastException e) {
			logger.error("类型转换错误!");
		} catch (Exception e) {
			logger.error("PropertiesUtil.getIntProperty is error ,error information :",	e);
		}
		return value;
	}

	/**
	 * 根据key获取属性值
	 * 
	 * @param key
	 *            对应的key
	 * @return 返回Long类型的value
	 */
	public static long getLongProperty(String key) {
		long value = 0;
		try {
			value = Long.parseLong(pros.getProperty(key));
		} catch (ClassCastException e) {
			logger.error("类型转换错误!");
		} catch (Exception e) {
			logger.error("PropertiesUtil.getLongProperty is error ,error information :", e);
		}
		return value;
	}

	/**
	 * 根据key获取属性值
	 * 
	 * @param key
	 *            对应的key
	 * @return 返回Boolean类型的value
	 */
	public static boolean getBoolProperty(String key) {
		boolean bool = false;
		try {
			bool = Boolean.parseBoolean(pros.getProperty(key));
		} catch (ClassCastException e) {
			logger.error("类型转换错误!");
		} catch (Exception e) {
			logger.error("PropertiesUtil.getBoolProperty is error ,error information :", e);
		}
		return bool;
	}
	
	public static  Set<Entry<Object, Object>> getAll(){
		return pros.entrySet();
	}

	public static void main(String[] args) throws Exception {
		// Properties pros1= new Properties();
		String configFile = "conf/storage.properties";
		if(new File(configFile).exists()){
			System.out.println(new File(configFile).length());
			
		}
		PropertiesUtil.initConfig(configFile);

		System.out.println(PropertiesUtil.getStringProperty("solr.http.solr"));
	}
}
