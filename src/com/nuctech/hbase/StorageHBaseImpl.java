package com.nuctech.hbase;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.io.FileUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HConnection;
import org.apache.hadoop.hbase.client.HConnectionManager;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nuctech.model.FileInfo;
import com.nuctech.model.PageList;
import com.nuctech.model.PageQuery;
import com.nuctech.solr.SolrIndex;
import com.nuctech.storage.Storage;
import com.nuctech.utils.ConnectionCxn;
import com.nuctech.utils.Constants;

/**
 * 
 * @类功能说明：hbase存储实现
 * @作者：shangjianping
 * @创建时间：2015-5-11
 * @版本：V1.0
 */
public class StorageHBaseImpl extends Storage<FileInfo> {
	private static final Logger logger = LoggerFactory
			.getLogger(StorageHBaseImpl.class);
	private static Constants constants = Constants.getInstance();
	private HConnection connection;
	private String tableName;

	public StorageHBaseImpl(String tableName) {
		this.tableName = tableName;
	}

	/**
	 * @创建人：shangjianping
	 * @创建时间：2015-5-11
	 * @功能描述： 连接hbase
	 */
	@Override
	public void connect() {
		Configuration config = ConnectionCxn.getHbaseConf();
		try {
			if (connection == null) {
				connection = HConnectionManager.createConnection(config);
			}
		} catch (java.io.IOException e) {
			e.printStackTrace();
			logger.error("connect hbase fail");
		}
	}

	/**
	 * @创建人：shangjianping
	 * @创建时间：2015-5-11
	 * @功能描述： 关闭hbase连接
	 * @param table
	 */
	@Override
	public void close() {
		try {
			if (connection != null) {
				connection.close();
			}
		} catch (java.io.IOException e) {
			e.printStackTrace();
			logger.error("disconnect hbase fail");
		}
	}

	/**
	 * @创建人：shangjianping
	 * @创建时间：2015-5-11
	 * @功能描述： 获取htable实例
	 * @param tableName
	 * @return
	 */
	private HTableInterface getTable() {
		HTableInterface table = null;
		try {
			table = connection.getTable(TableName.valueOf(tableName));
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("get hbaseTable fail");
			try {
				connection.close();
			} catch (IOException e1) {
				e1.printStackTrace();
				logger.error("获取" + tableName + "失败");
			}
		}
		return table;
	}

	/**
	 * @创建人：shangjianping
	 * @创建时间：2015-5-11
	 * @功能描述： 增加单个存储对象
	 * @param fileInfo
	 * @return
	 */
	@Override
	public int insert(FileInfo fileInfo) {
		HTableInterface table = getTable();
		int rt = 0;
		if (table == null) {
			logger.error("获取htable实例失败");
			return 0;
		}
		try {
			Put p = null;
			Field[] fields = fileInfo.getClass().getDeclaredFields();
			for (Field field : fields) {
				field.setAccessible(true);
				Object property = field.get(fileInfo);
				if (property != null) {
					if (field.getName().equals("id")) {
						p = new Put(property.toString().getBytes());
					}else if(field.getName().equals("scanImageFolderPath")){
						File folderFile = new File(property.toString());
						for(File file : folderFile.listFiles()){
							p.add("data".getBytes(), file.getName().getBytes(),
									FileUtils.readFileToByteArray(file));
						}
					}else if(field.getName().equals("xmlFile")){
						p.add("data".getBytes(), field.getName().getBytes(),
								(byte[])property);
					}else {
						p.add("meta".getBytes(), field.getName().getBytes(),
								property.toString().getBytes("utf-8"));
					}
				}
			}

			if (p == null) {
				logger.info("RowKey is null,insert fail!");
				return 0;
			}
			table.put(p);
			table.flushCommits();
			logger.info("文件信息写入HBase成功！");
			rt = 1;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("文件信息写入HBase失败！");
		}
		try {
			table.close();
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("关闭hbase表失败");
		}
		return rt;
	}

