package com.nuctech.hbase;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.io.FileUtils;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nuctech.model.InspectInfo;
import com.nuctech.utils.Constants;

/**
 * 
 * @类功能说明：
 * @作者：shangjianping
 * @创建时间：2015-5-12
 * @版本：V1.0
 */
public class InsertNewThread extends Thread {
	private static final Logger logger = LoggerFactory
			.getLogger(InsertNewThread.class);
	Map<Object, Map<String, Object>> map;
	Table table;
	List<InspectInfo> list;

	public InsertNewThread(Table table, List<InspectInfo> list) {
		this.table = table;
		this.list = list;
	}

	@Override
	public void run() {
		try {
			//ArrayList<Put> puts = createPut();
			put();
			// table.put(puts);
			table.close();
			logger.info(InsertNewThread.currentThread().getName() + "处理完毕！");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public ArrayList<Put> createPut() throws Exception {

		ArrayList<Put> puts = new ArrayList<Put>();
		for (InspectInfo info : list) {
			if (info == null ||info.getId()==null||info.getId().equals("")) {
				logger.info("RowKey is null,insert fail!");
				continue;
			}
			Put p=new Put(Bytes.toBytes(info.getId()));
			addPut(info.getMeta(), p, "meta", "");
			addPut(info.getAlgorithmResult(), p, "algorithmResult", "");
			puts.add(p);
		}
		return puts;

	}

	public void put() throws Exception {
		if (table == null) {
			logger.error("获取htable实例失败");
			return;
		}
		logger.info(InsertNewThread.currentThread().getName() + "处理数目："+list.size());
		for (InspectInfo info : list) {
			if (info == null ||info.getId()==null||info.getId().equals("")) {
				logger.info("RowKey is null,insert fail!");
				continue;
			}
			Put p=new Put(Bytes.toBytes(info.getId()));
			addPut(info.getMeta(), p, "meta", "");
			addPut(info.getAlgorithmResult(), p, "algorithmResult", "");
			table.put(p);
		}
	}
	
	public void addPut(Object obj, Put put, String family, String prefix)
			throws Exception {
		if (obj != null) {
			for (Field field : obj.getClass().getDeclaredFields()) {
				field.setAccessible(true);
				Object property = field.get(obj);

				if (property != null) {
					if (field.getType().equals(String.class)) {
						System.out.println(InsertNewThread.currentThread().getName()+":"+family + " : " + prefix + field.getName()
								+ "=" + property.toString());
						if (field.getName().equals("path")
								&& new File(property.toString()).exists()) {
							put.add(Bytes.toBytes(family), Bytes.toBytes(prefix
									+ field.getName()), FileUtils
									.readFileToByteArray(new File(property
											.toString())));
						} else {
							put.add(Bytes.toBytes(family),
									Bytes.toBytes(prefix + field.getName()),
									Bytes.toBytes(property.toString()));
						}
					} else if (field.getType().equals(List.class)) {
						int i = 0;
						for (Object member : (List) property) {
							addPut(member,
									put,
									family,
									prefix + field.getName() + "["
											+ String.valueOf(i) + "].");
							i += 1;
						}
					} else {
						addPut(property, put, family, prefix + field.getName()
								+ ".");
					}
				}
			}
		}
	}
}
