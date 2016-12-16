package com.nuctech.hbase;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConstructorUtils;
import org.apache.commons.io.FileUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nuctech.model.SuspectImage;
import com.nuctech.model.InspectInfo;
import com.nuctech.model.Meta;
import com.nuctech.model.PageList;
import com.nuctech.model.PageQuery;
import com.nuctech.solr.SolrIndex;
import com.nuctech.solr.SolrNewIndex;
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
public class StorageSuspectImpl extends Storage<SuspectImage> {
	private static final Logger logger = LoggerFactory
			.getLogger(StorageSuspectImpl.class);
	private static Constants constants = Constants.getInstance();
	private static Configuration config = ConnectionCxn.getHbaseConf();
	private Connection connection;
	private String tableName;

	public StorageSuspectImpl(String tableName) {
		this.tableName = tableName;
	}

	/**
	 * @创建人：shangjianping
	 * @创建时间：2015-5-11
	 * @功能描述： 连接hbase
	 */
	@Override
	public void connect() {
		try {
			if (connection == null) {
				connection = ConnectionFactory.createConnection(config);
				;
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
	private Table getTable() {
		Table table = null;
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
	 * @param inspectInfo
	 * @return
	 */
	@Override
	public int insert(SuspectImage info) {
		if (info == null || info.getId() == null || info.getId().equals("")) {
			logger.info("RowKey is null,insert fail!");
			return 0;
		}
		Table table = getTable();
		int rt = 0;
		if (table == null) {
			logger.error("获取htable实例失败");
			return 0;
		}
		try {
			Put p = new Put(Bytes.toBytes(info.getId()));
			addPut(info, p, "image");
			table.put(p);
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

	public void addPut(Object obj, Put put, String family) throws Exception {
		if (obj != null) {
			for (Field field : obj.getClass().getDeclaredFields()) {
				field.setAccessible(true);
				Object property = field.get(obj);
				if (property != null && field.getType().equals(byte[].class)) {
					System.out.println(family + " : " + field.getName() + "=");
					put.addColumn(Bytes.toBytes(family),
							Bytes.toBytes(field.getName()), (byte[]) property);
				}
			}
		}
	}

	/**
	 * @创建人：shangjianping
	 * @创建时间：2015-5-11
	 * @功能描述： 批量增加存储对象
	 * @param list
	 */
	@Override
	public int insert(List<SuspectImage> list) {
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
	public SuspectImage read(String id) {
		Table table = getTable();
		if (table == null) {
			logger.error("获取htable实例失败");
			return null;
		}
		SuspectImage info = null;
		try {
			Get get = new Get(id.getBytes());
			Result rs;
			rs = table.get(get);
			info = beanSet(rs);
			System.out.println(new String(rs.toString()));
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("读取单个存储对象失败");
		}
		try {
			table.close();
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("关闭hbase表失败");
		}
		return info;
	}

	@Override
	public int isExists(String id) {
		Table table = getTable();
		if (table == null) {
			logger.error("获取htable实例失败");
			return -1;
		}
		int rt = 0;
		try {
			Get get = new Get(id.getBytes());
			boolean flag = table.exists(get);
			if (flag) {
				rt = 1;
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("读取单个存储对象失败");
		}
		try {
			table.close();
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("关闭hbase表失败");
		}
		return rt;
	}

	@Override
	public List<SuspectImage> read(Object obj) {
		return read(obj, null);
	}

	public void newInstanceByName(Object obj, String fieldPath, String field)
			throws Exception {
		String packageName = obj.getClass().getPackage().getName();
		Constructor<?> con = Class.forName(parseClassName(packageName, field))
				.getConstructor();
		BeanUtils.setProperty(obj, fieldPath, con.newInstance());

	}

	public void newListInstanceByName(Object obj, String fieldPath,
			String field, int number) throws Exception {
		List<Object> list = new ArrayList<Object>();
		String packageName = obj.getClass().getPackage().getName();
		for (int i = 0; i < number; i++) {
			Object o = ConstructorUtils.invokeConstructor(
					Class.forName(parseClassName(packageName, field)), null);
			list.add(o);
		}
		BeanUtils.setProperty(obj, fieldPath, list);

	}

	public String parseClassName(String packageName, String name) {
		if (null == name || "".equals(name)) {
			return null;
		}
		String setName = packageName + "." + name.substring(0, 1).toUpperCase()
				+ name.substring(1);
		return setName;
	}

	private SuspectImage beanSet(Result rs) {
		SuspectImage info = new SuspectImage();
		String family;
		String qualifier;
		try {
			info.setId(Bytes.toString(rs.getRow()));
			for (Cell cell : rs.listCells()) {
				family = Bytes.toString(cell.getFamilyArray(),
						cell.getFamilyOffset(), cell.getFamilyLength());
				qualifier = Bytes.toString(cell.getQualifierArray(),
						cell.getQualifierOffset(), cell.getQualifierLength());
				System.out.println("column=" + family + ":" + qualifier);
				BeanUtils.setProperty(info, qualifier,
						CellUtil.cloneValue(cell));

			}

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return info;
	}

	/**
	 * @创建人：shangjianping
	 * @创建时间：2015-5-11
	 * @功能描述： 批量读取存储对象
	 * @param inspectInfo
	 * @return
	 */
	@Override
	public PageList<SuspectImage> read(Object obj, String filterQuery,
			PageQuery pageQuery) {
		return read(obj, null, pageQuery);
	}

	@Override
	public PageList<SuspectImage> read(Object obj, PageQuery pageQuery) {
		Table table = getTable();
		if (table == null) {
			logger.error("获取htable实例失败");
			return null;
		}
		Get get = null;
		PageList<SuspectImage> page = new PageList<SuspectImage>();
		if (pageQuery != null) {
			page.setPage(pageQuery.getPage());
			page.setPageSize(pageQuery.getPageSize());
		}
		try {
			List<Get> list = new ArrayList<Get>();
			SolrNewIndex index = new SolrNewIndex();
			SolrDocumentList docs = index.query(obj, pageQuery, tableName);
			page.setTotalItems((int) docs.getNumFound());
			for (SolrDocument doc : docs) {
				get = new Get(Bytes.toBytes((String) doc.getFieldValue("id")));
				// get.addFamily(family);
				list.add(get);
				for (Entry<String, Object> entry : doc.entrySet()) {
					System.out.print(entry.getKey() + ":");
					System.out.println(entry.getValue());
				}
			}
			Result[] res = table.get(list);
			for (Result rs : res) {
				SuspectImage infoTemp = beanSet(rs);

				System.out.println(new String(rs.toString()));
				page.add(infoTemp);
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
	public int update(SuspectImage info) {
		return insert(info);
	}

	/**
	 * @创建人：shangjianping
	 * @创建时间：2015-5-11
	 * @功能描述： 删除单个存储对象
	 * @param id
	 */
	@Override
	public int delete(String id) {
		Table table = getTable();
		int rt = 0;
		if (table == null) {
			logger.error("获取htable实例失败");
			return 0;
		}
		try {
			Delete delete = new Delete(id.getBytes());
			table.delete(delete);
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
	 * @param inspectInfo
	 */
	@Override
	public int delete(Object obj) {
		Table table = getTable();
		int rt = 0;
		if (table == null) {
			logger.error("获取htable实例失败");
			return 0;
		}
		try {
			List<Delete> list = new ArrayList<Delete>();
			List<String> ids = new ArrayList<String>();
			SolrNewIndex index = new SolrNewIndex();
			SolrDocumentList docs = index.query(obj, null, tableName);
			Delete delete;
			for (SolrDocument doc : docs) {
				delete = new Delete(Bytes.toBytes((String) doc
						.getFieldValue("id")));
				list.add(delete);
				ids.add((String) doc.getFieldValue("id"));
			}
			table.delete(list);
			// index.deleteIndex(ids);
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

	public int deleteAll() {
		Table table = getTable();
		int rt = 0;
		if (table == null) {
			logger.error("获取htable实例失败");
			return 0;
		}
		try {
			List<Delete> list = new ArrayList<Delete>();
			SolrIndex index = new SolrIndex();
			SolrDocumentList docs = index.query(null, null);
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
			List<InspectInfo> list) {
		ArrayList<Map<Object, Map<String, Object>>> processList = new ArrayList<Map<Object, Map<String, Object>>>();
		int i = 0;
		int avg = list.size() / constants.getHBASE_READ_PROCESS();
		int addIndex = list.size() - avg * constants.getHBASE_READ_PROCESS();
		Map<Object, Map<String, Object>> rowKeyMap = new HashMap<Object, Map<String, Object>>();
		processList.add(rowKeyMap);
		for (InspectInfo info : list) {
			if ((processList.size() <= addIndex && i == avg + 1)
					|| (processList.size() > addIndex && i == avg)) {
				rowKeyMap = new HashMap<Object, Map<String, Object>>();
				processList.add(rowKeyMap);
				i = 0;
			}
			Object rowKey = null;
			Map<String, Object> cfMap = new HashMap<String, Object>();
			try {
				Field[] fields = info.getClass().getDeclaredFields();
				for (Field field : fields) {
					field.setAccessible(true);
					Object property = field.get(info);
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
				logger.info("inspectInfo id is null");
			}
			i++;
		}
		return processList;
	}

	public List<List<SuspectImage>> splitList(List<SuspectImage> list) {
		ArrayList<List<SuspectImage>> processList = new ArrayList<List<SuspectImage>>();
		List<SuspectImage> subList = new ArrayList<SuspectImage>();
		int i = 0;
		System.out.println(constants.getHBASE_READ_PROCESS());
		int avg = list.size() / constants.getHBASE_READ_PROCESS();
		int addIndex = list.size() - avg * constants.getHBASE_READ_PROCESS();
		processList.add(subList);
		for (SuspectImage info : list) {
			if ((processList.size() <= addIndex && i == avg + 1)
					|| (processList.size() > addIndex && i == avg)) {
				subList = new ArrayList<SuspectImage>();
				processList.add(subList);
				i = 0;
			}
			subList.add(info);
			i++;
		}
		return processList;
	}

	public static void main(String[] args) throws Exception {

		InspectInfo a = new InspectInfo();
		a.setId("0002");
		// a.setState("3");
		// a.setDate("20151112");
		// a.setSource("test");
		// a.setType("testType");
		// a.setCheckConclusion("red");
		// a.setCheckTime("1111");
		// a.setCommodityCategory("test");
		// a.setCommodityName("test");
		// a.setCreateTime("test");
		// a.setDeclarationCompany("test");
		// a.setDeclarationNo("test");
		// a.setDescription("test");
		// a.setLastUpdateTime("test");
		// a.setOperatingCompany("test");
		// a.setReleaseTime("test");
		// a.setRemark("test");
		// a.setScanTime("test");
		// a.setCreateUser("test");
		// a.setLastUpdateUser("test");
		// a.setReleaseUser("test");
		// a.setXmlContent("aaaaaaaaaaaaaaaaaaaaaaaa");
		System.out.println(new java.text.SimpleDateFormat(
				"yyyyMMdd:HH:mm:ss:SSS").format(new Date()));
		Storage<SuspectImage> storageHBaseImpl = new StorageSuspectImpl(
				"image20160120");
		// storageHBaseImpl.isExists("86574MB02201402180051");
		storageHBaseImpl.connect();
		PageQuery query = new PageQuery();
		query.setPage(1);
		query.setPageSize(2);
		// storageHBaseImpl.read("58001DH01206101070013");
		// storageHBaseImpl.insert(a);
		// storageHBaseImpl.insert(list);
		Meta meta = new Meta();
		// meta.setRiskLevel("green");
		List<SuspectImage> resultInfos = storageHBaseImpl.read(meta, query);
		// SuspectImage resultInfo =
		// ()storageHBaseImpl.read("86574MB02201402250012");
		for (SuspectImage resultInfo : resultInfos) {
			
		}
		// storageHBaseImpl.update(inspectInfo2);
		// storageHBaseImpl.delete("testaaa");
		// storageHBaseImpl.deleteAll();
		storageHBaseImpl.close();
		System.out.println(new java.text.SimpleDateFormat(
				"yyyyMMdd:HH:mm:ss:SSS").format(new Date()));
		System.out.println("ok");
	}

}
