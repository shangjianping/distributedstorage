package com.nuctech.utils;

import com.nuctech.utils.PropertiesUtil;

/**
 * @类功能说明：配置常量
 * @作者：shangjianping
 * @创建时间：2015-5-12
 * @版本：V1.0
 */
public class Constants {
	public static Constants constants = new Constants();
	public static final byte[] CF_DATA = "data".getBytes();
	public static final byte[] CF_META = "meta".getBytes();
	public static final byte[] CF_ALGORITH = "algorithmResult".getBytes();
	/**
	 * @类名：Constants.java
	 * @描述：私有化构造方法
	 */
	private Constants() {
		PropertiesUtil.initConfig("conf/storage.properties");
	}

	public static Constants getInstance() {
		if (constants == null) {
			constants = new Constants();
		}
		return constants;
	}

	private String HBASE_MASTER;
	private String ZK_QUORUM;
	private String ZK_CLIENR_PORT;
	private String ZK_ZNODE_PARENT;
	private String HBASE_KEYVALUE_MAXSIZE;
	private Integer HBASE_READ_PROCESS;
	private String SOLR_HTTP_URL;
	private Integer SOLR_QUEUE_SIZE;
	private Integer SOLR_THREAD_COUNT;
	private String HDFS_DEFAULT_FS;
	private String SPARK_IP;
	private String SPARK_USR;
	private String RESULT_TABLENAME;
	private String CLUSTER_HOSTINFO;
	private String XLS_FILEPATH;
	public String getXLS_FILEPATH() {
		if (XLS_FILEPATH == null) {
			XLS_FILEPATH = PropertiesUtil.getStringProperty("xsl.file.path");
		}
		return XLS_FILEPATH;
	}

	public void setXLS_FILEPATH(String xLS_FILEPATH) {
		XLS_FILEPATH = xLS_FILEPATH;
	}

	public String getCLUSTER_HOSTINFO() {
		if (CLUSTER_HOSTINFO == null) {
			CLUSTER_HOSTINFO = PropertiesUtil.getStringProperty("cluster.host.info");
		}
		return CLUSTER_HOSTINFO;
	}

	public void setCLUSTER_HOSTINFO(String cLUSTER_HOSTINFO) {
		CLUSTER_HOSTINFO = cLUSTER_HOSTINFO;
	}

	public String getRESULT_TABLENAME() {
		if (RESULT_TABLENAME == null) {
			RESULT_TABLENAME = PropertiesUtil.getStringProperty("stream.result.tablename");
		}
		return RESULT_TABLENAME;
	}

	public void setRESULT_TABLENAME(String rESULT_TABLENAME) {
		RESULT_TABLENAME = rESULT_TABLENAME;
	}

	private String FLUME_HOST;
	private String FLUME_PORT;
	private String RECIVE_PATH;
	private String IMAGE_TABLENAME;
	public String getIMAGE_TABLENAME() {
		if (IMAGE_TABLENAME == null) {
			IMAGE_TABLENAME = PropertiesUtil.getStringProperty("image.table.name");
		}
		return IMAGE_TABLENAME;
	}

	public void setIMAGE_TABLENAME(String iMAGE_TABLENAME) {
		IMAGE_TABLENAME = iMAGE_TABLENAME;
	}

	private String IMAGE_DOWNLOAD_PATH;
	private String VERI_MODE_PATH;
	private String MAPPING_MODE_PATH;
	private String WASTE_MODE_PATH;
	
	private Integer ICON_WIDTH;
	private Integer ICON_HEIGHT;
	private String CORE_INSPECT;
	private String CORE_STANDARD;
	private String STANDARD_TABLENAME;
	private String FTP_IP;
	private Integer FTP_PORT;
	private String FTP_USER;
	private String FTP_PASSWD;
	private String FTP_SHARE_PATH;
	private String PHOENIX_DRIVER;
	private String PHOENIX_URL;
	
	public String getPHOENIX_DRIVER() {
		if (PHOENIX_DRIVER == null) {
			PHOENIX_DRIVER = PropertiesUtil.getStringProperty("phoenix.jdbc.driver");
		}
		return PHOENIX_DRIVER;
	}

	public void setPHOENIX_DRIVER(String pHOENIX_DRIVER) {
		PHOENIX_DRIVER = pHOENIX_DRIVER;
	}

	public String getPHOENIX_URL() {
		if (PHOENIX_URL == null) {
			PHOENIX_URL = PropertiesUtil.getStringProperty("phoenix.jdbc.url");
		}
		return PHOENIX_URL;
	}

