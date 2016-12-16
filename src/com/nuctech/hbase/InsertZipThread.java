package com.nuctech.hbase;

import java.util.List;
import java.util.Map;

import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nuctech.model.ImgInfo;

/**
 * 
 * @类功能说明：
 * @作者：shangjianping
 * @创建时间：2015-5-12
 * @版本：V1.0
 */
public class InsertZipThread extends Thread {
	private static final Logger logger = LoggerFactory
			.getLogger(InsertZipThread.class);
	Map<Object, Map<String, Object>> map;
	Table table;
	List<ImgInfo> list;

	public InsertZipThread(Table table, List<ImgInfo> list) {
		this.table = table;
		this.list = list;
	}

	@Override
	public void run() {
		try {
			put();
			table.close();
			logger.info(InsertZipThread.currentThread().getName() + "处理完毕！");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public void put() throws Exception {
		if (table == null) {
			logger.error("获取htable实例失败");
			return;
		}

		for (ImgInfo info : list) {
			if (info == null || info.getId() == null || info.getId().equals("")) {
				logger.info("RowKey is null,insert fail!");
				continue;
			}
			Put p = new Put(Bytes.toBytes(info.getId()));
			if(info.getZips()!=null){
				p.addColumn(Bytes.toBytes("data"),Bytes.toBytes("zips"),info.getZips());
			}
			if(info.getState()!=null){
				p.addColumn(Bytes.toBytes("meta"),Bytes.toBytes("state"),Bytes.toBytes(info.getState()));
			}
			table.put(p);
		}
	}
}