	/**
	 * @创建人：shangjianping
	 * @创建时间：2015-5-11
	 * @功能描述： 批量增加存储对象
	 * @param list
	 */
	@Override
	public int insert(List<FileInfo> list) {
		if (list.size() == 0) {
			logger.info("Task list is none");
			return 0;
		}
		List<List<FileInfo>> taskList = splitList(list);

		HTableInterface[] table = new HTableInterface[taskList.size()];
		Thread[] threads = new Thread[taskList.size()];
		for (int i = 0; i < taskList.size(); i++) {
			table[i] = getTable();
			if (table[i] == null) {
				logger.error("获取htable实例失败");
				return 0;
			}
			table[i].setAutoFlushTo(false);
			threads[i] = new InsertThread(table[i], taskList.get(i));
			threads[i].start();
			// threads[i] = new flushCommitThread(table[i]);
		}

		for (int i = 0; i < taskList.size(); i++) {
			try {
				threads[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
				return 0;
			}
		}
		return 1;

	}

	/**
	 * @创建人：shangjianping
	 * @创建时间：2015-5-11
	 * @功能描述： 读取单个存储对象
	 * @param table
	 * @param id
	 * @return
	 */
	@Override
	public FileInfo read(String id) {
		HTableInterface table = getTable();
		if (table == null) {
			logger.error("获取htable实例失败");
			return null;
		}
		FileInfo fileInfo = null;
		try {
			Get get = new Get(id.getBytes());
			Result rs;
			rs = table.get(get);
			fileInfo = beanSet(rs);
			//fileInfo.setId(Bytes.toString(rs.getRow()));
			//fileInfo.setName(Bytes.toString(rs.getValue("meta".getBytes(),
			//		"name".getBytes())));
			System.out.println(new String(rs.toString()));
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("读取单个存储对象失败");
		}
		try {
			table.close();
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("关闭hbase表失败");
		}
		return fileInfo;
	}
	
	@Override
	public int isExists(String id){
		FileInfo fileInfo = new FileInfo();
		fileInfo.setId(id);
		SolrIndex index = new SolrIndex();
		SolrDocumentList docs = index.query(fileInfo,null);
		logger.info("流水号找到"+docs.getNumFound());
		return (int) docs.getNumFound();
	}

	@Override
	public List<FileInfo> read(Object fileInfo) {
		return read(fileInfo,null);
	}
	
	private FileInfo beanSet(Result rs){
		FileInfo fileInfo= new FileInfo();
		try{
		fileInfo.setId(Bytes.toString(rs.getRow()));
		fileInfo.setCheckConclusion(new String(rs.getValue("meta".getBytes(),"checkConclusion".getBytes()),"utf-8"));
		fileInfo.setCheckTime(new String(rs.getValue("meta".getBytes(),"checkTime".getBytes()),"utf-8"));
		fileInfo.setCommodityCategory(new String(rs.getValue("meta".getBytes(),"commodityCategory".getBytes()),"utf-8"));
		fileInfo.setCommodityName(new String(rs.getValue("meta".getBytes(),"commodityName".getBytes()),"utf-8"));
		fileInfo.setCreateTime(new String(rs.getValue("meta".getBytes(),"createTime".getBytes()),"utf-8"));
		fileInfo.setCreateUser(new String(rs.getValue("meta".getBytes(),"createUser".getBytes()),"utf-8"));
		fileInfo.setDeclarationCompany(new String(rs.getValue("meta".getBytes(),"declarationCompany".getBytes()),"utf-8"));
		fileInfo.setDeclarationNo(new String(rs.getValue("meta".getBytes(),"declarationNo".getBytes()),"utf-8"));
		fileInfo.setDescription(new String(rs.getValue("meta".getBytes(),"description".getBytes()),"utf-8"));
		fileInfo.setLastUpdateTime(new String(rs.getValue("meta".getBytes(),"lastUpdateTime".getBytes()),"utf-8"));
		fileInfo.setLastUpdateUser(new String(rs.getValue("meta".getBytes(),"lastUpdateUser".getBytes()),"utf-8"));
		fileInfo.setOperatingCompany(new String(rs.getValue("meta".getBytes(),"operatingCompany".getBytes()),"utf-8"));
		fileInfo.setReleaseTime(new String(rs.getValue("meta".getBytes(),"releaseTime".getBytes()),"utf-8"));
		fileInfo.setLastUpdateUser(new String(rs.getValue("meta".getBytes(),"lastUpdateUser".getBytes()),"utf-8"));
		fileInfo.setOperatingCompany(new String(rs.getValue("meta".getBytes(),"operatingCompany".getBytes()),"utf-8"));
		fileInfo.setReleaseTime(new String(rs.getValue("meta".getBytes(),"releaseTime".getBytes()),"utf-8"));
		fileInfo.setReleaseUser(new String(rs.getValue("meta".getBytes(),"releaseUser".getBytes()),"utf-8"));
		fileInfo.setRemark(new String(rs.getValue("meta".getBytes(),"remark".getBytes()),"utf-8"));
		fileInfo.setScanTime(new String(rs.getValue("meta".getBytes(),"scanTime".getBytes()),"utf-8"));
		fileInfo.setSource(new String(rs.getValue("meta".getBytes(),"source".getBytes()),"utf-8"));
		fileInfo.setType(new String(rs.getValue("meta".getBytes(),"type".getBytes()),"utf-8"));
		fileInfo.setXmlFile(rs.getValue("data".getBytes(),"xmlFile".getBytes()));
		if(rs.getValue("meta".getBytes(),"analysisTips".getBytes())!=null){
			fileInfo.setAnalysisTips(new String(rs.getValue("meta".getBytes(),"analysisTips".getBytes()),"utf-8"));
		}
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
		return fileInfo;
	}
	
	/**
	 * @创建人：shangjianping
	 * @创建时间：2015-5-11
	 * @功能描述： 批量读取存储对象
	 * @param fileInfo
	 * @return
	 */
	@Override
	public PageList<FileInfo> read(Object fileInfo,String filterQuery, PageQuery pageQuery) {
		return read(fileInfo ,null ,pageQuery);
	}
	
	@Override
	public PageList<FileInfo> read(Object fileInfo,PageQuery pageQuery) {
		HTableInterface table = getTable();
		if (table == null) {
			logger.error("获取htable实例失败");
			return null;
		}
		Get get = null;
		PageList<FileInfo> page = new PageList<FileInfo>();
		try {
			List<Get> list = new ArrayList<Get>();
			SolrIndex index = new SolrIndex();
			SolrDocumentList docs = index.query((FileInfo)fileInfo,pageQuery);
			page.setTotalItems((int)docs.getNumFound());
			for (SolrDocument doc : docs) {
				get = new Get(Bytes.toBytes((String) doc.getFieldValue("id")));
				list.add(get);
				for (Entry<String, Object> entry : doc.entrySet()) {
					System.out.print(entry.getKey() + ":");
					System.out.println(entry.getValue());
				}
			}
			Result[] res = table.get(list);
			for (Result rs : res) {
				FileInfo fileInfoTemp = beanSet(rs);
			
				System.out.println(new String(rs.toString()));
				page.add(fileInfoTemp);
			}

		} catch (IOException e) {
			e.printStackTrace();
			logger.error("批量读取失败");
		}
		try {
			table.close();
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("关闭hbase表失败");
		}
		return page;
	}

	@Override
	public int update(FileInfo fileInfo) {
		return insert(fileInfo);
	}

	/**
	 * @创建人：shangjianping
	 * @创建时间：2015-5-11
	 * @功能描述： 删除单个存储对象
	 * @param id
	 */
	@Override
	public int delete(String id) {
		HTableInterface table = getTable();
		int rt = 0;
		if (table == null) {
			logger.error("获取htable实例失败");
			return 0;
		}
		try {
			Delete delete = new Delete(id.getBytes());
			table.delete(delete);
			SolrIndex index = new SolrIndex();
			index.deleteIndex(id);
			logger.info(id + ":删除行成功!");
			rt = 1;
		} catch (IOException e) {
			e.printStackTrace();
			logger.error(id + ":删除行失败!");
		}
		try {
			table.close();
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("关闭hbase表失败");
		}
		return rt;

	}

	/**
	 * @创建人：shangjianping
	 * @创建时间：2015-5-11
	 * @功能描述： 批量删除存储对象
	 * @param fileInfo
	 */
	@Override
	public int delete(Object obj) {
		HTableInterface table = getTable();
		int rt = 0;
		if (table == null) {
			logger.error("获取htable实例失败");
			return 0;
		}
		try {
			List<Delete> list = new ArrayList<Delete>();
			List<String> ids = new ArrayList<String>();
			SolrIndex index = new SolrIndex();
			SolrDocumentList docs = index.query(obj,null);
			Delete delete;
			for (SolrDocument doc : docs) {
				delete = new Delete(Bytes.toBytes((String) doc
						.getFieldValue("id")));
				list.add(delete);
				ids.add((String) doc.getFieldValue("id"));
			}
			table.delete(list);
			index.deleteIndex(ids);
			logger.info("批量删除行成功!");
			rt = 1;
		} catch (IOException e) {
			e.printStackTrace();
			logger.info("批量删除行失败!");
		}
		try {
			table.close();
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("关闭hbase表失败");
		}
		return rt;

	}
	
	public int deleteAll(){
		HTableInterface table = getTable();
		int rt = 0;
		if (table == null) {
			logger.error("获取htable实例失败");
			return 0;
		}
		try {
			List<Delete> list = new ArrayList<Delete>();
			SolrIndex index = new SolrIndex();
			SolrDocumentList docs = index.query(null,null);
			Delete delete;
			for (SolrDocument doc : docs) {
				delete = new Delete(Bytes.toBytes((String) doc
						.getFieldValue("id")));
				list.add(delete);
			}
			table.delete(list);
			index.deleteAllIndex();
			logger.info("批量删除行成功!");
			rt = 1;
		} catch (IOException e) {
			e.printStackTrace();
			logger.info("批量删除行失败!");
		}
		try {
			table.close();
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("关闭hbase表失败");
		}
		return rt;
	}

	/**
	 * @创建人：shangjianping
	 * @创建时间：2015-5-11
	 * @功能描述： 切分存储任务
	 * @param list
	 * @return
	 */
	public ArrayList<Map<Object, Map<String, Object>>> splitTask(
			List<FileInfo> list) {
		ArrayList<Map<Object, Map<String, Object>>> processList = new ArrayList<Map<Object, Map<String, Object>>>();
		int i = 0;
		int avg = list.size() / constants.getHBASE_READ_PROCESS();
		int addIndex = list.size() - avg * constants.getHBASE_READ_PROCESS();
		Map<Object, Map<String, Object>> rowKeyMap = new HashMap<Object, Map<String, Object>>();
		processList.add(rowKeyMap);
		for (FileInfo fileInfo : list) {
			if ((processList.size() <= addIndex && i == avg + 1)
					|| (processList.size() > addIndex && i == avg)) {
				rowKeyMap = new HashMap<Object, Map<String, Object>>();
				processList.add(rowKeyMap);
				i = 0;
			}
			Object rowKey = null;
			Map<String, Object> cfMap = new HashMap<String, Object>();
			try {
				Field[] fields = fileInfo.getClass().getDeclaredFields();
				for (Field field : fields) {
					field.setAccessible(true);
					Object property = field.get(fileInfo);
					// System.out.print(field.getName() + ":");
					// System.out.println(property);
					if (property != null) {
						if (field.getName().equals("id")) {
							rowKey = property;
						} else {
							cfMap.put(field.getName(), property);
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (rowKey != null) {
				rowKeyMap.put(rowKey, cfMap);
			} else {
				logger.info("fileInfo id is null");
			}
			i++;
		}
		return processList;
	}
	
	public List<List<FileInfo>> splitList(
			List<FileInfo> list) {
		ArrayList<List<FileInfo>> processList = new ArrayList<List<FileInfo>>();
		List<FileInfo> subList = new ArrayList<FileInfo>();
		int i = 0;
		System.out.println(constants.getHBASE_READ_PROCESS());
		int avg = list.size() / constants.getHBASE_READ_PROCESS();
		int addIndex = list.size() - avg * constants.getHBASE_READ_PROCESS();
		processList.add(subList);
		for (FileInfo fileInfo : list) {
			if ((processList.size() <= addIndex && i == avg + 1)
					|| (processList.size() > addIndex && i == avg)) {
				subList = new ArrayList<FileInfo>();
				processList.add(subList);
				i = 0;
			}
			subList.add(fileInfo);
			i++;
		}
		return processList;
	}

	public static void main(String[] args) throws Exception {

		List<FileInfo> list = new ArrayList<FileInfo>();
		
		FileInfo fileInfo1 = new FileInfo();
		// fileInfo1.setId("004");
		//fileInfo1.setDate("aaa");
		FileInfo a = new FileInfo();
		a.setId("0002");
		a.setState("3");
		a.setDate("20151112");
//		a.setSource("test");
//		a.setType("testType");
//		a.setCheckConclusion("red");
//		a.setCheckTime("1111");
//		a.setCommodityCategory("test");
//		a.setCommodityName("test");
//		a.setCreateTime("test");
//		a.setDeclarationCompany("test");
//		a.setDeclarationNo("test");
//		a.setDescription("test");
//		a.setLastUpdateTime("test");
//		a.setOperatingCompany("test");
//		a.setReleaseTime("test");
//		a.setRemark("test");
//		a.setScanTime("test");
//		a.setCreateUser("test");
//		a.setLastUpdateUser("test");
//		a.setReleaseUser("test");
//		a.setXmlContent("aaaaaaaaaaaaaaaaaaaaaaaa");
		System.out.println(new java.text.SimpleDateFormat(
				"yyyyMMdd:HH:mm:ss:SSS").format(new Date()));
		StorageHBaseImpl storageHBaseImpl = new StorageHBaseImpl("test11");
		//storageHBaseImpl.isExists("86574MB02201402180051");
		storageHBaseImpl.connect();
		 PageQuery query = new PageQuery();
	        query.setPage(2);
	        query.setPageSize(10);
	        //storageHBaseImpl.read("58001DH01206101070013");
		storageHBaseImpl.insert(a);
		//storageHBaseImpl.insert(list);
		// storageHBaseImpl.read(fileInfo1);
		// storageHBaseImpl.update(fileInfo2);
		//storageHBaseImpl.delete("testaaa");
		//storageHBaseImpl.deleteAll();
		storageHBaseImpl.close();
		System.out.println(new java.text.SimpleDateFormat(
				"yyyyMMdd:HH:mm:ss:SSS").format(new Date()));
		System.out.println("ok");
	}
}