	public void setPHOENIX_URL(String pHOENIX_URL) {
		PHOENIX_URL = pHOENIX_URL;
	}

	public String getFTP_IP() {
		if (FTP_IP == null) {
			FTP_IP = PropertiesUtil.getStringProperty("ftp.ip");
		}
		return FTP_IP;
	}

	public void setFTP_IP(String fTP_IP) {
		FTP_IP = fTP_IP;
	}

	public Integer getFTP_PORT() {
		if (FTP_PORT == null) {
			FTP_PORT = PropertiesUtil.getIntProperty("ftp.port");
		}
		return FTP_PORT;
	}

	public void setFTP_PORT(int fTP_PORT) {
		FTP_PORT = fTP_PORT;
	}

	public String getFTP_USER() {
		if (FTP_USER == null) {
			FTP_USER = PropertiesUtil.getStringProperty("ftp.user");
		}
		return FTP_USER;
	}

	public void setFTP_USER(String fTP_USER) {
		FTP_USER = fTP_USER;
	}

	public String getFTP_PASSWD() {
		if (FTP_PASSWD == null) {
			FTP_PASSWD = PropertiesUtil.getStringProperty("ftp.passwd");
		}
		return FTP_PASSWD;
	}

	public void setFTP_PASSWD(String fTP_PASSWD) {
		FTP_PASSWD = fTP_PASSWD;
	}

	public String getFTP_SHARE_PATH() {
		if (FTP_SHARE_PATH == null) {
			FTP_SHARE_PATH = PropertiesUtil.getStringProperty("ftp.share.path");
		}
		return FTP_SHARE_PATH;
	}

	public void setFTP_SHARE_PATH(String fTP_SHARE_PATH) {
		FTP_SHARE_PATH = fTP_SHARE_PATH;
	}

	public String getSTANDARD_TABLENAME() {
		if (STANDARD_TABLENAME == null) {
			STANDARD_TABLENAME = PropertiesUtil.getStringProperty("standard.image.tablename");
		}
		return STANDARD_TABLENAME;
	}

	public void setSTANDARD_TABLENAME(String sTANDARD_TABLENAME) {
		STANDARD_TABLENAME = sTANDARD_TABLENAME;
	}

	public String getCORE_INSPECT() {
		if (CORE_INSPECT == null) {
			CORE_INSPECT = PropertiesUtil.getStringProperty("solr.core.inspect");
		}
		return CORE_INSPECT;
	}

	public void setCORE_INSPECT(String cORE_INSPECT) {
		CORE_INSPECT = cORE_INSPECT;
	}

	public String getCORE_STANDARD() {
		if (CORE_STANDARD == null) {
			CORE_STANDARD = PropertiesUtil.getStringProperty("solr.core.standard");
		}
		return CORE_STANDARD;
	}

	public void setCORE_STANDARD(String cORE_STANDARD) {
		CORE_STANDARD = cORE_STANDARD;
	}

	public int getICON_WIDTH() {
		if (ICON_WIDTH == null) {
			ICON_WIDTH = PropertiesUtil.getIntProperty("icon.width");
		}
		return ICON_WIDTH;
	}

	public void setICON_WIDTH(int iCON_WIDTH) {
		ICON_WIDTH = iCON_WIDTH;
	}

	public int getICON_HEIGHT() {
		if (ICON_HEIGHT == null) {
			ICON_HEIGHT = PropertiesUtil.getIntProperty("icon.height");
		}
		return ICON_HEIGHT;
	}

	public void setICON_HEIGHT(int iCON_HEIGHT) {
		ICON_HEIGHT = iCON_HEIGHT;
	}


	public String getWASTE_MODE_PATH() {
		if (WASTE_MODE_PATH == null) {
			WASTE_MODE_PATH = PropertiesUtil.getStringProperty("waste.mode.path");
		}
		return WASTE_MODE_PATH;
	}

	public void setWASTE_MODE_PATH(String wASTE_MODE_PATH) {
		WASTE_MODE_PATH = wASTE_MODE_PATH;
	}

	public String getVERI_MODE_PATH() {
		if (VERI_MODE_PATH == null) {
			VERI_MODE_PATH = PropertiesUtil.getStringProperty("veri.mode.path");
		}
		return VERI_MODE_PATH;
	}

