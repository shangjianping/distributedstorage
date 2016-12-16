package com.nuctech.utils;



import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @类功能说明：
 * @作者：shangjianping
 * @创建时间：2015-5-12
 * @版本：V1.0
 */
public class ConnectionCxn {
	private static final Logger logger = LoggerFactory
			.getLogger(ConnectionCxn.class);
	private static Constants constants = Constants.getInstance();
	private static Configuration conf = null;
	private static Configuration hbaseConf = null;

	public static synchronized Configuration getHDFSConf() throws Exception {
		try {
			if (conf == null) {
				conf = new Configuration();
				conf.set("fs.defaultFS", constants.getHDFS_DEFAULT_FS());
			}
			return conf;
		} catch (Exception e) {
			logger.error(String.format("实例化Configuration失败,原因如下:【%s】",
					new Object[] { e.getMessage() }));
			throw new Exception(String
					.format("实例化Configuration失败，原因如下:{%s}", e));
		}
	}

	public static synchronized Configuration getHbaseConf(){
		try {
			if (hbaseConf == null) {
				Configuration HBASE_CONFIG = new Configuration();
				 HBASE_CONFIG.set("hbase.zookeeper.quorum", constants.getZK_QUORUM());  
				 HBASE_CONFIG.set("hbase.zookeeper.property.clientPort", constants.getZK_CLIENR_PORT());
				 HBASE_CONFIG.set("zookeeper.znode.parent", constants.getZK_ZNODE_PARENT());
				 HBASE_CONFIG.set("hbase.client.keyvalue.maxsize", constants.getHBASE_KEYVALUE_MAXSIZE());
				hbaseConf = HBaseConfiguration.create(HBASE_CONFIG);
			}
			return hbaseConf;
		} catch (Exception e) {
			logger.error(String.format("实例化Configuration失败,原因如下:【%s】",
					new Object[] { e.getMessage() }));
			e.printStackTrace();
			return null;
		}
	}

}
