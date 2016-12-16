package com.nuctech.hbase;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.io.FileUtils;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nuctech.model.FileInfo;
import com.nuctech.utils.Constants;

/**
 * 
 * @类功能说明：
 * @作者：shangjianping
 * @创建时间：2015-5-12
 * @版本：V1.0
 */
public class InsertThread extends Thread {
	private static final Logger logger = LoggerFactory
			.getLogger(InsertThread.class);
	Map<Object, Map<String, Object>> map;
	HTableInterface table;
	List<FileInfo> list;

	public InsertThread(HTableInterface table, List<FileInfo> list) {
		this.table = table;
		this.list = list;
	}

	@Override
	public void run() {
		try {
			//ArrayList<Put> puts = createPut();
			put();
			// table.put(puts);
			table.flushCommits();
			table.close();
			logger.info(InsertThread.currentThread().getName() + "处理完毕！");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public ArrayList<Put> createPut() throws IOException {

		ArrayList<Put> puts = new ArrayList<Put>();
		for (Entry<Object, Map<String, Object>> row : map.entrySet()) {

			byte[] rowKey = Bytes.toBytes((String) row.getKey());

			Put put = new Put(rowKey);
			System.out.println(InsertThread.currentThread().getName()
					+ "--rowKey:" + row.getKey() + "  column: ");
			for (Entry<String, Object> column : row.getValue().entrySet()) {
				if (column.getKey().equals("scanImageFolderPath")) {
					File folderFile = new File((String) column.getValue());
					for (File file : folderFile.listFiles()) {
						put.add(Constants.CF_DATA, file.getName().getBytes(),
								FileUtils.readFileToByteArray(file));
						// System.out.print("  "+new
						// String(Constants.CF_DATA)+"/"+file.getName()+":"+file.getPath());
					}
				} else {

					put.add(Constants.CF_META, Bytes.toBytes(column.getKey()),

					Bytes.toBytes((String) column.getValue()));
					// System.out.println("  "+new
					// String(Constants.CF_META)+"/"+column.getKey()+":"+column.getValue());
				}
			}

			puts.add(put);
		}
		return puts;

	}

	public void put() throws Exception {
		if (table == null) {
			logger.error("获取htable实例失败");
			return;
		}

		for (FileInfo fileInfo : list) {
			Put p = null;
			Field[] fields = fileInfo.getClass().getDeclaredFields();
			for (Field field : fields) {
				field.setAccessible(true);
				Object property = field.get(fileInfo);
				if (property != null) {
					if (field.getName().equals("id")) {
						p = new Put(property.toString().getBytes());
						System.out.print(InsertThread.currentThread().getName()
								+ "--rowKey:" + property.toString() + "  column: ");
					} else if (field.getName().equals("scanImageFolderPath")) {
						File folderFile = new File(property.toString());
						for (File file : folderFile.listFiles()) {
							p.add(Constants.CF_DATA, file.getName().getBytes(),
									FileUtils.readFileToByteArray(file));
							System.out.print("  "+new
									String(Constants.CF_DATA)+"/"+file.getName()+":"+file.getPath());
						}
					} else {
						p.add(Constants.CF_META, field.getName().getBytes(),
								property.toString().getBytes());
						System.out.print("  "+new
								String(Constants.CF_META)+"/"+field.getName()+":"+property.toString());
					}
				}
			}
			System.out.println("");
			if (p == null) {
				logger.info("RowKey is null,insert fail!");
				return;
			}
			table.put(p);
		}
	}
}