	public void setVERI_MODE_PATH(String vERI_MODE_PATH) {
		VERI_MODE_PATH = vERI_MODE_PATH;
	}

	public String getMAPPING_MODE_PATH() {
		if (MAPPING_MODE_PATH == null) {
			MAPPING_MODE_PATH = PropertiesUtil.getStringProperty("mapping.mode.path");
		}
		return MAPPING_MODE_PATH;
	}

	public void setMAPPING_MODE_PATH(String mAPPING_MODE_PATH) {
		MAPPING_MODE_PATH = mAPPING_MODE_PATH;
	}

	private String HDFS_MODE_PATH;
	
	public String getFLUME_HOST() {
		if (FLUME_HOST == null) {
			FLUME_HOST = PropertiesUtil.getStringProperty("flume.hostname");
		}
		return FLUME_HOST;
	}

	public void setFLUME_HOST(String fLUME_HOST) {
		FLUME_HOST = fLUME_HOST;
	}

	public String getFLUME_PORT() {
		if (FLUME_PORT == null) {
			FLUME_PORT = PropertiesUtil.getStringProperty("flume.port");
		}
		return FLUME_PORT;
	}

	public void setFLUME_PORT(String fLUME_PORT) {
		FLUME_PORT = fLUME_PORT;
	}

	public String getRECIVE_PATH() {
		if (RECIVE_PATH == null) {
			RECIVE_PATH = PropertiesUtil.getStringProperty("flume.recive.path");
		}
		return RECIVE_PATH;
	}

	public void setRECIVE_PATH(String rECIVE_PATH) {
		RECIVE_PATH = rECIVE_PATH;
	}

	

	public String getIMAGE_DOWNLOAD_PATH() {
		if (IMAGE_DOWNLOAD_PATH == null) {
			IMAGE_DOWNLOAD_PATH = PropertiesUtil.getStringProperty("batch.process.download");
		}
		return IMAGE_DOWNLOAD_PATH;
	}

	public void setIMAGE_DOWNLOAD_PATH(String iMAGE_DOWNLOAD_PATH) {
		IMAGE_DOWNLOAD_PATH = iMAGE_DOWNLOAD_PATH;
	}


	public String getHDFS_MODE_PATH() {
		if (HDFS_MODE_PATH == null) {
			HDFS_MODE_PATH = PropertiesUtil.getStringProperty("hdfs.mode.path");
		}
		return HDFS_MODE_PATH;
	}

	public void setHDFS_MODE_PATH(String hDFS_MODE_PATH) {
		HDFS_MODE_PATH = hDFS_MODE_PATH;
	}

	public String getSPARK_IP() {
		if (SPARK_IP == null) {
			SPARK_IP = PropertiesUtil.getStringProperty("spark.ip");
		}
		return SPARK_IP;
	}

	public void setSPARK_IP(String sPARK_IP) {
		SPARK_IP = sPARK_IP;
	}

	public String getSPARK_USR() {
		if (SPARK_USR == null) {
			SPARK_USR = PropertiesUtil.getStringProperty("spark.user");
		}
		return SPARK_USR;
	}

	public void setSPARK_USR(String sPARK_USR) {
		SPARK_USR = sPARK_USR;
	}

	public String getSPARK_PASSWD() {
		if (SPARK_PASSWD == null) {
			SPARK_PASSWD = PropertiesUtil.getStringProperty("spark.passwd");
		}
		return SPARK_PASSWD;
	}

	public void setSPARK_PASSWD(String sPARK_PASSWD) {
		SPARK_PASSWD = sPARK_PASSWD;
	}

	private String SPARK_PASSWD;

	public String getHBASE_MASTER() {
		if (HBASE_MASTER == null) {
			HBASE_MASTER = PropertiesUtil.getStringProperty("hbase.master");
		}
		return HBASE_MASTER;
	}

	public void setHBASE_MASTER(String hBASE_MASTER) {
		HBASE_MASTER = hBASE_MASTER;
	}

	public String getZK_QUORUM() {
		if (ZK_QUORUM == null) {
			ZK_QUORUM = PropertiesUtil
					.getStringProperty("hbase.zookeeper.quorum");
		}
		return ZK_QUORUM;
	}

	public void setZK_QUORUM(String zK_QUORUM) {
		ZK_QUORUM = zK_QUORUM;
	}

	public String getZK_CLIENR_PORT() {
		if (ZK_CLIENR_PORT == null) {
			ZK_CLIENR_PORT = PropertiesUtil
					.getStringProperty("hbase.zookeeper.property.clientPort");
		}
		return ZK_CLIENR_PORT;
	}

