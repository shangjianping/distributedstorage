package com.nuctech.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @类功能说明：配置常量
 * @作者：shangjianping
 * @创建时间：2015-5-12
 * @版本：V1.0
 */
public class Catalog {
	private static final Logger logger = LoggerFactory.getLogger(Catalog.class);
	public static Map<String, String> catalogMap;

	/**
	 * @类名：Constants.java
	 * @描述：私有化构造方法
	 */
	private Catalog() {

	}

	public synchronized static Map<String, String> getCatalogMap() {
		if (catalogMap == null) {
			catalogMap = new HashMap<String, String>();
			Properties pros = new Properties();
			InputStream is = null;
			try {
				is = Catalog.class.getClassLoader().getResourceAsStream(
						"conf/catalog.properties");
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
			for (Entry<Object, Object> entry : pros.entrySet()) {
				catalogMap.put((String) entry.getKey(),
						(String) entry.getValue());
			}
		}
		return catalogMap;
	}

	public static void main(String[] arg) {
		Constants con = Constants.getInstance();

		Map<String, String> map = Catalog.getCatalogMap();
		for (Entry<String, String> set : map.entrySet()) {
			System.out.println("1:=" + set.getKey() + ":" + set.getValue());

		}
        System.out.println(map.get("95"));
		System.out.println(con.getCLUSTER_HOSTINFO());
	}

}
