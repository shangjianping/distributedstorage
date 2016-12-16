package com.nuctech.hbase;

import java.io.IOException;
import java.util.Date;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.io.compress.Compression.Algorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nuctech.utils.ConnectionCxn;
import com.nuctech.utils.Constants;

/**
 * 
 * @类功能说明：hbase表操作
 * @作者：shangjianping
 * @创建时间：2015-5-12
 * @版本：V1.0
 */
public class HtableAdmin {
	private static final Logger logger = LoggerFactory
			.getLogger(HtableAdmin.class);
	public static Configuration conf = ConnectionCxn.getHbaseConf();

	public void overwriteTable(String tableName) {
		try {
			HBaseAdmin admin = new HBaseAdmin(conf);
			HTableDescriptor table = new HTableDescriptor(
					TableName.valueOf(tableName));
			if (admin.tableExists(table.getName())) {
				admin.disableTable(table.getName());
				admin.deleteTable(table.getName());
			}
			table.addFamily(new HColumnDescriptor(Constants.CF_META)
					.setCompressionType(Algorithm.SNAPPY));
			table.addFamily(new HColumnDescriptor(Constants.CF_DATA)
					.setCompressionType(Algorithm.SNAPPY));
			admin.createTable(table);
			admin.close();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("overwrite table " + tableName + " fail!");
		}
	}

	public void createTables(String tableName) {
		try {
			HBaseAdmin admin = new HBaseAdmin(conf);
			if (!admin.tableExists(tableName)) {
				HTableDescriptor table = new HTableDescriptor(
						TableName.valueOf(tableName));
				table.addFamily(new HColumnDescriptor(Constants.CF_META)
						.setCompressionType(Algorithm.SNAPPY));
				table.addFamily(new HColumnDescriptor(Constants.CF_DATA)
						.setCompressionType(Algorithm.SNAPPY));
	
				admin.createTable(table);
			}
			admin.close();
			logger.info("Creating table " + tableName + " success!");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Creating table " + tableName + " fail!");
		}
	}

	public void dropTables(String tableName) {
		try {
			HBaseAdmin admin = new HBaseAdmin(conf);
			if (admin.tableExists(tableName)) {
				admin.disableTable(TableName.valueOf(tableName));
				admin.deleteTable(TableName.valueOf(tableName));
				logger.info("delete table " + tableName + " success!");
			} else {
				logger.info("delete table " + tableName + " not exists!");
			}
			admin.close();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("delete table " + tableName + " fail!");
		}
	}

	
	public static void main(String[] args) throws IOException {
		HtableAdmin a = new HtableAdmin();
		
		System.out.println("begin:"
				+ new java.text.SimpleDateFormat("yyyy/MM/dd HH:mm:ss:SSS")
						.format(new Date()));
		//a.dropTables("feat");
		a.createTables("feat");
		System.out.println("end:"
				+ new java.text.SimpleDateFormat("yyyy/MM/dd HH:mm:ss:SSS")
						.format(new Date()));

	}

}