	public void setZK_CLIENR_PORT(String zK_CLIENR_PORT) {
		ZK_CLIENR_PORT = zK_CLIENR_PORT;
	}

	public String getZK_ZNODE_PARENT() {
		if (ZK_ZNODE_PARENT == null) {
			ZK_ZNODE_PARENT = PropertiesUtil
					.getStringProperty("zookeeper.znode.parent");
		}
		return ZK_ZNODE_PARENT;
	}

	public void setZK_ZNODE_PARENT(String zK_ZNODE_PARENT) {
		ZK_ZNODE_PARENT = zK_ZNODE_PARENT;
	}

	public String getHBASE_KEYVALUE_MAXSIZE() {
		if (HBASE_KEYVALUE_MAXSIZE == null) {
			HBASE_KEYVALUE_MAXSIZE = PropertiesUtil
					.getStringProperty("hbase.client.keyvalue.maxsize");
		}
		return HBASE_KEYVALUE_MAXSIZE;
	}

	public void setHBASE_KEYVALUE_MAXSIZE(String hBASE_KEYVALUE_MAXSIZE) {
		HBASE_KEYVALUE_MAXSIZE = hBASE_KEYVALUE_MAXSIZE;
	}

	public int getHBASE_READ_PROCESS() {
		if (HBASE_READ_PROCESS == null) {
			HBASE_READ_PROCESS = PropertiesUtil
					.getIntProperty("hbase.read.process");
		}
		return HBASE_READ_PROCESS;
	}

	public void setHBASE_READ_PROCESS(int hBASE_READ_PROCESS) {
		HBASE_READ_PROCESS = hBASE_READ_PROCESS;
	}

	public String getSOLR_HTTP_URL() {
		if (SOLR_HTTP_URL == null) {
			SOLR_HTTP_URL = PropertiesUtil.getStringProperty("solr.http.solr");
		}
		return SOLR_HTTP_URL;
	}

	public void setSOLR_HTTP_URL(String sOLR_HTTP_URL) {
		SOLR_HTTP_URL = sOLR_HTTP_URL;
	}
	
	public int getSOLR_QUEUE_SIZE() {
		if (SOLR_QUEUE_SIZE == null) {
			SOLR_QUEUE_SIZE = PropertiesUtil.getIntProperty("solr.queue.size");
		}
		return SOLR_QUEUE_SIZE;
	}

	public void setSOLR_QUEUE_SIZE(int sOLR_QUEUE_SIZE) {
		SOLR_QUEUE_SIZE = sOLR_QUEUE_SIZE;
	}

	public int getSOLR_THREAD_COUNT() {
		if (SOLR_THREAD_COUNT == null) {
			SOLR_THREAD_COUNT = PropertiesUtil.getIntProperty("solr.thread.count");
		}
		return SOLR_THREAD_COUNT;
	}

	public void setSOLR_THREAD_COUNT(int sOLR_THREAD_COUNT) {
		SOLR_THREAD_COUNT = sOLR_THREAD_COUNT;
	}
	
	public String getHDFS_DEFAULT_FS() {
		if (HDFS_DEFAULT_FS == null) {
			HDFS_DEFAULT_FS = PropertiesUtil.getStringProperty("fs.defaultFS");
		}
		return HDFS_DEFAULT_FS;
	}

	public void setHDFS_DEFAULT_FS(String hDFS_DEFAULT_FS) {
		HDFS_DEFAULT_FS = hDFS_DEFAULT_FS;
	}
	
	public static void main(String[] arg){
		System.out.println(Constants.getInstance().getHBASE_KEYVALUE_MAXSIZE());
		System.out.println(Constants.getInstance().getHBASE_MASTER());
		System.out.println(Constants.getInstance().getHBASE_READ_PROCESS());
		System.out.println(Constants.getInstance().getSOLR_HTTP_URL());
		System.out.println(Constants.getInstance().getZK_CLIENR_PORT());
		System.out.println(Constants.getInstance().getZK_QUORUM());
		System.out.println(Constants.getInstance().getZK_ZNODE_PARENT());
		System.out.println(Constants.getInstance().getHDFS_DEFAULT_FS());
		System.out.println(Constants.getInstance().getSOLR_QUEUE_SIZE());
		System.out.println(Constants.getInstance().getSOLR_THREAD_COUNT());
	}

}
